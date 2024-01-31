package org.example.studentmanagement.controller;

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
public class TeacherController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    @GetMapping("/teachers")
    public String teachersPage(ModelMap modelMap) {
        List<User> teachers = userRepository.findAllByUserType(UserType.TEACHER);
        modelMap.addAttribute("teachers", teachers);
        return "teachers";
    }

    @GetMapping("/teachers/add")
    public String addTeacherPage(ModelMap modelMap) {
        modelMap.addAttribute("lessons", lessonRepository.findAll());
        return "addTeacher";
    }

    @PostMapping("/teachers/add")
    public String addTeacher(@ModelAttribute User user,
                             @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty() && !multipartFile.getOriginalFilename().equals("")) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);
            user.setPicName(picName);
        }
        user.setUserType(UserType.TEACHER);
        userRepository.save(user);
        return "redirect:/teachers";
    }

    @GetMapping("/teachers/update/{id}")
    public String updateTeacherPage(ModelMap modelMap,
                                    @PathVariable("id") int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            modelMap.addAttribute("teacher", byId.get());
            modelMap.addAttribute("lessons", lessonRepository.findAll());
            return "updateTeacher";
        }
        return "redirect:/teachers";
    }

    @PostMapping("/teachers/update")
    public String updateTeacher(@ModelAttribute User user,
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
        return "redirect:/teachers";
    }

    @GetMapping("/teachers/delete/{id}")
    public String deleteLesson(@PathVariable("id") int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            userRepository.deleteById(id);
        }
        return "redirect:/teachers";
    }

    @GetMapping("/teachers/image/delete/{id}")
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
        return "redirect:/teachers/update/" + id;
    }
}
