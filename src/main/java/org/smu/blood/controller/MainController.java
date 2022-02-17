package org.smu.blood.controller;

import java.util.List;

import org.smu.blood.api.JWTService;
import org.smu.blood.database.Apply;
import org.smu.blood.database.ApplyRepository;
import org.smu.blood.database.Request;
import org.smu.blood.database.RequestRepository;
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

    // get Request Register request
    @PostMapping("main/registerRequest")
    public boolean bloodRequest(@RequestHeader String token, @RequestBody Request request){
        System.out.println("[+] Request Register from Android");

        if(jwtService.checkTokenExp(token)){
            // token에서 userId 가져오기
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);
            System.out.println("[+] "+ request.toString());

            request.setRequestId((int)requestRepository.count()+1);
            request.setUserId(userId);

            System.out.println("[+] "+ request.toString());
            // insert request document in Request collection
            requestRepository.insert(request);
            return true;
        }
        return false;
    }
    
    // get list of request
    @GetMapping("main/list")
    public List<Request> requestList(){
    	System.out.println("[+] Get list of request from Android");
    	
    	List<Request> list = requestRepository.findAll();
    	for(int i=0; i<list.size(); i++) System.out.println("[+] Request["+i+"]: " + list.get(i));
    	
    	return list;
    }

    // apply blood donation
    @PostMapping("main/apply")
    public boolean bloodApply(@RequestHeader String token, @RequestBody Apply apply){
        System.out.println("[+] Apply Register from Android");

        if(jwtService.checkTokenExp(token)){
            // token에서 userId 가져오기
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);

            apply.setApplyId((int) applyRepository.count() + 1);
            apply.setUserId(userId);

            System.out.println("[+] "+ apply);
            // insert request document in Request collection
            applyRepository.insert(apply);
            return true;
        }
        return false;
    }
}
