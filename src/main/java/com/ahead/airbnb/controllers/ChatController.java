package com.ahead.airbnb.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final ChatClient chatClient;

    public ChatController(final ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * A basic example of how to use the chat client to pass a message and call the LLM
     *
     * @param message - Describe the type of joke you want to hear.
     * @return A joke
     */
    @GetMapping("/")
    public String prompt(@RequestParam(value = "message", defaultValue = "Tell me a dad joke") String message) {
        log.info("prompt() <- {}", message);
        return chatClient.prompt()
                .user(message)
                .call()
                .content(); // short for getResult().getOutput().getContent();
    }

}
