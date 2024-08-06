package dev.chriskarpyszyn.pingugpt;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * PinguGptController is a controller class that handles the main functionality of PinguGpt chat application.
 * It provides endpoints for generating a unique chat ID, initiating a chat session, and streaming responses to the client.
 */
@Controller
@CrossOrigin
public class PinguGptController {

    private static final Logger log = LoggerFactory.getLogger(PinguGptController.class);
    private final ChatClient.Builder chatClientBuilder;
    private final ChatMemory chatMemory;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * PinguGptController
     *
     * This class represents a controller for the Pingu Gpt Chat application. It handles various HTTP requests and interacts with the ChatClient.
     */
    @Autowired
    public PinguGptController(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
        this.chatMemory = new InMemoryChatMemory();
    }

    /**
     * Returns the view name "index" after generating a unique chat ID and adding it as a model attribute.
     *
     * @param model the model object to add attributes to
     * @return the view name "index"
     */
    @GetMapping("")
    public String home(Model model) {
        String chatId = UUID.randomUUID().toString();
        model.addAttribute("chatId", chatId);
        return "index";
    }

    /**
     * Initiates a chat session by processing the user's message and returning an HtmxResponse.
     *
     * @param message the user's message
     * @param chatId the identifier of the chat session
     * @param model the model object to add attributes to
     * @return an HtmxResponse object containing the views to render
     */
    @HxRequest
    @PostMapping("/api/chat")
    public HtmxResponse initiate(@RequestParam String message, @RequestParam String chatId, Model model) {
        log.info("User message: {}, Chat ID: {}", message, chatId);
        model.addAttribute("message", message);
        return HtmxResponse.builder()
                .view("response :: responseFragment")
                .view("recent-message-list :: messageFragment")
                .build();
    }

    /**
     * Streams a response to the client using Server-Sent Events (SSE).
     *
     * @param message the message to send to the backend chat client
     * @param chatId the identifier of the chat session
     * @return an SseEmitter instance that streams the response to the client
     */
    @GetMapping(value = "/api/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamResponse(@RequestParam String message, @RequestParam String chatId) {
        SseEmitter emitter = new SseEmitter();

        executorService.execute(() -> {
            try {
                ChatClient chatClient = chatClientBuilder
                        .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                        .build();

                String response = chatClient.prompt()
                        .user(message)
                        .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                        .call()
                        .content();

                // Simulate streaming by sending chunks of the response
                String[] words = response.split("\\s+");
                for (String word : words) {
                    emitter.send(word + " ");
                    Thread.sleep(10); // Adjust delay as needed
                }
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }

    @Scheduled(fixedRate = 3600000) // Run every hour
    public void cleanupChatMemories() {
        // Implement logic to remove old or inactive chat memories
    }
}