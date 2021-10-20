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
        //find all public works(distinct creator)
        //List<Map<String, Object>> allPublicWorks = artifactRepo.findByDistinctUserId();

        //find all public works
        List<Map<String, Object>> allPublicWorks = artifactRepo.findByArtifactWeights();
        //returning data
        List<Map<String, Object>> modifyWorks = new ArrayList<>();
        Result result = new Result();

        for (int i = 0; i < allPublicWorks.size(); i++) {
            Map<String, Object> newMap = new HashMap<>(allPublicWorks.get(0));
            //find user by user_id
            User user = this.userRepo.getUserById((Integer) newMap.get("user_id"));
            //replace user_id with username, add user profile picture location
            //newMap.remove("user_id");
            newMap.put("username", user.getUsername());
            newMap.put("profile_pic_store", user.getProfilePicStore());
            //add new map record
            allPublicWorks.add(newMap);
            //remove original map record
            allPublicWorks.remove(allPublicWorks.get(0));
        }

        //remove all the general article
        for (int i = 0; i < allPublicWorks.size(); i++) {
            if (allPublicWorks.get(i).get("category_name").equals("General")) {
                allPublicWorks.remove(allPublicWorks.get(i));
            }
        }

        //remove duplicate creators
        List<Map<String, Object>> tempDuplicate = new ArrayList<>();
        for (Map<String, Object> publicWork : allPublicWorks) {
            String name = publicWork.get("username").toString();
            int count = 0;
            for (Map<String, Object> temp : allPublicWorks) {
                if (temp.get("username").equals(name)) {
                    count++;
                }
            }
            if (count < 2) {
                modifyWorks.add(publicWork);
            } else {
                tempDuplicate.add(publicWork);
            }
        }

        if (tempDuplicate.size() > 0) {
            //randomly choose one record
            Collections.shuffle(tempDuplicate);
            modifyWorks.add(tempDuplicate.get(0));
        }

        //return 6 objects to frontend
        if (modifyWorks.size() > 6) {
            for (int i = 6; i < modifyWorks.size(); i++) {
                modifyWorks.remove(i);
            }
        }

        result.setCode(0);
        result.setMsg("Query success!");
        //randomly shuffle information
        Collections.shuffle(modifyWorks);
        result.setData(modifyWorks);
        return result;

        //terrible code!

    }
}
