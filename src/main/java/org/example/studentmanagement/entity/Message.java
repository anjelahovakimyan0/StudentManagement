package org.example.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "message")
    private String messageText;

    @ManyToOne
    private User from;

    @ManyToOne
    private User to;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date dateTime;
}
