package com.ahead.airbnb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling Airbnb-related operations.
 */
@RestController
public class ChatController {

	private final ChatClient chatClient;
	private final ChatModel chatModel;

	private final ChatClient statefulChatClient;

	/**
	 * Constructor for ChatController.
	 *
	 * @param builder the chat client builder
	 * @param chatModel the chat model
	 */
	public ChatController(final ChatClient.Builder builder,
						  final ChatModel chatModel) {
		this.chatClient = builder.build();
		this.chatModel = chatModel;
		statefulChatClient = builder.defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory())).build();
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
	public String getStatelessChat(@RequestParam(value = "message") String message) {
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
	public String conversation(@RequestParam(value = "message") String message,
			@RequestParam(defaultValue = "default") String chatId) {
		return statefulChatClient.prompt()
			.user(message)
			.advisors(as -> as.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
			.call()
			.content();
	}

}
