package org.smu.blood.api;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FCMInitializer {

    @PostConstruct
    public void initialize() throws IOException {
        // initialize Admin SDK using OAuth 2.0 refresh token

        FileInputStream refreshToken = new FileInputStream("/Users/koharin/FCMPush/blring-push-firebase-adminsdk-j3ftj-bad95bfde3.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .setProjectId("538005555008")
                .build();

        FirebaseApp.initializeApp(options);
        System.out.println("[+] Firebase application initialized");
    }
}
