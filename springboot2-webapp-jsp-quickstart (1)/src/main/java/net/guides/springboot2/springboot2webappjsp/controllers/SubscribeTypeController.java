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
//    private float tier_0;
//    private float tier_1;
//    private float tier_2;

    @PostMapping(path)
    public Result addPrice(HttpServletRequest request, @RequestBody SubscriptionType subscriptionType){
        Result result=new Result();
        String email = JwtUtil.getUserEmailByToken(request);
        User userQueryResult = userRepo.getUserByEmail(email);
        SubscriptionType existedSubscriptionTypeQueryResult=subscriptionTypeRepo.getSubscriptionTypeByUserId(userQueryResult.getId());
        int subscriptionId=0;
        if(existedSubscriptionTypeQueryResult!=null){
//            result.setMsg("Record existed. Please update instead of update.");
//            result.setCode(1);
//            return result;
            subscriptionId=existedSubscriptionTypeQueryResult.getSubscriptionTypeId();
        }else{
            subscriptionType.getSubscriptionTypeId();
        }

        SubscriptionType newSubscriptionType=new SubscriptionType(subscriptionId,
                                                                  userQueryResult.getId(),
                                                                  subscriptionType.getTier_0(),
                                                                  subscriptionType.getTier_1(),
                                                                  subscriptionType.getTier_2());
        SubscriptionType saveResult=subscriptionTypeRepo.save(newSubscriptionType);
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
