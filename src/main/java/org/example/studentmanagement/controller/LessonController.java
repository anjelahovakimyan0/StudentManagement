package org.example.studentmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.entity.Lesson;
import org.example.studentmanagement.entity.User;
import org.example.studentmanagement.entity.UserType;
import org.example.studentmanagement.repository.LessonRepository;
import org.example.studentmanagement.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LessonController {

    private final LessonRepository lessonRepository;

    private final UserRepository userRepository;

    @GetMapping("/lessons")
    public String lessonsPage(ModelMap modelMap) {
        modelMap.addAttribute("lessons", lessonRepository.findAll());
        return "lessons";
    }

    @GetMapping("/lessons/add")
    public String addLessonPage(ModelMap modelMap) {
        List<User> teachers = userRepository.findAllByUserType(UserType.TEACHER);
        modelMap.addAttribute("teachers", teachers);
        return "addLesson";
    }

    @PostMapping("/lessons/add")
    public String addLesson(@ModelAttribute Lesson lesson) {
        lessonRepository.save(lesson);
        return "redirect:/lessons";
    }

    @GetMapping("/lessons/update/{id}")
    public String updateLessonPage(ModelMap modelMap, @PathVariable("id") int id) {
        modelMap.addAttribute("lesson", lessonRepository.findById(id).get());
        List<User> teachers = userRepository.findAllByUserType(UserType.TEACHER);
        modelMap.addAttribute("teachers", teachers);
        return "updateLesson";
    }

    @PostMapping("/lessons/update")
    public String updateLesson(@ModelAttribute Lesson lesson) {
        if (lesson.getStartDate() == null) {
            Optional<Lesson> byId = lessonRepository.findById(lesson.getId());
            lesson.setStartDate(byId.get().getStartDate());
        }
        lessonRepository.save(lesson);
        return "redirect:/lessons";
    }

    @GetMapping("/lessons/delete/{id}")
    public String deleteLesson(@PathVariable("id") int id) {
        Optional<Lesson> byId = lessonRepository.findById(id);
        if (byId.isPresent()) {
            lessonRepository.deleteById(id);
        }
        return "redirect:/lessons";
    }
}
