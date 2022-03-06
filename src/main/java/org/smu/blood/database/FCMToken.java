package org.smu.blood.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "FCMToken")
public class FCMToken {
    @Id private String userId;
    private String token;

    public FCMToken(String userId, String fcmToken) {
        this.userId = userId;
        this.token = fcmToken;
    }

    public void setToken(String value){ token = value; }
    public String getToken(){ return token; }

    @Override
    public String toString() {
        return "FCMToken[" + "userId='" + userId + ',' + ", token='" + token + ']';
    }
}
