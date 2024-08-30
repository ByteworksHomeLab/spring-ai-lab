package com.ahead.airbnb.controllers;

import com.ahead.airbnb.services.IngestionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Controller for handling Airbnb-related operations.
 */
@RestController
public class AirbnbController {

	private static final Logger log = LoggerFactory.getLogger(AirbnbController.class);

	private final VectorStore vectorStore;

	private final IngestionService ingestionService;

	private final Resource listingsTemplateResource;

	private PromptTemplate promptTemplate;

	private final ChatModel chatModel;

	private final ChatClient chatClient;

	/**
	 * Constructor for AirbnbController.
	 * @param chatModel the chat model to use
	 * @param builder the chat client builder
	 * @param vectorStore the vector store for similarity search
	 * @param ingestionService the service for data ingestion
	 * @param listingsTemplateResource the resource for the listings template
	 */
	public AirbnbController(final ChatModel chatModel, final ChatClient.Builder builder, final VectorStore vectorStore,
			final IngestionService ingestionService,
			@Value("classpath:/templates/listing.st") Resource listingsTemplateResource) {
		this.chatModel = chatModel;
		this.chatClient = builder.build();
		this.vectorStore = vectorStore;
		this.listingsTemplateResource = listingsTemplateResource;
		this.ingestionService = ingestionService;
	}

	/**
	 * Initializes the prompt template by reading the template from the resource.
	 */
	@PostConstruct
	public void initPromptTemplate() {
		try (Reader reader = new InputStreamReader(this.listingsTemplateResource.getInputStream(), UTF_8)) {
			String template = FileCopyUtils.copyToString(reader);
			this.promptTemplate = new PromptTemplate(template);
		}
		catch (IOException e) {
			throw new UncheckedIOException(e);
		}

	}

	/**
	 * If you aren't using a GPU, this method may take a couple of minutes to return the
	 * result. In a broswer:
	 * http://localhost:8080/rag?message=2%20bedroom%2c2%20bath%2cclose%20to%20downtown%20austin
	 *
	 * With httpie CLI: http ":8080/rag?message=2 bedroom 2 bath close to downtown"
	 * @param message - The host's listing description
	 * @return - A generated description based on your input that is similar to listings
	 * returned from the vector database.
	 */
	@Operation(
			summary = "The user provides a description of a listing and the system uses the RAG model to generate a similar description based on the user's input and the listings in the database.")
	@GetMapping("/rag")
	@ResponseStatus(HttpStatus.OK)
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

	/**
	 * Ingests data from the Airbnb CSV file into the vector store. This process runs for
	 * about an hour.
	 * @return a response entity indicating the ingestion process has started
	 */
	@Operation(summary = "Ingest data from the Airbnb CSV file into the vector store. It runs for about an hour.")
	@GetMapping("/run-ingestion")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> ingest() {
		ingestionService.ingest();
		return ResponseEntity.accepted().build();
	}

}
