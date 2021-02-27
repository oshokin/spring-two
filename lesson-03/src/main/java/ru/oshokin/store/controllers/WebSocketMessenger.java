package ru.oshokin.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.oshokin.store.entities.OutcomingMessageJS;
import ru.oshokin.store.interfaces.IWebSocketMessenger;

@Controller
public class WebSocketMessenger implements IWebSocketMessenger {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendMessage(String destination, OutcomingMessageJS message) {
        messagingTemplate.convertAndSend(destination, message);
    }

}
