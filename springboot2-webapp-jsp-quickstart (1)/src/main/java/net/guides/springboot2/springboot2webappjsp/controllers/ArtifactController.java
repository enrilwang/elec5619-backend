package net.guides.springboot2.springboot2webappjsp.controllers;

import net.guides.springboot2.springboot2webappjsp.configuration.JwtUtil;
import net.guides.springboot2.springboot2webappjsp.domain.Artifact;
import net.guides.springboot2.springboot2webappjsp.domain.Category;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.ArtifactRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.CategoryRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("works")
public class ArtifactController {

    //1 for fail, 0 for success
    @Autowired
    private ArtifactRepository artifactRepo;
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;

    public ArtifactController(CategoryRepository categoryRepo, UserRepository repository) {
        this.categoryRepo = categoryRepo;
        this.userRepo = repository;
    }

    //method for finding all works
    //default method
    @GetMapping("/find-all")
    @CrossOrigin
    public List<Artifact> getAllWorks() {
        return this.artifactRepo.findAll();
    }

    //method for finding one creator's works
    @GetMapping
    @CrossOrigin
    public Result getWorkById(HttpServletRequest request) {
        //fill in token
        String email = JwtUtil.getUserEmailByToken(request);
        User user;
        try {
            user = userRepo.getUserByEmail(email);
        } catch (NullPointerException | EmptyResultDataAccessException | NoSuchElementException exception) {
            return new Result(1,"No such creator!");
        }

        //Query creator's work
        try {
            List<Map<String,Object>> artifacts = this.artifactRepo.findByUserId(user.getId());
            if (artifacts.size() != 0) {
                Result result = new Result(0, "Query success!");
                result.setData(artifacts);
                return result;
            } else {
                return new Result(1, "No work exist!");
            }
        } catch (NullPointerException | EmptyResultDataAccessException | NoSuchElementException exception){
            return new Result(1, "Request fail!");
        }
    }

    //method for delete work
    @DeleteMapping("/del/{work_id}")
    @CrossOrigin
    public Result deleteArtifact(@PathVariable("work_id") Integer work_id) {
        try {
            artifactRepo.deleteById(work_id);
            Artifact artifact = artifactRepo.findArtifactByArtifactId(work_id);
            if (artifact == null) {
                return new Result(0, "Delete success!");
            } else {
                return new Result(1, "Delete error!");
            }
        } catch (NullPointerException | EmptyResultDataAccessException | NoSuchElementException exception) {
            return new Result(1, "Request work not exist");
        }
    }

    //method for upload new artifact
    @PostMapping("/add")
    @CrossOrigin
    public Result addArtifact(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile file,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String category_name) {

        //fill in token
        String email = JwtUtil.getUserEmailByToken(request);
        User user;
        try {
            user = userRepo.getUserByEmail(email);
        } catch (NullPointerException | EmptyResultDataAccessException | NoSuchElementException exception) {
            return new Result(1,"No such creator!");
        }

        if (title.length() == 0) {
            return new Result(1, "Title can't be null");
        } else if (category_name.length() == 0) {
            return new Result(1, "Category can't be null");
        } else if (file.getSize() == 0) {
            return new Result(1, "File can't be empty");
        } else {
            //Saving object
            Artifact artifact = new Artifact();
            try {
                Optional<Category> categories = categoryRepo.findById(category_name);
                artifact = new Artifact(title,description,categories.get());
                artifact.setUser(user);

            } catch (NullPointerException | EmptyResultDataAccessException | NoSuchElementException exception) {
                return new Result(1, "Request category not exist!");
            }

            //method for file upload
            FileUploader fileUploader = new FileUploader();
            String path = fileUploader.fileUpload(file,"C:/Users/ASUS/Desktop/elec5619-backend/springboot2-webapp-jsp-quickstart (1)/src/main/resources/resources/post");

            String[] gg = path.split("\\\\");
            String last = gg[gg.length-1];

            String newPath = "/api/post/" +  last.substring(0,last.length()/2);

            if (path.equals("fail")) {
                return new Result(1, "File upload fail!");
            } else {
                artifact.setStoreLocation(newPath);
            }

            //default artifact weight is 0
            artifact.setArtifactWeights(0);

            try {
                this.artifactRepo.save(artifact);
                return new Result(0, "Post success!");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(1, "Saving fail!");
            }
        }

    }

    //method for edit work
    @PutMapping("/edit/{work_id}")
    public Result updateArtifact(
            @PathVariable(value = "work_id") Integer work_id,
            @RequestParam String title,
            @RequestParam(required = false) String description) {
        try {
            Artifact artifact = this.artifactRepo.findArtifactByArtifactId(work_id);
            if (artifact == null) {
                return new Result(1,"Request artifact not exist!");
            } else {
                //saving data
                artifact.setTitle(title);
                artifact.setDescription(description);
                this.artifactRepo.save(artifact);

                //validate
                Artifact temp = this.artifactRepo.findArtifactByArtifactId(work_id);
                if (temp.getTitle().equals(title) && temp.getDescription().equals(description)) {
                    return new Result(0, "Update success!");
                } else {
                    return new Result(1, "Update fail!");
                }

            }
        } catch (NullPointerException | EmptyResultDataAccessException | NoSuchElementException exception) {
            return new Result(1, "Request artifact not exist!");
        }
    }

}
