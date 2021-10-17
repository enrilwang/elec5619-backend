package net.guides.springboot2.springboot2webappjsp.repositories;

import net.guides.springboot2.springboot2webappjsp.domain.Artifact;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ArtifactRepository extends JpaRepository<Artifact, Integer> {

    //public List<Artifact> findByUser_id(Integer user_id);

    @Query(value = "select artifact_id, title, description, category_name, store_location from artifact where user_id = ?1", nativeQuery = true)
    public List<Map<String,Object>> findByUserId(Integer user_id);


    @Query(value = "select * from artifact where user_id = ?1",nativeQuery = true)
    public List<Artifact> findAllArtifact(Integer user_id);

    public Artifact findArtifactByArtifactId(Integer artifact_id);

}
