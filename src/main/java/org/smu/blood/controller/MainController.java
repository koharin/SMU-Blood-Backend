package org.smu.blood.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.smu.blood.api.JWTService;
import org.smu.blood.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@EnableMongoRepositories(basePackages="org.smu.blood")
@RestController
public class MainController {
    @Autowired
    JWTService jwtService;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    ApplyRepository applyRepository;
    @Autowired
    UserRepository userRepository;

    // Request Register request
    @PostMapping("main/registerRequest")
    public boolean bloodRequest(@RequestHeader String token, @RequestBody Request request){
        System.out.println("[+] Request Register");

        if(jwtService.checkTokenExp(token)){
            // token에서 userId 가져오기
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);
            System.out.println("[+] "+ request.toString());

            request.setRequestId((int)requestRepository.count()+1);
            request.setUserId(userId);

            System.out.println("[+] "+ request);
            // insert request document in Request collection
            requestRepository.insert(request);
            return true;
        }
        return false;
    }
    
    // get all list of request
    @GetMapping("main/list")
    public List<Request> allRequestList() {
    	System.out.println("[+] Get list of request");

        List<Request> list = requestRepository.findAll();
        List<Request> result = new ArrayList<>();

    	try{
    		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date currentDate = new Date();
            
            System.out.println("[+] current Date: " + currentDate);

            for (Request request : list) {
                Date endDate = format.parse(request.getEndDate());
                System.out.println("[+] request end date: " + endDate);
                if (!endDate.before(currentDate))
                    result.add(request);
            }
            return result;
        } catch (ParseException e){
            e.printStackTrace();
        }
		return result; 
    }

    // apply blood donation
    @PostMapping("main/apply")
    public int bloodApply(@RequestHeader String token, @RequestBody HashMap<String,String> applyInfo){
        System.out.println("[+] Apply Register request from Android");

        Apply apply = new Apply();
        if(jwtService.checkTokenExp(token)){
            // token에서 userId 가져오기
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);
            
            // if same requestId, userId in apply collection, reject
            if(applyRepository.findByRequestIdAndUserId(Integer.parseInt(applyInfo.get("requestId")), userId) != null) {
            	System.out.println("[-] Already apply in this request");
            	return 401;
            }
            
            // setting for apply info
            apply.setApplyId((int) applyRepository.count() + 1);
            apply.setUserId(userId);
            apply.setApplyDate(applyInfo.get("applyDate"));
            apply.setRequestId(Integer.parseInt(applyInfo.get("requestId")));
            System.out.println("[+] "+apply);
            // insert apply into Apply Collection
            applyRepository.insert(apply);
            
           // increase applicantNum in request document
            Request request = requestRepository.findByRequestId(Integer.parseInt(applyInfo.get("requestId")));
            if( request != null) {
            	System.out.println("[+] request info for apply(before update): " + request);
            	request.setApplicantNum(request.getApplicantNum()+1);
            	
            	// update request document in Request collection
            	requestRepository.save(request);
            	System.out.println("[+] request info for apply(after update): " + request);
        
            	return 200;
            }
        }
        return 400;
    }
    
    //get my request list
    @GetMapping("main/myRequest/myRequestList")
    public List<Request> requestList(@RequestHeader String token){
    	System.out.println("[+] Get my request list");
    	
    	if(jwtService.checkTokenExp(token)){
            // token에서 userId 가져오기
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);
            
            // find request list by userId
            List<Request> requestlist = requestRepository.findByUserId(userId);
            for(int i=0; i<requestlist.size(); i++) System.out.println("[+] Request["+i+"]: " + requestlist.get(i));
            
            return requestlist;
    	}
    	
    	return null;
    }
    
    // get apply list of my request
    @PostMapping("main/myRequest/applyList")
    public List<Apply> applyOfMyRequest(@RequestBody int requestId){
    	System.out.println("[+] Get apply list of my request");
    	
    	List<Apply> applylist = applyRepository.findByRequestId(requestId);
    	for(int i=0; i<applylist.size(); i++) System.out.println("[+] Apply["+i+"]: " + applylist.get(i));
    	
    	return applylist;
    }
    
    // get my apply list
    @GetMapping("main/myApply/myApplyList")
    public List<Apply> applyList(@RequestHeader String token){
    	System.out.println("[+] Get my apply list request from Android");
    	
        if(jwtService.checkTokenExp(token)){
            // token에서 userId 가져오기
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);
            
            // find apply list by userId
            List<Apply> applyList = applyRepository.findByUserId(userId);
            for(int i=0; i<applyList.size(); i++) System.out.println("[+] Apply["+i+"]: " + applyList.get(i));
            
            return applyList;
        }else {
        	return null;
        }
    }
    
    // get request of my apply
    @PostMapping("main/myApply/request")
    public Request requestOfMyApply(@RequestBody int requestId){
    	System.out.println("[+] Get request of my apply request from Android");
    	Request requestInfo = requestRepository.findByRequestId(requestId);
    	System.out.println("[+] request info of my apply: " + requestInfo.toString());
    	
    	return requestInfo;
    }
       
}
