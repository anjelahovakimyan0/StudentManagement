package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.User;
import org.example.studentmanagement.entity.UserType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAllByUserType(UserType userType);

    Optional<User> findByEmail(String email);

    void save(User user, MultipartFile multipartFile) throws IOException;

    Optional<User> findById(int id);

    void update(User user, MultipartFile multipartFile) throws IOException;

    void deleteById(int id);

    void deleteImage(int id);
}
