package net.guides.springboot2.springboot2webappjsp.controllers;

import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.ArtifactRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.CategoryRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("frontpage")
public class FrontPageController {

    @Autowired
    ArtifactRepository artifactRepo;
    @Autowired
    CategoryRepository categoryRepo;
    @Autowired
    UserRepository userRepo;

    @GetMapping
    public Result defaultPage() {
        //find all public works
        List<Map<String, Object>> allPublicWorks = artifactRepo.findByDistinctUserId();
        Result result = new Result();

        for (int i = 0; i < allPublicWorks.size(); i++) {
            Map<String, Object> newMap = new HashMap<>(allPublicWorks.get(0));
            //find user by user_id
            User user = this.userRepo.getUserById((Integer) newMap.get("user_id"));
            //replace user_id with username
            newMap.replace("user_id", user.getUsername());
            //add new map record
            allPublicWorks.add(newMap);
            //remove original map record
            allPublicWorks.remove(allPublicWorks.get(0));
        }

        //Remove all the general article
        for (int i = 0; i < allPublicWorks.size(); i++) {
            if (allPublicWorks.get(i).get("category_name").equals("General")) {
                allPublicWorks.remove(allPublicWorks.get(i));
            }
        }

        //Return 6 objects to frontend
        if (allPublicWorks.size() > 6) {
            for (int i = 6; i < allPublicWorks.size(); i++) {
                allPublicWorks.remove(i);
            }
        }

        result.setCode(0);
        result.setMsg("Query success!");
        //randomly shuffle information
        Collections.shuffle(allPublicWorks);
        result.setData(allPublicWorks);
        return result;
    }

}
