package org.smu.blood.controller;

import org.smu.blood.api.JWTService;
import org.smu.blood.database.Apply;
import org.smu.blood.database.ApplyRepository;
import org.smu.blood.database.Request;
import org.smu.blood.database.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
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

            request.setUserId(userId);

            System.out.println("[+] "+ request);
            // insert request document in Request collection
            requestRepository.insert(request);
            return true;
        }
        return false;
    }

    // apply blood donation
    @PostMapping("main/apply")
    public boolean bloodApply(@RequestHeader String token, @RequestBody Apply apply){
        System.out.println("[+] Apply Register from Android");

        if(jwtService.checkTokenExp(token)){
            // token에서 userId 가져오기
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);

            apply.setUserId(userId);

            System.out.println("[+] "+ apply);
            // insert request document in Request collection
            applyRepository.insert(apply);
            return true;
        }
        return false;
    }
}
