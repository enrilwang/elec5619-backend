package net.guides.springboot2.springboot2webappjsp.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Table(name = "artifact")
@Entity
public class Artifact {

    @Id
    @Column(name = "artifact_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer artifactId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = true)
    //@NotNull(message = "User_id can't be empty!")
    private User user;

    @Column(name = "store_location")
    //@NotBlank(message = "Please define store location!")
    private String storeLocation;

    @Column(name = "title", nullable = false)
    @NotBlank(message = "Title can't be empty!")
    private String title;

    @Column(name = "description")
    private String description;

    //artifact weights represent visibility
    //0 for public, 1 for subscribe user
    @Column(name = "artifact_weights")
    private Integer artifactWeights;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_name", nullable = false)
    private Category categoryName;


    public Artifact() {

    }

    public Artifact(String title, String description, Category categoryName) {
        this.title = title;
        this.description = description;
        this.categoryName = categoryName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(Integer artifactId) {
        this.artifactId = artifactId;
    }

    public Category getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(Category categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getArtifactWeights() {
        return artifactWeights;
    }

    public void setArtifactWeights(Integer artifactWeights) {
        this.artifactWeights = artifactWeights;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

}
