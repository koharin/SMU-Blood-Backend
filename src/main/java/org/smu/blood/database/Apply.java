package org.smu.blood.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Apply {
    @Id private int applyId;
    private String userId;
    private int requestId;
    private String applyDate;

    public Apply(int applyId, String userId, int requestId, String applyDate){
        this.applyId = applyId;
        this.userId = userId;
        this.requestId = requestId;
        this.applyDate = applyDate;
    }

    public void setUserId(String value){ userId = value; }
}
