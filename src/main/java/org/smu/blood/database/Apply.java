package org.smu.blood.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Apply")
public class Apply {
    @Id private int applyId;
    private String userId;
    private int requestId;
    private String applyDate;

    public Apply() {}
    public Apply(int applyId, String userId, int requestId, String applyDate){
        this.applyId = applyId;
        this.userId = userId;
        this.requestId = requestId;
        this.applyDate = applyDate;
    }

    public int getApplyId() { return applyId; }
    public String getUserId() { return userId; }
    public int getRequestId() { return requestId; }
    public String getApplyDate() { return applyDate; }
    
    public void setApplyId(int value){ applyId = value; }
    public void setUserId(String value){ userId = value; }
    public void setRequestId(int value) {requestId = value; }
    public void setApplyDate(String value) { applyDate = value; }
    
    public String toString() { return String.format("Apply[applyId: %d, userId: %s, requestId: %d, applyDate: %s]", applyId, userId, requestId, applyDate); }
}
