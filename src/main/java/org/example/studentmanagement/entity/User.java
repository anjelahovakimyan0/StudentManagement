package org.example.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String surname;

    private String email;

    private String password;

    private String picName;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @ManyToOne
    private Lesson lesson;
}
