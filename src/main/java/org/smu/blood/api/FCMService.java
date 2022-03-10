package org.smu.blood.api;

import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    Logger logger = LoggerFactory.getLogger(FCMService.class);

    // send message to android
    public String sendMessage(int requestId, String registrationToken) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600*1000)
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setRestrictedPackageName("org.smu.blood") // 애플리케이션 패키지 이름
                        .setDirectBootOk(true)
                        .setNotification(AndroidNotification.builder()
                                .setTitle("BLRING") // 알림 제목
                                .setBody("헌혈 요청글에 헌혈이 신청되었습니다.") // 알림 본문
                                .setIcon("@drawable/bling")
                                .build())
                        .build())
                .putData("requestId", Integer.toString(requestId)) // request 식별 정보(requestId) 넣기
                .setToken(registrationToken) // 요청자의 디바이스에 대한 registration token으로 설정
                .build();

        System.out.println("[+] message to send: " + message);
        logger.info("[+] message to send: " + message);

        // Send a message to the device corresponding to the provided registration token.
        String response = FirebaseMessaging.getInstance().send(message);

        return response;
    }
}
