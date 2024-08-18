package com.ahead.airbnb.controllers;

import com.ahead.airbnb.etl.IngestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
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

    private final Resource listingsTemplateResource;

    private PromptTemplate promptTemplate;

    private final ChatModel chatModel;

    private final ChatClient chatClient;

    public AirbnbController(final ChatModel chatModel,
                            final ChatClient.Builder builder,
                            final VectorStore vectorStore,
                            final IngestionService ingestionService,
                            @Value("classpath:/templates/listing.st") Resource listingsTemplateResource) {
        this.chatModel = chatModel;
        this.chatClient = builder.build();
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
     *
     * @param message e.g. http://localhost:8080/prompt-stuffing?message=2%20bedroom%2c2%20bath%2cclose%20to%20downtown%20austin
     * @return - A generated description based on your input that is similar to listings returned from the vector database.
     */
    @GetMapping("/prompt-stuffing")
    public String promptStuffing(@RequestParam String message) {
        log.info("promptStuffing() <- {}", message);
        List<Document> documents = this.vectorStore.similaritySearch(message);
        List<String> listings = new ArrayList<>();
        for (Document document : documents) {
            log.info(document.getFormattedContent());
            listings.add(document.getContent());
        }
        log.info("promptStuffing() found {} similar results", documents.size());
        SystemMessage systemMessage = new SystemMessage("""
                    - Rewrite the user's Airbnb description using the information provided by the user in their prompt.
                    - DO NOT change the number of rooms or number of bathrooms from the host's description.
                    - When rewriting the user's description consider similar descriptions from the LISTINGS section.
                    - Return your single best-rewritten description.
                """);
        Message userMessage = promptTemplate.createMessage(Map.of(
                "input", message,
                "listings", listings
        ));
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        return this.chatModel.call(prompt)
                .getResult()
                .getOutput()
                .getContent();
    }

    /**
     * If you aren't using a GPU, this method may take a couple of minutes to return the result.
     *
     * @param message e.g. http://localhost:8080/rag?message=2%20bedroom%2c2%20bath%2cclose%20to%20downtown%20austin
     * @return - A generated description based on your input that is similar to listings returned from the vector database.
     */
    @GetMapping("/rag")
    public String rag(@RequestParam String message) {
        log.info("rag() <- {}", message);
        return this.chatClient.prompt()
                .system("""
                            - Rewrite the user's Airbnb description using the information provided by the user in their prompt.
                            - DO NOT change the number of rooms or number of bathrooms from the host's description.
                            - Return your single best-rewritten description.
                        """)
                .user(message)
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .call()
                .content(); // short for getResult().getOutput().getContent();
    }

    @GetMapping("/run-ingestion")
    public ResponseEntity<?> ingest() {
        ingestionService.ingest();
        return ResponseEntity.accepted().build();
    }
}
