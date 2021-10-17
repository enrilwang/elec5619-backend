package net.guides.springboot2.springboot2webappjsp.repositories;

import net.guides.springboot2.springboot2webappjsp.domain.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscribeRepository extends JpaRepository<Subscribe,Integer>{
    Subscribe getSubscribeBySubscribeId(int subscribeId);
    List<Subscribe> getSubscribeByUserId(int userId);
    List<Subscribe> getSubscribeByUserIdAndCreatorIdAndActivated(int userId, int creatorId, boolean activated);

    List<Subscribe> getSubscribeByCreatorId(int creatorId);
}

