package net.guides.springboot2.springboot2webappjsp.controllers;

import net.guides.springboot2.springboot2webappjsp.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.guides.springboot2.springboot2webappjsp.repositories.CategoryRepository;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepo;

    public CategoryController() {

        //default category
        Category photo = new Category();
        photo.setId("Photo");
        photo.setUpdateTime(Instant.now());
        photo.setCategoryDescription("Record your life moments and reveal the beauty of nature!");
        categoryRepo.save(photo);

        Category music = new Category();
        music.setId("Music");
        music.setUpdateTime(Instant.now());
        music.setCategoryDescription("Record the moments of your inspiration with music!");
        categoryRepo.save(music);

        Category art = new Category();
        art.setId("Art");
        art.setUpdateTime(Instant.now());
        art.setCategoryDescription("Record the nature and life with art!");
        categoryRepo.save(art);


    }

    //method for listing existing category
    //default method
    @GetMapping
    public @ResponseBody List<String> getAll() {
        return this.categoryRepo.findAllId();
    }

    //test method for find
    @GetMapping("/{id}")
    public Result find(@PathVariable(value = "id") String id) {
        Result result = new Result();
        result.setData(this.categoryRepo.findById(id));
        return result;
    }

}
