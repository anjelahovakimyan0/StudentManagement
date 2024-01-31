package org.example.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String duration;

    private double price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User user;
}
