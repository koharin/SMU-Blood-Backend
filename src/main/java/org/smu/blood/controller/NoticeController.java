package org.smu.blood.controller;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.smu.blood.api.JWTService;
import org.smu.blood.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EnableMongoRepositories(basePackages="org.smu.blood")
@RestController
public class NoticeController {
    @Autowired
    JWTService jwtService;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    FCMTokenRepository fcmTokenRepository;

    public static final String FCM_SERVER_API = "https://fcm.googleapis.com/fcm/send";

    // get all my notice history
    @GetMapping("notice/list")
    public List<Notification> noticeList(@RequestHeader String token){
        System.out.println("[+] get user notice list");

        if(jwtService.checkTokenExp(token)){
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);

            // get user notice list where deleteState == false
            List<Notification> list = notificationRepository.findByUserIdAndDeleteStateOrderByNoticeIdDesc(userId, false);
            System.out.println("[+] list size: " + list.size());

            if(list.size() > 0){
                // for latest notice is first
                System.out.println("[+] notice list exists");

                // get all notice list (exclude deleteState == true)
                for (Notification notification : list) {
                    System.out.println("[+] notice history for request: " + notification);
                }
            }else{
                System.out.println("[+] no notice list");
            }

            return list;
        }else{
            System.out.println("[-] Invalid token");
        }
        return null;
    }

    // return request list that had notice
    @GetMapping("notice/requestlist")
    public List<Request> requestlistOfNotice(@RequestHeader String token){
        System.out.println("[+] request list of notice request from android");
        System.out.println("token: " + token);

        if(jwtService.checkTokenExp(token)){
            // token에서 userId 가져오기
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);

            List<Request> requestList = new ArrayList<>();
            List<Notification> list = notificationRepository.findByUserId(userId);
            if(list.size() > 0){
                for (Notification notification : list) {
                    Request request = requestRepository.findByRequestId(notification.getRequestId()).get();
                    System.out.println("[+] request with notice: " + request);
                    requestList.add(request);
                }
                return requestList;
            }else{
                System.out.println("[+] no notification entity for user");
            }
        }else{
            System.out.println("[+] invalid token");
        }
        return null;
    }

    // update notice state
    @PostMapping("notice/updateState")
    public boolean updateNoticeState(@RequestHeader String token, @RequestBody int noticeId){
        System.out.println("\n[+] update notice state request from android");
        System.out.println("token: " + token);

        if(jwtService.checkTokenExp(token)){
            // token에서 userId 가져오기
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);

            if(notificationRepository.findByNoticeId(noticeId).isPresent()){
                Notification notification = notificationRepository.findByNoticeId(noticeId).get();
                notification.setNotState(false);
                System.out.println("[+] updated: " + notification);
                notificationRepository.save(notification);
                return true;
            }else{
                System.out.println("[-] no such notification");
            }

        }else{
            System.out.println("[-] invalid token");
        }
        return false;
    }

    // set notice deleteState true
    @PostMapping("notice/deleteState")
    public boolean setDeleteState(@RequestHeader String token, @RequestBody int noticeId){
        System.out.println("\n[+] update delete state request from android");
        System.out.println("token: " + token);

        if(jwtService.checkTokenExp(token)){
            // token에서 userId 가져오기
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);

            // if notification entity present, update deleteState to true
            if(notificationRepository.findByNoticeId(noticeId).isPresent()){
                Notification notification = notificationRepository.findByNoticeId(noticeId).get();

                notification.setDeleteState(true);
                System.out.println("[+] updated: " + notification);
                notificationRepository.save(notification);

                return true;
            }else{
                System.out.println("[-] no such notification");
            }

        }else{
            System.out.println("[-] invalid token");
        }
        return false;
    }

    @PostMapping("notice/sendPush")
    public String sendPush(@RequestBody int requestId) throws Exception{
        if(requestRepository.findByRequestId(requestId).isPresent()){
            String userId = requestRepository.findByRequestId(requestId).get().getUserId();
            System.out.println("[+] userId: " + userId);

            if(fcmTokenRepository.findById(userId).isPresent()){
                String registrationToken = fcmTokenRepository.findById(userId).get().getToken();

                System.out.println("[+] fcmToken: " + registrationToken);

                // initialize Admin SDK using OAuth 2.0 refresh token
                FileInputStream refreshToken = new FileInputStream("/Users/koharin/FCMPush/blring-push-firebase-adminsdk-j3ftj-bad95bfde3.json");
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(refreshToken))
                        .setProjectId("538005555008")
                        .build();
                FirebaseApp.initializeApp(options);

                // android message
                Message message = Message.builder()
                        .setAndroidConfig(AndroidConfig.builder()
                                .setTtl(3600*1000)
                                .setPriority(AndroidConfig.Priority.HIGH)
                                .setRestrictedPackageName("org.smu.blood") // 애플리케이션 패키지 이름
                                .setDirectBootOk(true) //
                                .setNotification(AndroidNotification.builder()
                                        .setTitle("BLRING") // 알림 제목
                                        .setBody("헌혈 요청글에 헌혈이 신청되었습니다.") // 알림 본문
                                        //.setClickAction("")
                                        .setIcon("@drawable/bling")
                                        .build())
                                .build())
                        .setToken(registrationToken) // 요청자의 디바이스에 대한 registration token으로 설정
                        .build();
                // Send a message to the device corresponding to the provided registration token.
                String response = FirebaseMessaging.getInstance().send(message);
                // Response is a message ID string.
                System.out.println("[+] Successfully sent message: " + response);
                return response;
            }
        }else{
            System.out.println("[-] invalid");
        }
        return null;
    }
}
