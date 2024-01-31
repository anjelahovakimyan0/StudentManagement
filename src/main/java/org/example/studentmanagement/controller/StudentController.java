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
public class StudentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    @GetMapping("/students")
    public String studentsPage(ModelMap modelMap) {
        List<User> students = userRepository.findAllByUserType(UserType.STUDENT);
        modelMap.addAttribute("students", students);
        return "students";
    }

    @GetMapping("/students/add")
    public String addStudentPage(ModelMap modelMap) {
        modelMap.addAttribute("lessons", lessonRepository.findAll());
        return "addStudent";
    }

    @PostMapping("/students/add")
    public String addStudent(@ModelAttribute User user,
                             @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty() && !multipartFile.getOriginalFilename().equals("")) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);
            user.setPicName(picName);
        }
        user.setUserType(UserType.STUDENT);
        userRepository.save(user);
        return "redirect:/students";
    }

    @GetMapping("/students/update/{id}")
    public String updateStudentPage(ModelMap modelMap,
                                    @PathVariable("id") int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            modelMap.addAttribute("student", byId.get());
            modelMap.addAttribute("lessons", lessonRepository.findAll());
            return "updateStudent";
        }
        return "redirect:/students";
    }

    @PostMapping("/students/update")
    public String updateStudent(@ModelAttribute User user,
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
        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteLesson(@PathVariable("id") int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            userRepository.deleteById(id);
        }
        return "redirect:/students";
    }

    @GetMapping("/students/image/delete/{id}")
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
        return "redirect:/students/update/" + id;
    }
}
