package org.smu.blood.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notification")
public class Notification {
    @Id private int noticeId;
    private int requestId;
    private String userId;
    private String notTime;
    private boolean notState;

    public Notification(){}
    public Notification(int noticeId, int requestId, String userId, String notTime, boolean notState){
        this.noticeId = noticeId;
        this.requestId = requestId;
        this.userId = userId;
        this.notTime = notTime;
        this.notState = notState;
    }

    public void setNoticeId(int value) { this.noticeId = value; }
    public void setRequestId(int value) { requestId = value; }
    public void setUserId(String value){ userId = value; }
    public void setNotTime(String value){ notTime = value; }
    public void setNotState(boolean value) { notState = value; }

    public int getNoticeId(){ return noticeId; }
    public int getRequestId() { return requestId; }
    public String getUserId() { return userId; }
    public String getNotTime(){ return notTime; }
    public boolean getNotState(){ return notState; }

    @Override
    public String toString() {
        return String.format("Notification[noticeId: %d, requestId: %d, userId: %s, notTime: %s, notState: %b]", noticeId, requestId, userId, notTime, notState);
    }
}
