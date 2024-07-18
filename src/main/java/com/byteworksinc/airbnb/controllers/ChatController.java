package com.byteworksinc.airbnb.controllers;


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

    private final ChatClient chatClient;

    public ChatController(final ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * A basic example of how to use the chat client to pass a message and call the LLM
     * @param message
     * @return
     */
    @GetMapping("/")
    public String joke(@RequestParam(value = "message", defaultValue = "Tell me a dad joke") String message) {
        return this.chatClient.prompt()
                .user(message)
                .call()
                .content(); // short for getResult().getOutput().getContent();
    }

    /**
     * Take in a topic as a request parameter and use that param in the user message
     * @param topic
     * @return
     */
    @GetMapping("/jokes-by-topic")
    public String jokesByTopic(@RequestParam(value = "topic", defaultValue = "animals") String topic) {
        return chatClient.prompt()
                .user(u -> u.text("Tell me a joke about {topic}").param("topic",topic))
                .call()
                .content();
    }

    /**
     * What if you didn't want to get a String back, and you wanted the whole response?
     * @param message
     * @return
     */
    @GetMapping("jokes-with-response")
    public ChatResponse jokeWithResponse(@RequestParam(value = "message", defaultValue = "Tell me a dad joke about computers") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
    }

    @GetMapping("jokes-by-pete")
    public ChatResponse jokesByPete(
            @RequestParam(value = "name", defaultValue = "friend") String name,
            @RequestParam(value = "adjective", defaultValue = "dad") String adjective,
            @RequestParam(value = "topic", defaultValue = "animals") String topic
    ) {
        String userText = "My name is {name}. Tell me a {adjective}  joke about {topic}.";
        PromptTemplate userPromptTemplate = new PromptTemplate(userText);
        Message userMessage = userPromptTemplate.createMessage(Map.of("name", name, "adjective", adjective, "topic", topic));
        String systemText = """
                               You're a comedian named {name}.
                               First, greet the user by name, introduce yourself, then reply to the user's request.
                               Finish by thanking the user for asking for a joke.
                             """;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", "Alexa"));
        System.out.println(systemMessage.getContent());
        return chatClient.prompt()
                .user(userMessage.getContent())
                .system(systemMessage.getContent())
                .call()
                .chatResponse();
    }

}
