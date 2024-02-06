package org.example.studentmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.entity.Message;
import org.example.studentmanagement.entity.UserType;
import org.example.studentmanagement.security.SpringUser;
import org.example.studentmanagement.service.MessageService;
import org.example.studentmanagement.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    private final UserService userService;

    @GetMapping("/messages/{id}")
    public String messagesPage(ModelMap modelMap,
                               @PathVariable("id") int id,
                               @AuthenticationPrincipal SpringUser springUser) {
        if (springUser.getUser().getId() == id) {
            modelMap.addAttribute("messages", messageService.findAllByToId(id));
            return "messages";
        }
        return "users";
    }

    @GetMapping("/messages/send")
    public String sendMessagePage(@AuthenticationPrincipal SpringUser springUser,
                                  @RequestParam(value = "msg", required = false) String msg,
                                  ModelMap modelMap) {
        if (msg != null && !msg.isEmpty()) {
            modelMap.addAttribute("msg", msg);
        }
        modelMap.addAttribute("users", userService.findAllByUserType(UserType.STUDENT));
        return "sendMessage";
    }

    @PostMapping("/messages/send")
    public String sendMessage(@ModelAttribute Message message,
                              @AuthenticationPrincipal SpringUser springUser) {
        if (messageService.save(message, springUser) == null) {
            return "redirect:/messages/send?msg=You can only send messages to your classmates!";
        }
        return "redirect:/messages/send?msg=Message sent!";
    }
}
