# Build your own Spring AI Project from Scratch using OpenAI in under 10-minutes
[Previous](3-Ollama.md) | [Next](5-Vector-Embedding.md)

Okay, now that you know how to run everything in this [GitHub project](https://github.com/ByteworksHomeLab/spring-ai-lab), it's your turn to set up your own Spring AI
project. This will take less than 10 minutes.

## Spring AI Library
Use [Spring Initializr](https://start.spring.io) to create a project with the dependencies shown below. To keep it simple, we'll start with
just OpenAPI and Spring Web.

![start-spring-io.png](src%2Fmain%2Fresources%2Fstatic%2Fstart-spring-io.png)

## Set up the Application Properties

Paste these bare-bones properties file for ChatGPT into the `application.yml` file.

```yaml
spring:
  application:
    name: demo
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: gpt-4o
```

Next, add a REST endpoint.

```shell
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    
    private final ChatClient chatClient;

    public ChatController(final ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    
    @GetMapping("/")
    public String prompt(@RequestParam(value = "message", defaultValue = "Tell me a dad joke") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content(); // short for getResult().getOutput().getContent();
    }
}
```

That is all you need to get started. Make sure the unit test runs, then start the project and use ChatGPT.

1) Run ` . ./.env`
2) Run `mvn spring-boot:run`.
2) Open http://localhost:8080?message=You are a Spring Developer Advocate. Tell me about Spring AI. Use HTML for the response.

Next, take a look at this project and experiment with other features like Ollama and a vector database. Have fun!

[Previous](3-Ollama.md) | [Next](5-Vector-Embedding.md)
