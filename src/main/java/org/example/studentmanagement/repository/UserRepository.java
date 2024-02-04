package org.example.studentmanagement.repository;

import org.example.studentmanagement.entity.User;
import org.example.studentmanagement.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByUserType(UserType userType);

    Optional<User> findByEmail(String email);
}
