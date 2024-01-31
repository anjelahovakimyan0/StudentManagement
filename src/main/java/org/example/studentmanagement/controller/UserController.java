package org.example.studentmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.entity.User;
import org.example.studentmanagement.entity.UserType;
import org.example.studentmanagement.repository.LessonRepository;
import org.example.studentmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {


    private final UserRepository userRepository;

    private final LessonRepository lessonRepository;

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

    @GetMapping("/users/add")
    public String addUserPage(ModelMap modelMap) {
        modelMap.addAttribute("users", userRepository.findAll());
        return "addUser";
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute User user,
                          @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty() && !multipartFile.getOriginalFilename().equals("")) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);
            user.setPicName(picName);
        }
        if (user.getUserType() == UserType.TEACHER) {
            user.setUserType(UserType.TEACHER);
            userRepository.save(user);
            return "redirect:/teachers";
        }
        user.setUserType(UserType.STUDENT);
        userRepository.save(user);
        return "redirect:/students";
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
        if (user.getUserType() == UserType.TEACHER) {
            return "redirect:/teachers";
        }
        return "redirect:/students";
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
        if (user.getUserType() == UserType.TEACHER) {
            return "redirect:/teachers";
        }
        return "redirect:/students";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            userRepository.deleteById(id);
        }
        if (byId.get().getUserType() == UserType.TEACHER) {
            return "redirect:/teachers";
        }
        return "redirect:/students";
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
