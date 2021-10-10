package net.guides.springboot2.springboot2webappjsp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "category", schema = "elec5619")
public class Category {
    @Id
    @Column(name = "category_name", nullable = false, length = 45)
    private String id;

    @Column(name = "update_time", nullable = false)
    private Instant updateTime;

    @Column(name = "category_description", nullable = false)
    private String categoryDescription;

    public Category (){

    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}