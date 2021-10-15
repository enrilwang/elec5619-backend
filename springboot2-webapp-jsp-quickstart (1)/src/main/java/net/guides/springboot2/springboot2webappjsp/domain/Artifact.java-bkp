package net.guides.springboot2.springboot2webappjsp.domain;

import javax.persistence.*;

@Table(name = "artifact")
@Entity
public class Artifact {

    @Id
    @Column(name = "artifact_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer artifactId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user_id;

    @Column(name = "store_location", nullable = false)
    private String storeLocation;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "artifact_weights")
    private Integer artifactWeights;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_name", nullable = false)
    private Category categoryName;

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
