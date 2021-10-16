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
