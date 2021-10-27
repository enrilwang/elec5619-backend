package net.guides.springboot2.springboot2webappjsp.controllers;

import net.guides.springboot2.springboot2webappjsp.configuration.JwtUtil;
import net.guides.springboot2.springboot2webappjsp.domain.SubscriptionType;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.SubscriptionTypeRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SubscribeTypeController {
    private static final String path = "subscribetype";

    @Autowired
    UserRepository userRepo;
    @Autowired
    SubscriptionTypeRepository subscriptionTypeRepo;

    @GetMapping(path)
    public Result getPrice(HttpServletRequest request){
        Result result=new Result();
        String email = JwtUtil.getUserEmailByToken(request);
        User userQueryResult = userRepo.getUserByEmail(email);
        if(!userQueryResult.getIsCreator().equals("creator")){
            result.setMsg("You are not a creator.");
            result.setCode(1);
            return result;
        }
        SubscriptionType subscriptionTypeResult= subscriptionTypeRepo.getSubscriptionTypeByUserId(userQueryResult.getId());
        if(subscriptionTypeResult==null){
            result.setMsg("No such subscribe type information.");
            result.setCode(1);
            return result;
        }
        result.setCode(0);
        result.setMsg("Success");
        result.setData(subscriptionTypeResult);
        return result;
    }

//    private int subscriptionTypeId;
//    private int userId;
//    private float photo;
//    private float music;
//    private float art;

    @PostMapping(path)
    public Result addPrice(HttpServletRequest request, @RequestBody SubscriptionType subscriptionType){
        Result result=new Result();
        String email = JwtUtil.getUserEmailByToken(request);
        User userQueryResult = userRepo.getUserByEmail(email);
        SubscriptionType existedSubscriptionTypeQueryResult = subscriptionTypeRepo.getSubscriptionTypeByUserId(userQueryResult.getId());
        int subscriptionId=0;
        SubscriptionType saveResult;
        if(existedSubscriptionTypeQueryResult!=null){
            System.out.println("art ===" + subscriptionType.getArt());
            existedSubscriptionTypeQueryResult.setArt(subscriptionType.getArt());
            existedSubscriptionTypeQueryResult.setMusic(subscriptionType.getMusic());
            existedSubscriptionTypeQueryResult.setPhoto(subscriptionType.getPhoto());
            saveResult = subscriptionTypeRepo.save(existedSubscriptionTypeQueryResult);
            // subscriptionId=existedSubscriptionTypeQueryResult.getSubscriptionTypeId();
        }
        else{
            SubscriptionType newSubscribe = new SubscriptionType();
            newSubscribe.setArt(subscriptionType.getArt());
            newSubscribe.setMusic(subscriptionType.getMusic());
            newSubscribe.setPhoto(subscriptionType.getPhoto());
            newSubscribe.setUserId(userQueryResult.getId());
            saveResult = subscriptionTypeRepo.save(newSubscribe);
        }

        if (saveResult==null){
            result.setMsg("add failed");
            result.setCode(1);
            return result;
        }
        result.setMsg("Success");
        result.setCode(0);
        return result;
    }


}
