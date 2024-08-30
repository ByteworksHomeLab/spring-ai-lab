package com.ahead.airbnb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Controller for handling Airbnb-related operations.
 */
@RestController
public class ChatController {

	private static final Logger log = LoggerFactory.getLogger(ChatController.class);

	private final ChatClient chatClient;

	private final ChatModel chatModel;

	private final ChatClient statefulChatClient;

	private Resource restrictionsTemplateResource;

	private PromptTemplate promptTemplate;

	/**
	 * Constructor for ChatController.
	 * @param builder the chat client builder
	 */
	public ChatController(final ChatModel chatModel, final ChatClient.Builder builder,
			@Value("classpath:/templates/restrictions-prompt.st") Resource restrictionsTemplateResource) {
		this.chatModel = chatModel;
		this.chatClient = builder.build();
		statefulChatClient = builder.defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory())).build();
		this.restrictionsTemplateResource = restrictionsTemplateResource;
	}

	/**
	 * Initializes the prompt template by reading the template from the resource.
	 */
	@PostConstruct
	public void initPromptTemplate() {

		try (Reader reader = new InputStreamReader(this.restrictionsTemplateResource.getInputStream(), UTF_8)) {
			String template = FileCopyUtils.copyToString(reader);
			this.promptTemplate = new PromptTemplate(template);
		}
		catch (IOException e) {
			throw new UncheckedIOException(e);
		}

	}

	/**
	 * A basic example of how to use the chat client to pass a message and call the LLM
	 * With a web browser: -
	 * http://localhost:8080/chat?message=When%20was%20the%20Apple%20II%20released%3F With
	 * httpie CLI: - http ":8080/chat?message=When was the Apple II released?"
	 * @param message - Input your question for the LLM to answer
	 * @return The answer to your question.
	 */
	@Operation(summary = "Get a single response from the LLM without holding state of the conversation")
	@GetMapping("/chat")
	@ResponseStatus(HttpStatus.OK)
	public String getStatelessChat(@RequestParam(value = "message",
			defaultValue = "When was the Apple II computer released?") String message) {
		return chatClient.prompt().user(message).call().content(); // short for
																	// getResult().getOutput().getContent();
	}

	/**
	 * An example of how to maintain conversational state with the LLM. With a web
	 * browser: -
	 * http://localhost:8080/conversation?message=When%20was%20the%20Apple%20II%20released%3F&chatId=1234
	 * -
	 * http://localhost:8080/conversation?message=What%20other%20computers%20came%20out%20that%20year%3F&chatId=1234
	 * With httpie CLI: - http ":8080/conversation?message=When was the Apple II
	 * released?&chatId=1234" - http ":8080/conversation?message=What other computers came
	 * out that year?&chatId=1234"
	 * @param message - Input your question for the LLM to answer
	 * @return The answer to your question.
	 */
	@Operation(summary = "Get a conversation response from the LLM by holding the state of a conversation in memory.")
	@GetMapping("/conversation")
	@ResponseStatus(HttpStatus.OK)
	public String conversation(
			@RequestParam(value = "message", defaultValue = "When was the Apple II computer released?") String message,
			@RequestParam(defaultValue = "default") String chatId) {
		return statefulChatClient.prompt()
			.user(message)
			.advisors(as -> as.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
			.call()
			.content();
	}

	/**
	 * An example of how to use prompt stuffing to answer questions when vector store data
	 * is not available.
	 * @param message - The user's question about the homeowner's association
	 * restrictions.
	 * @return - The answer to the user's question.
	 */
	@Operation(summary = "Answers questions about home owner's association restrictions using prompt stuffing.")
	@GetMapping("/hoa-restrictions")
	@ResponseStatus(HttpStatus.OK)
	public String getHAOARestrictionsAnswer(
			@RequestParam(value = "message", defaultValue = "Can I store a boat on my property?") String message) {
		SystemMessage systemMessage = new SystemMessage("""
				- Answer the user's question about the home owner's association restrictions.
				- Use the DOCUMENTS provided to answer the question.
				- If you don't know the answer, you can say so.""");
		String documents = """
				No truck, trailer, boat, equipment or machinery or cars not in daily use may be
				   parked or otherwise maintained on any lot or street in the Properties.
				   f. No signs, billboards or advertising structures of any kind may be maintained on any
				   lot; provided, however, that permission is hereby granted for the erection and
				   maintenance of not more than two advertising signs on each lot, the total face area
				   of which shall not exceed ten square feet per sign and which may be used only for
				   advertising for sale of such lot. In addition, Declarant’s sales representative may
				   erect and maintain additional information signs to direct traffic to the model home
				   sales center.""";
		Message userMessage = promptTemplate.createMessage(Map.of("question", message, "documents", documents));
		Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
		return this.chatModel.call(prompt).getResult().getOutput().getContent();
	}

}
