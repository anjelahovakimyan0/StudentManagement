package org.example.studentmanagement.repository;

import org.example.studentmanagement.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    Optional<Lesson> findLessonByUser_Id(int id);
}
