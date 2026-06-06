package agri_chatbot.controller;

import agri_chatbot.model.ChatRequest;
import agri_chatbot.model.ChatResponse;
import agri_chatbot.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String reply = chatService.getReply(
                request.getMessage(),
                request.getCity(),
                request.getLanguage()
        );
        return new ChatResponse(reply);
    }
}
