package net.guides.springboot2.springboot2webappjsp.controllers;

import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserProfileController {
    @Autowired
    UserRepository userRepo;

    //change password
    @RequestMapping(value = "api/changeNameAndPassword", method = RequestMethod.POST)
    @CrossOrigin
    public Result changeNameAndPassword(@RequestParam String email,  @RequestParam String newPassword, @RequestParam String name) {
        Result result = new Result();
        User existUser = userRepo.getUserByEmail(email);

        if (existUser == null) {
            result.setMsg("wrong email address!");
            result.setCode(400);
            return result;
        }
        else {
            existUser.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
            existUser.setName(name);
            userRepo.save(existUser);
            result.setMsg("change password successfully");
            result.setCode(201);
            return result;
        }


    }


//    //add profile picture
//    @RequestMapping(value = "api/addProfilePicture", method = RequestMethod.POST)
//    @CrossOrigin
//    public Result addProfilePicture(@RequestParam String email,  @RequestParam String newPassword, @RequestParam String name) {
//        Result result = new Result();
//        User existUser = userRepo.getUserByEmail(email);
//
//        if (existUser == null) {
//            result.setMsg("wrong email address!");
//            result.setCode(400);
//            return result;
//        }
//        else {
//            existUser.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
//            existUser.setName(name);
//            userRepo.save(existUser);
//            result.setMsg("change password successfully");
//            result.setCode(201);
//            return result;
//        }
//
//
//    }

    //getAllFavouriteList
    @RequestMapping(value = "api/getFavouriteList", method = RequestMethod.POST)
    @CrossOrigin
    public Result getFavouriteList(@RequestParam String email) {
        Result result = new Result();
        User existUser = userRepo.getUserByEmail(email);
        List<User> favouriteList = new ArrayList<>();
        List<User> users = userRepo.findAll();
        String now = existUser.getUser_favorite_user_id();
        if (now != null) {
            String[] str = now.split(",");

            List<Integer> ids = new ArrayList<>();
            for (String s : str) ids.add(Integer.valueOf(s));

            for (int i = 0; i < ids.size(); i++) {
                for (int j = 0; j < users.size(); j++) {
                    if (ids.get(i) == users.get(j).getUser_id()) {
                        favouriteList.add(users.get(j));
                        break;
                    }
                }
            }
        }

        result.setMsg("OK");
        result.setCode(200);
        result.setData(favouriteList);
        return result;



    }



    //getAllSubscribeList
    @RequestMapping(value = "api/getSubscribeList", method = RequestMethod.POST)
    @CrossOrigin
    public Result getSubscribeList(@RequestParam String email) {
        Result result = new Result();
        User existUser = userRepo.getUserByEmail(email);

        if (existUser == null) {
            result.setMsg("wrong email address!");
            result.setCode(400);
            return result;
        }
        else {
            List<User> subscribeList = new ArrayList<>();
            List<User> users = userRepo.findAll();


            String now = existUser.getUser_subscribe_user_id();
            if (now != null) {
                String[] str = now.split(",");
                List<Integer> ids = new ArrayList<>();
                for (String s : str) ids.add(Integer.valueOf(s));
                for (int i = 0; i < ids.size(); i++) {
                    for (int j = 0; j < users.size(); j++) {
                        if (ids.get(i) == users.get(j).getUser_id()) {
                            subscribeList.add(users.get(j));
                            break;
                        }
                    }
                }
            }



            result.setCode(200);
            result.setData(subscribeList);
            return result;
        }


    }










}
