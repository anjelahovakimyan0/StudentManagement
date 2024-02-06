package org.example.studentmanagement.repository;

import org.example.studentmanagement.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findAllByTo_Id(int id);
}
