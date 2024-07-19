package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Message;
import com.bezkoder.spring.security.postgresql.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class WebSocketController {
    private final SimpMessagingTemplate template;
    private final MessageService messageService;

    public WebSocketController(SimpMessagingTemplate template, MessageService messageService) {
        this.template = template;
        this.messageService = messageService;
    }

    @MessageMapping("/chat")
    public void sendMessage(Message message) {
        if (message.getTimestamp() == null) {
            message.setTimestamp(LocalDateTime.now());
        }
        messageService.saveMessage(message);
        template.convertAndSendToUser(message.getReceiver().getUsername(), "/queue/messages", message);
    }


}
