package net.guides.springboot2.springboot2webappjsp.controllers;

import net.guides.springboot2.springboot2webappjsp.configuration.JwtUtil;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@RestController
public class FavoriteController {
    private static final String path = "favorite";

    @Autowired
    UserRepository userRepo;

    @PostMapping(path)
    public Result addFavourite(HttpServletRequest request, @RequestParam int favouriteUserId) {
        Result result = new Result();
        User favouriteUserQueryResult = userRepo.getUserById(favouriteUserId);
        if (favouriteUserQueryResult == null) {
            result.setCode(1);
            result.setMsg("No such favorite user");
            return result;
        }
        String email = JwtUtil.getUserEmailByToken(request);
        User userQueryResult = userRepo.getUserByEmail(email);
        if (userQueryResult == null) {
            result.setCode(1);
            result.setMsg("No such user");
            return result;
        }
        
        Set<Integer> favoriteIds = IntArrayStringToIntArray.intArrayStringToIntArray(userQueryResult.getFavoriteId());
        favoriteIds.add(favouriteUserId);
        userQueryResult.setFavoriteId(favoriteIds.toString());
        User updateResult = userRepo.save(userQueryResult);
        if (updateResult == null) {
            result.setMsg("Update failed");
            result.setCode(1);
            return result;
        }
        result.setCode(0);
        result.setMsg("Success");
        return result;


    }


}
