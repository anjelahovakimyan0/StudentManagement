package org.example.studentmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.entity.User;
import org.example.studentmanagement.entity.UserType;
import org.example.studentmanagement.service.LessonService;
import org.example.studentmanagement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.example.studentmanagement.util.TypeResolver.resolveType;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final LessonService lessonService;

    @GetMapping("/students")
    public String studentsPage(ModelMap modelMap,
                               @RequestParam(value = "msg", required = false) String msg) {
        if (msg != null && !msg.isEmpty()) {
            modelMap.addAttribute("msg", msg);
        }
        List<User> users = userService.findAllByUserType(UserType.STUDENT);
        modelMap.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/teachers")
    public String teachersPage(ModelMap modelMap) {
        List<User> users = userService.findAllByUserType(UserType.TEACHER);
        modelMap.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/register")
    public String registerUserPage(@RequestParam(value = "msg", required = false) String msg,
                                   ModelMap modelMap) {
        if (msg != null && !msg.isEmpty()) {
            modelMap.addAttribute("msg", msg);
        }
        modelMap.addAttribute("lessons", lessonService.findAll());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        Optional<User> byEmail = userService.findByEmail(user.getEmail());
        if (byEmail.isPresent()) {
            return "redirect:/users/register?msg=User already exists!";
        }
        userService.save(user, multipartFile);
        return "redirect:/users/register?msg=User Registered!";
    }

    @GetMapping("/update/{id}")
    public String updateUserPage(ModelMap modelMap,
                                 @PathVariable("id") int id) {
        Optional<User> byId = userService.findById(id);
        User user = byId.get();
        if (byId.isPresent()) {
            modelMap.addAttribute("user", user);
            modelMap.addAttribute("lessons", lessonService.findAll());
            return "updateUser";
        }
        return resolveType(user);
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        userService.update(user, multipartFile);
        return resolveType(user);
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        Optional<User> byId = userService.findById(id);
        if (byId.isPresent()) {
            userService.deleteById(id);
        }
        return resolveType(byId.get());
    }

    @GetMapping("/image/delete/{id}")
    public String deleteImage(@PathVariable("id") int id) {
        userService.deleteImage(id);
        return "redirect:/users/update/" + id;
    }
}
