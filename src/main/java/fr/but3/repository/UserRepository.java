package fr.but3.repository;

import fr.but3.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByNameAndPwd(String name, String pwd);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}