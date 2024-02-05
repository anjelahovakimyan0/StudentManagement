package org.example.studentmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.entity.User;
import org.example.studentmanagement.entity.UserType;
import org.example.studentmanagement.repository.LessonRepository;
import org.example.studentmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.example.studentmanagement.util.TypeResolver.resolveType;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    private final LessonRepository lessonRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    @GetMapping("/students")
    public String studentsPage(ModelMap modelMap) {
        List<User> users = userRepository.findAllByUserType(UserType.STUDENT);
        modelMap.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/teachers")
    public String teachersPage(ModelMap modelMap) {
        List<User> users = userRepository.findAllByUserType(UserType.TEACHER);
        modelMap.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/users/register")
    public String registerUserPage(@RequestParam(value = "msg", required = false) String msg,
                                   ModelMap modelMap) {
        if (msg != null && !msg.isEmpty()) {
            modelMap.addAttribute("msg", msg);
        }
        modelMap.addAttribute("lessons", lessonRepository.findAll());
        return "register";
    }

    @PostMapping("/users/register")
    public String registerUser(@ModelAttribute User user,
                               @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail.isPresent()) {
            return "redirect:/users/register?msg=User already exists!";
        }
        if (multipartFile != null && !multipartFile.isEmpty() && !multipartFile.getOriginalFilename().equals("")) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);
            user.setPicName(picName);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/users/register?msg=User Registered!";
    }

    @GetMapping("/users/update/{id}")
    public String updateUserPage(ModelMap modelMap,
                                 @PathVariable("id") int id) {
        Optional<User> byId = userRepository.findById(id);
        User user = byId.get();
        if (byId.isPresent()) {
            modelMap.addAttribute("user", user);
            modelMap.addAttribute("lessons", lessonRepository.findAll());
            return "updateUser";
        }
        userRepository.save(user);
        return resolveType(user);
    }

    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty() && !multipartFile.getOriginalFilename().equals("")) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);
            user.setPicName(picName);
        } else {
            Optional<User> byId = userRepository.findById(user.getId());
            user.setPicName(byId.get().getPicName());
        }
        userRepository.save(user);
        return resolveType(user);
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            userRepository.deleteById(id);
        }
        return resolveType(byId.get());
    }

    @GetMapping("/users/image/delete/{id}")
    public String deleteImage(@PathVariable("id") int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            String picName = user.getPicName();
            if (picName != null) {
                user.setPicName(null);
                userRepository.save(user);
                File file = new File(uploadDirectory, picName);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        return "redirect:/users/update/" + id;
    }
}
