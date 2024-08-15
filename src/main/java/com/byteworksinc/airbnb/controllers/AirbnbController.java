package com.byteworksinc.airbnb.controllers;

import com.byteworksinc.airbnb.etl.IngestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
public class AirbnbController {

    private static final Logger log = LoggerFactory.getLogger(AirbnbController.class);

    private final VectorStore vectorStore;
    private final IngestionService ingestionService;
    private static final SystemMessage systemMessage = new SystemMessage("""
//        - You work for Airbnb.
//        - You help hosts write better descriptions for airbnb listings.
        - Rewrite the user's Airbnb description using the information provided by the user in their prompt.
        - DO NOT change the number of rooms or number of bathrooms from the host's description.
        - When rewriting the user's description consider similar descriptions from the LISTINGS section.
        - Return your single best-rewritten description.
    """);

    private final Resource listingsTemplateResource;

    private PromptTemplate  promptTemplate;

    private final ChatModel chatModel;

    public AirbnbController(final ChatModel chatModel,
                            final VectorStore vectorStore,
                            final IngestionService ingestionService,
                            @Value("classpath:/templates/listing.st") Resource listingsTemplateResource) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
        this.listingsTemplateResource = listingsTemplateResource;
        this.ingestionService = ingestionService;
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

    /**
     * If you aren't using a GPU, this method may take a couple of minutes to return the result.
     * @param message e.g. http://localhost:8080/prompt-stuffing?message=2%20bedroom%2c2%20bath%2cclose%20to%20downtown%20austin
     * @return - A generated description based on your input that is similar to listings returned from the vector database.
     */
    @GetMapping("/prompt-stuffing")
    public String promptStuffing(@RequestParam String message) {
        log.info("promptStuffing() <- {}", message);
        List<Document> documents = this.vectorStore.similaritySearch(message);
        List<String> listings = new ArrayList<>();
        for (Document document: documents) {
            log.info(document.getFormattedContent());
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

    @GetMapping("/run-ingestion")
    public ResponseEntity<?> ingest() {
        ingestionService.ingest();
        return ResponseEntity.accepted().build();
    }
}
