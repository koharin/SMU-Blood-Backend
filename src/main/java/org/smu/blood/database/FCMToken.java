package org.smu.blood.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "FCMToken")
public class FCMToken {
    @Id private String userId;
    private String token;

    public FCMToken(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public void setUserId(String value){ userId = value; }
    public void setToken(String value){ token = value; }
    public String getToken(){ return token; }
    public String getUserId(){ return userId; }

    @Override
    public String toString() {
        return "FCMToken[" + "userId='" + userId + ',' + ", token='" + token + ']';
    }
}
