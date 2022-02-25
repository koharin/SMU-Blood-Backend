package org.smu.blood.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Request")
public class Request {
    @Id private int requestId;
    private String userId;
    private int hospitalId;
    private int bloodType;
    private boolean rhType;
    private String donationType; // 헌혈 종류
    private int wardNum;
    private String patientName;
    private int patientNum;
    private String protectorContact;
    private String startDate;
    private String endDate;
    private String registerTime;
    private String story;
    private int applicantNum;
    private boolean state;

    public Request() {}
    
    public Request(int requestId, String userId, int hospitalId, int bloodType, boolean rhType, String donationType, int wardNum, String patientName, int patientNum, String protectorContact, String startDate, String endDate, String registerTime, String story, int applicantNum, boolean state){
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
        this.state = state;
    }

    public int getRequestId() { return requestId; }
    public String getUserId() { return userId; }
    public int getHospitalId( ){ return hospitalId; }
    public int getBloodType() { return bloodType; }
    public boolean getRhType() { return rhType; }
    public String getDonationType() { return donationType; }
    public int getWardNum() { return wardNum; }
    public String getPatientName() { return patientName; }
    public int getPatientNum() { return patientNum; }
    public String getProtectorContact() { return protectorContact; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getRegisterTime() { return registerTime; }
    public String getStory() { return story; }
    public int getApplicantNum() { return applicantNum; }
    public boolean getState() { return state; }
    
    public void setRequestId(int value){ requestId = value; }
    public void setUserId(String value){ userId = value; }
    public void setHospitalId(int value) { hospitalId = value; }
    public void setBloodType(int value) { bloodType = value; }
    public void setRhType(boolean value) { rhType = value; }
    public void setDonationType(String value) { donationType = value; }
    public void setWardNum(int value) { wardNum = value; }
    public void setPatientName(String value) { patientName = value; }
    public void setPatientNum(int value) { patientNum = value; }
    public void setProtectorContact(String value) { protectorContact = value; }
    public void setStartDate(String value) { startDate = value; }
    public void setEndDate(String value) { endDate = value; }
    public void setRegisterTime(String value) { registerTime = value; }
    public void setStory(String value) { story = value; }
    public void setApplicantNum(int value) { applicantNum = value; }
    public void setState(boolean value) { state = value; }
    
    public String toString() { return String.format("Request[requestId: %d. userId: %s, hospitalId: %d, bloodType: %d, rhType: %b, donationType: %s, wardNum: %d, patientName: %s, patientNum: %d, protectorContact: %s, startDate: %s, endDate: %s, registerTime: %s, story: %s, applicantNum: %d, state: %b]", requestId,userId,hospitalId,bloodType,rhType,donationType,wardNum,patientName,patientNum,protectorContact,startDate,endDate,registerTime,story,applicantNum, state); }

}
