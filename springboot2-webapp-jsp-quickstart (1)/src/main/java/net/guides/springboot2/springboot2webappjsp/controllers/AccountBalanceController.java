package net.guides.springboot2.springboot2webappjsp.controllers;

import net.guides.springboot2.springboot2webappjsp.configuration.JwtUtil;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AccountBalanceController {
    @Autowired
    UserRepository userRepo;

    final String path = "accountBalance";

    //update
    @PostMapping(path)
    public Result getAccountBalance(HttpServletRequest request, @RequestParam float topUp) {
        Result result = new Result();
        //check>0
        if (topUp <= 0) {
            result.setMsg("You shoud top up a positive amount of money.");
            result.setCode(1);
            return result;
        }
        String email = JwtUtil.getUserEmailByToken(request);
        User userQueryResult = userRepo.getUserByEmail(email);
        //check overflow
        if (Integer.MAX_VALUE - topUp < userQueryResult.getAccountBalance()) {//final balance > max_int
            userQueryResult.setAccountBalance(Integer.MAX_VALUE);
            result.setMsg("Add too much money. Balance is set as the max value.");
        } else {
            userQueryResult.setAccountBalance(userQueryResult.getAccountBalance() + topUp);
            result.setMsg("Success");
        }
        User updateResult = userRepo.save(userQueryResult);
        if (updateResult == null) {
            result.setMsg("Error updating database.");
            result.setCode(1);
            return result;
        }
        result.setCode(0);
        return result;
    }

    //clear balance
    @DeleteMapping(path)
    public Result deleteAccountBalance(HttpServletRequest request) {
        Result result = new Result();
        String email = JwtUtil.getUserEmailByToken(request);
        User userQueryResult = userRepo.getUserByEmail(email);
        userQueryResult.setAccountBalance(0);
        User updateResult = userRepo.save(userQueryResult);
        if (updateResult == null) {
            result.setMsg("Error updating database.");
            result.setCode(1);
            return result;
        }
        result.setMsg("Balance is cleared.");
        result.setCode(0);
        return result;
    }
}
