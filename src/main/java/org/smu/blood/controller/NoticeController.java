package org.smu.blood.controller;

import org.smu.blood.api.JWTService;
import org.smu.blood.database.Notification;
import org.smu.blood.database.NotificationRepository;
import org.smu.blood.database.Request;
import org.smu.blood.database.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.*;

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

    // get all my notice history
    @GetMapping("notice/list")
    public List<Notification> noticeList(@RequestHeader String token){
        if(jwtService.checkTokenExp(token)){
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);

            List<Notification> list = notificationRepository.findByUserId(userId);

            // for latest notice is first
            Collections.sort(list, Collections.reverseOrder());

            for (Notification notification : list)
                System.out.println("[+] notice history for request: " + notification);

            return list;
        }else{
            System.out.println("[-] Invalid token");
        }
        return null;
    }

    // return request if user is notification state
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
                for(int i=0; i<list.size(); i++){
                    Request request = requestRepository.findByRequestId(list.get(i).getRequestId()).get();
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
        System.out.println("[+] update notice state request from android");
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
}
