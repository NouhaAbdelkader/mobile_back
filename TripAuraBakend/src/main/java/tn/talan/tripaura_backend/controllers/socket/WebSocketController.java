package tn.talan.tripaura_backend.controllers.socket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/message")
    @SendTo("/topic/replies")
    public String processMessageFromClient(String message) {
        // Logique de traitement du message
        return "Message reçu : " + message;
    }
}
