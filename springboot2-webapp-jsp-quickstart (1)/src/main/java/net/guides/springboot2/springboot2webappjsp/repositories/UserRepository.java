package net.guides.springboot2.springboot2webappjsp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.guides.springboot2.springboot2webappjsp.domain.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface UserRepository extends JpaRepository<User, Integer>
{
    User getUserByEmail(String email);
    User getUserById(int id);

    @Query(value = "select * from user  where username like CONCAT('%',:name,'%')",nativeQuery = true)
    public List<User> findByUserName(String name);



}
