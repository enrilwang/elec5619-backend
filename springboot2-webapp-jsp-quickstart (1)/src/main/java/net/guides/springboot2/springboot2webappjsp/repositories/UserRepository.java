package net.guides.springboot2.springboot2webappjsp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.guides.springboot2.springboot2webappjsp.domain.User;

public interface UserRepository extends JpaRepository<User, Integer>
{
    User getUserByEmail(String email);
    User getUserById(int id);
//    Boolean isUserExist(String email);
}
