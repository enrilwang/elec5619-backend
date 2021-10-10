package net.guides.springboot2.springboot2webappjsp.domain;

import javax.persistence.*;

@Table(name = "artifact")
@Entity
public class Artifact {
    @EmbeddedId
    private ArtifactId id;

    @Column(name = "store_location", nullable = false)
    private String storeLocation;

    @Column(name = "description")
    private String description;

    @Column(name = "artifact_weights")
    private Integer artifactWeights;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_name", nullable = false)
    private Category categoryName;

    public Artifact() {

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

    public ArtifactId getId() {
        return id;
    }

    public void setId(ArtifactId id) {
        this.id = id;
    }
}