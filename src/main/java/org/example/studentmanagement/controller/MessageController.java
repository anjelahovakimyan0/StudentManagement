package org.example.studentmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.entity.Message;
import org.example.studentmanagement.security.SpringUser;
import org.example.studentmanagement.service.MessageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{id}")
    public String messagesPage(ModelMap modelMap,
                               @PathVariable("id") int id,
                               @AuthenticationPrincipal SpringUser springUser) {
        if (springUser.getUser().getId() == id) {
            modelMap.addAttribute("messages", messageService.findAllByToId(id));
            return "messages";
        }
        return "users";
    }

    @GetMapping("/send/{id}")
    public String sendMessagePage(ModelMap modelMap,
                                  @PathVariable("id") int id) {
        modelMap.addAttribute("id", id);
        return "sendMessage";
    }

    @PostMapping("/send")
    public String sendMessage(@ModelAttribute Message message,
                              @AuthenticationPrincipal SpringUser springUser) {
        if (messageService.save(message, springUser) == null) {
            return "redirect:/students?msg=You can only send messages to your classmates!";
        }
        return "redirect:/students?msg=Message sent!";
    }
}
