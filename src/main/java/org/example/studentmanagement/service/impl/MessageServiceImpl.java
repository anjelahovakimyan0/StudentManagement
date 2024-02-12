package org.example.studentmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.entity.Message;
import org.example.studentmanagement.entity.User;
import org.example.studentmanagement.entity.UserType;
import org.example.studentmanagement.repository.MessageRepository;
import org.example.studentmanagement.security.SpringUser;
import org.example.studentmanagement.service.MessageService;
import org.example.studentmanagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    private final UserService userService;

    @Override
    public Message save(Message message, SpringUser springUser) {
        User fromUser = springUser.getUser();
        Optional<User> toUserOpt = userService.findById(message.getTo().getId());
        if (toUserOpt.isPresent()) {
            User toUser = toUserOpt.get();
            if (toUser.getUserType() == UserType.STUDENT &&
                    fromUser.getLesson().equals(toUser.getLesson())) {
                message.setFrom(fromUser);
                message.setTo(toUser);
                message.setDateTime(new Date());
                return messageRepository.save(message);
            }
        }
        return null;
    }

    @Override
    public List<Message> findAllByFromId(int id) {
        return messageRepository.findAllByFrom_Id(id);
    }
}
