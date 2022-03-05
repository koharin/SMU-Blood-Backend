package org.smu.blood.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notification")
public class Notification {
    @Id private String userId;
    private String token;
    private boolean notState;

    public Notification(){}
    public Notification(String userId, String token, boolean notState){
        this.userId = userId;
        this.token = token;
        this.notState = notState;
    }

    public void setUserId(String value){ userId = value; }
    public void setToken(String value){ token = value; }
    public void setNotState(boolean value) { notState = value; }
    public String getUserId() { return userId; }
    public String getToken() { return token; }
    public boolean getNotState(){ return notState; }

    @Override
    public String toString() {
        return "Notification{" + "userId='" + userId + ',' + ", token='" + token + ',' + '}';
    }
}
