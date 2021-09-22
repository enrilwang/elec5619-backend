package net.guides.springboot2.springboot2webappjsp.controllers;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;

@RestController
public class LoginController {
    @Autowired
    UserRepository userRepo;


    @RequestMapping(value = "/loginPage", method = RequestMethod.POST)
    public String login(@RequestBody User user) {
        if (user.getName().equals("enril") ) return "correct";
        else return "incorrect ";
    }
}
