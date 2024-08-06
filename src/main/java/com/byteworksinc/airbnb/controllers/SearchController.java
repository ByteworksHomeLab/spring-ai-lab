package com.byteworksinc.airbnb.controllers;

import com.byteworksinc.airbnb.entities.Listing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
public class SearchController {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    private final VectorStore vectorStore;
    private static final SystemMessage systemMessage = new SystemMessage("""
        - You are a travel agent that finds Airbnb listings for users. 
        - If you do not know the answer, say you don't know.
        - Return the top 3 results.
    """);

    private Resource listingsTemplateResource;

    private PromptTemplate  promptTemplate;

    private final ChatModel chatModel;

    public SearchController(final ChatModel chatModel, final VectorStore vectorStore, @Value("classpath:/templates/listing.st") Resource listingsTemplateResource) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
        this.listingsTemplateResource = listingsTemplateResource;
        this.initPromptTemplate();
    }

    public void initPromptTemplate() {

        try (Reader reader = new InputStreamReader(this.listingsTemplateResource.getInputStream(), UTF_8)) {
            String template = FileCopyUtils.copyToString(reader);
            this.promptTemplate = new PromptTemplate(template);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    @GetMapping("/prompt-stuffing")
    public String promptStuffing(@RequestParam(value = "message", defaultValue = "What is the lowest price listing") String message) {
        log.info("promptStuffing() <- {}", message);
        List<Document> documents = this.vectorStore.similaritySearch(message);
        List<String> listings = new ArrayList<>();
        for (Document document: documents) {
            listings.add(document.getContent());
        }
        log.info("promptStuffing() found {} similar results", documents.size());
        Message userMessage = promptTemplate.createMessage(Map.of(
                "input", message,
                "listings", listings
        ));
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        return this.chatModel.call(prompt)
                .getResult().getOutput().getContent();
    }
}
