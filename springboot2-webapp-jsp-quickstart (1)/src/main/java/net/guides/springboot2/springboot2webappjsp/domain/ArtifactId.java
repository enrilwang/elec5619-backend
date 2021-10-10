package net.guides.springboot2.springboot2webappjsp.domain;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ArtifactId implements Serializable {
    private static final long serialVersionUID = -1842238923782438962L;
    @Column(name = "artifact_id", nullable = false)
    private Integer artifactId;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(Integer artifactId) {
        this.artifactId = artifactId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(artifactId, userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ArtifactId entity = (ArtifactId) o;
        return Objects.equals(this.artifactId, entity.artifactId) &&
                Objects.equals(this.userId, entity.userId);
    }
}