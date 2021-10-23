package net.guides.springboot2.springboot2webappjsp.controllers;

import net.guides.springboot2.springboot2webappjsp.configuration.JwtUtil;
import net.guides.springboot2.springboot2webappjsp.domain.Subscribe;
import net.guides.springboot2.springboot2webappjsp.domain.SubscriptionType;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.PaymentRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.SubscribeRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.SubscriptionTypeRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class SubscribeController {
    private static final String path = "subscribe";

    @Autowired
    SubscribeRepository subscribeRepo;
    @Autowired
    SubscriptionTypeRepository subscriptionTypeRepo;
    @Autowired
    PaymentRepository paymentRepo;
    @Autowired
    UserRepository userRepo;

    @PostMapping(path)
    public Result postSubscribe(HttpServletRequest request, @RequestBody Subscribe subscribe, @RequestParam int month) {
        Result result = new Result();
        User userQueryResult;
        SubscriptionType subscriptionTypeQueryResult;

        List<Subscribe> subscribeQueryResult;
        User userBalanceQueryResult;

        /*validate data*/
        //check creator
        userQueryResult = userRepo.getUserById(subscribe.getCreatorId());
        if (userQueryResult == null) {
            result.setMsg("Creator is not existed");
            result.setCode(1);
            return result;
        } else {
            if (!userQueryResult.getIsCreator().equals("creator")) {
                result.setMsg("This user you subscribed is not a creator");
                result.setCode(1);
                return result;
            }
        }

        //check subscriber
        String email = JwtUtil.getUserEmailByToken(request);
        userQueryResult = userRepo.getUserByEmail(email);
        if (userQueryResult == null) {
            result.setMsg("User is not existed");
            result.setCode(1);
            return result;
        }

        //check type id
        subscriptionTypeQueryResult = subscriptionTypeRepo.getSubscriptionTypeBySubscriptionTypeId(subscribe.getSubscriptionTypeId());
        if (subscriptionTypeQueryResult == null) {
            result.setMsg("Subscription definition is not existed.");
            result.setCode(1);
            return result;
        }

        //check period
        if (subscribe.getSubscribeType() > 2 || subscribe.getSubscribeType() < 0) {
            result.setMsg("Subscription type is not existed.");
            result.setCode(1);
            return result;
        }

        if (month < 1) {
            result.setMsg("Month number should be a positive integer.");
            result.setCode(1);
            return result;
        }

        if (month > 100) {
            result.setMsg("Are you sure to subscribe a century?");
            result.setCode(1);
            return result;
        }

        //validate logic
        //check is activated
        //actually need sort**
        subscribeQueryResult = subscribeRepo.getSubscribeByUserIdAndCreatorIdAndActivated(userQueryResult.getId(), subscribe.getCreatorId(), true);
        if ((subscribeQueryResult!=null&&subscribeQueryResult.size()>0)&&subscribeQueryResult.get(0).isActivated() == true) {
            result.setMsg("Already activated. No need to subscribe again");
            result.setCode(1);
            return result;
        }

        //set target price
        float price = 0;
        if (subscribe.getSubscribeType() == 0) {
            price = subscriptionTypeQueryResult.getPhoto();
        }
        if (subscribe.getSubscribeType() == 1) {
            price = subscriptionTypeQueryResult.getMusic();
        }
        if (subscribe.getSubscribeType() == 2) {
            price = subscriptionTypeQueryResult.getArt();
        }

        //check account balance
        if (userQueryResult.getAccountBalance() < price*month) {
            result.setMsg("Account balance is not enough.");
            result.setCode(1);
            return result;
        }

        //start subscribe
        List<Subscribe> subscribeFalseQueryResult = subscribeRepo.getSubscribeByUserIdAndCreatorIdAndActivated(userQueryResult.getId(), subscribe.getCreatorId(), false);
        Collections.sort(subscribeFalseQueryResult, Collections.reverseOrder());
        Subscribe subscribeFalseEle=null;
        //format date
        Date currentDate=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(subscribeFalseQueryResult.size()>0){
            Date temp=null;
            subscribeFalseEle=subscribeFalseQueryResult.get(0);
            try{
                 temp = sdf.parse(subscribeFalseEle.getTimeEnd());
            }catch (Exception e){
                result.setMsg("Date Error");
                result.setCode(1);
                return result;
            }
            if (temp.after(new Date())) {
                currentDate=temp;
            }
        }

        String startTime = sdf.format(currentDate);
        //check period
        if (subscribe.getSubscribeType() == 0) {
            currentDate.setMonth(currentDate.getMonth() + 1*month);
        }
        if (subscribe.getSubscribeType() == 1) {
            currentDate.setMonth(currentDate.getMonth() + 1*month);
        }
        if (subscribe.getSubscribeType() == 2) {
            currentDate.setMonth(currentDate.getMonth() + 1*month);
        }
        String endTime = sdf.format(currentDate);

        //new subscribe info
        Subscribe newSubscribe = new Subscribe(subscribe.getSubscribeId(),
                true,
                subscribe.getCreatorId(),
                subscribe.getPaymentsSerialId(),
                subscribe.getSubscribeType(),
                subscribe.getSubscriptionTypeId(),
                userQueryResult.getId(),
                startTime,
                endTime);

        //update subscribe
        Subscribe subscribeInsertResult = subscribeRepo.save(newSubscribe);
        if (subscribeInsertResult == null) {
            result.setMsg("Update subscribe information failed. Balance is rolled back.");
            result.setCode(1);
            return result;
        }

        //update user balance & add subscribe id
        userQueryResult.setAccountBalance(userQueryResult.getAccountBalance() - price*month);
        Set<Integer> newSubscribeId = IntArrayStringToIntArray.intArrayStringToIntArray(userQueryResult.getSubscribeId());
        newSubscribeId.add(subscribeInsertResult.getCreatorId());
        userQueryResult.setSubscribeId(newSubscribeId.toString());
        userBalanceQueryResult = userRepo.save(userQueryResult);
        if (userBalanceQueryResult == null) {
            result.setMsg("Update balance failed. But subscribe is updated.");
            result.setCode(1);
            return result;
        }

        result.setMsg("Subscribe Success");
        result.setCode(0);
        return result;
    }

    @GetMapping(path)
    public Result getSubscribe(HttpServletRequest request, @RequestParam(name = "subscribeId", required = false) Integer subscribeId,
                               @RequestParam(name = "userId", required = false) Integer userId) {
        Result result = new Result();
        if (userId != null) {
            //return all valid subscription
            List<Subscribe> allUserSubscription = subscribeRepo.getSubscribeByUserId(userId);
            if (allUserSubscription == null||allUserSubscription.isEmpty()) {
                result.setMsg("No subscription information");
                result.setCode(1);
                return result;
            }
            result.setData(allUserSubscription);
            result.setCode(0);
            result.setMsg("Success");
            return result;
        }
        if (subscribeId != null) {
            //return specific subscription
            Subscribe subscribe = subscribeRepo.getSubscribeBySubscribeId(subscribeId);
            if (subscribe == null) {
                result.setMsg("No subscription information");
                result.setCode(1);
                return result;
            }
            result.setCode(0);
            result.setData(subscribe);
            result.setMsg("Success");
            return result;
        }
        String email = JwtUtil.getUserEmailByToken(request);
        if(request==null){
            result.setMsg("No Http token!");
            result.setCode(1);
            return result;
        }
        User userQueryResult = userRepo.getUserByEmail(email);
        if(userQueryResult==null){
            result.setMsg("Current User Null!");
            result.setCode(1);
            return result;
        }
        List<Subscribe> record=subscribeRepo.getSubscribeByUserId(userQueryResult.getId());
        result.setData(record);
        result.setMsg("Success!");
        result.setCode(0);
        return result;
    }

    @DeleteMapping(path)
    public Result cancelSubscribe(HttpServletRequest request, @RequestParam int creatorId) {
        Result result = new Result();
        String email = JwtUtil.getUserEmailByToken(request);
        User userQueryResult = userRepo.getUserByEmail(email);
        List<Subscribe> subscribeR = subscribeRepo.getSubscribeByUserIdAndCreatorIdAndActivated(userQueryResult.getId(),creatorId,true);
        if (subscribeR==null||subscribeR.size()<=0) {
            result.setCode(1);
            result.setMsg("No activated subscribe");
            return result;
        }
        Subscribe subscribe=subscribeR.get(0);
        subscribe.setActivated(false);
        Subscribe subscribeQueryResult = subscribeRepo.save(subscribe);
        if (subscribeQueryResult == null) {
            result.setMsg("Save subscribe information error");
            result.setCode(1);
            return result;
        }
        result.setMsg("Now is inactivated");
        result.setCode(0);
        return result;
    }
}
