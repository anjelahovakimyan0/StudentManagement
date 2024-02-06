package org.example.studentmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.entity.Lesson;
import org.example.studentmanagement.repository.LessonRepository;
import org.example.studentmanagement.security.SpringUser;
import org.example.studentmanagement.service.LessonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    @Override
    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Override
    public void save(Lesson lesson, SpringUser springUser) {
        lesson.setUser(springUser.getUser());
        lessonRepository.save(lesson);
    }

    @Override
    public Optional<Lesson> findById(int id) {
        return lessonRepository.findById(id);
    }

    @Override
    public void update(Lesson lesson) {
        if (lesson.getStartDate() == null) {
            Optional<Lesson> byId = findById(lesson.getId());
            lesson.setStartDate(byId.get().getStartDate());
        }
        lessonRepository.save(lesson);
    }

    @Override
    public void deleteById(int id) {
        Optional<Lesson> byId = findById(id);
        if (byId.isPresent()) {
            lessonRepository.deleteById(id);
        }
    }
}
