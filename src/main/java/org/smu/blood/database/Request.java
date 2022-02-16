package org.smu.blood.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Request {
    @Id private int requestId;
    private String userId;
    private int hospitalId;
    private int bloodType;
    private boolean rhType;
    private int donationType; // 헌혈 종류
    private int wardNum;
    private String patientName;
    private int patientNum;
    private String protectorContact;
    private String startDate;
    private String endDate;
    private String registerTime;
    private String story;
    private int applicantNum;

    public Request(int requestId, String userId, int hospitalId, int bloodType, boolean rhType, int donationType, int wardNum, String patientName, int patientNum, String protectorContact, String startDate, String endDate, String registerTime, String story, int applicantNum){
        this.requestId = requestId;
        this.userId = userId;
        this.hospitalId = hospitalId;
        this.bloodType = bloodType;
        this.rhType = rhType;
        this.donationType = donationType;
        this.wardNum = wardNum;
        this.patientName = patientName;
        this.patientNum = patientNum;
        this.protectorContact = protectorContact;
        this.startDate = startDate;
        this.endDate = endDate;
        this.registerTime = registerTime;
        this.story = story;
        this.applicantNum = applicantNum;
    }

    public void setUserId(String value){ userId = value; }

}
