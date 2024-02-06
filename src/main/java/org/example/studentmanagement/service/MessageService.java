package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.Message;
import org.example.studentmanagement.security.SpringUser;

import java.util.List;

public interface MessageService {

    Message save(Message message, SpringUser springUser);

    List<Message> findAllByToId(int id);
}
