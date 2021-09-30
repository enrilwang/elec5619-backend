package net.guides.springboot2.springboot2webappjsp.controllers;

import net.guides.springboot2.springboot2webappjsp.domain.Favourite;
import net.guides.springboot2.springboot2webappjsp.repositories.FavouriteRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomepageController {
    @Autowired
    FavouriteRepository favourite;

    @GetMapping("/")
    public List<Favourite> getFavourite() {
        return favourite.findAll();
    }
}
