package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.Lesson;
import org.example.studentmanagement.security.SpringUser;

import java.util.List;
import java.util.Optional;

public interface LessonService {

    List<Lesson> findAll();

    void save(Lesson lesson, SpringUser springUser);

    Optional<Lesson> findById(int id);

    void update(Lesson lesson);

    void deleteById(int id);
}
