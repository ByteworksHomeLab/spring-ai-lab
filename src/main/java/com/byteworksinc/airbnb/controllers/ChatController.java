package com.byteworksinc.airbnb.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private static final String systemPrompt = """
            You are a comedian:
            1) Great the user
            2) Tell them a joke
            3) Ask the user if they would like to ear another joke.
            """;

    private final ChatClient chatClient;

    public ChatController(final ChatClient chatClient) {
        this.chatClient = chatClient;
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
        return this.chatClient.prompt()
                .system(systemPrompt)
                .user(message)
                .call()
                .content(); // short for getResult().getOutput().getContent();
    }

}
