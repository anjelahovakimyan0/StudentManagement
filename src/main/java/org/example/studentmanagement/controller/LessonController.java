package org.example.studentmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.entity.Lesson;
import org.example.studentmanagement.entity.User;
import org.example.studentmanagement.entity.UserType;
import org.example.studentmanagement.repository.UserRepository;
import org.example.studentmanagement.security.SpringUser;
import org.example.studentmanagement.service.LessonService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    private final UserRepository userRepository;

    @GetMapping("")
    public String lessonsPage(ModelMap modelMap) {
        modelMap.addAttribute("lessons", lessonService.findAll());
        return "lessons";
    }

    @GetMapping("/add")
    public String addLessonPage(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        if (springUser.getUser().getUserType() == UserType.TEACHER) {
            List<User> teachers = userRepository.findAllByUserType(UserType.TEACHER);
            modelMap.addAttribute("teachers", teachers);
            return "addLesson";
        }
        return "redirect:/home";
    }

    @PostMapping("/add")
    public String addLesson(@ModelAttribute Lesson lesson,
                            @AuthenticationPrincipal SpringUser springUser) {
        lessonService.save(lesson, springUser);
        return "redirect:/lessons";
    }

    @GetMapping("/update/{id}")
    public String updateLessonPage(ModelMap modelMap, @PathVariable("id") int id) {
        modelMap.addAttribute("lesson", lessonService.findById(id).get());
        List<User> teachers = userRepository.findAllByUserType(UserType.TEACHER);
        modelMap.addAttribute("teachers", teachers);
        return "updateLesson";
    }

    @PostMapping("/update")
    public String updateLesson(@ModelAttribute Lesson lesson) {
        lessonService.update(lesson);
        return "redirect:/lessons";
    }

    @GetMapping("/delete/{id}")
    public String deleteLesson(@PathVariable("id") int id) {
        lessonService.deleteById(id);
        return "redirect:/lessons";
    }

    @GetMapping("/singleLesson/{id}")
    public String singleLesson(ModelMap modelMap,
                               @PathVariable("id") int id) {
        modelMap.addAttribute("lesson", lessonService.findById(id).get());
        return "singleLesson";
    }
}
