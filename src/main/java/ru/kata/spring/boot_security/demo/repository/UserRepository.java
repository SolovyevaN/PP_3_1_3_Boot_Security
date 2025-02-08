package ru.kata.spring.boot_security.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   @Transactional(readOnly = true)
   @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.name = :name")
   Optional<User> findByName(@Param("name") String name);

   @Query("SELECT u FROM User u WHERE u.password = :password")
   Optional<User> findByPassword(@Param("password") String password);
}
