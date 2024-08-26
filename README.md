# Spring AI Lab

Do you want to make your Java apps smarter? This lab will help. It shows you how to add cool AI features to your Java projects using the Spring 
framework. It's perfect for beginners who want to start using AI.

It covers the following:
• Simple Chatbots: Learn how to build basic chatbots that can talk to users using Spring Boot.
• Smart Answers with AI: Discover how to create apps that give better answers by combining search with AI tools.
• Vector Databases: Find out how to use special databases that help AI apps handle lots of information quickly.
• AI Workflows: See how to set up workflows that let your apps do complex tasks on their own.

This project is part of a presentation, [Intruduction to Spring AI](src%2Fmain%2Fresources%2Fstatic%2Fdocs%2FSLIDES.pptx)

## Chapters
More detailed information is available in the following chapters: 
1) [Introduction](src%2Fmain%2Fresources%2Fstatic%2Fdocs%2F1-Introduction.md)
2) [Requirements](src%2Fmain%2Fresources%2Fstatic%2Fdocs%2F2-Requirements.md)
3) [Ollama](src%2Fmain%2Fresources%2Fstatic%2Fdocs%2F3-Ollama.md)

## Project Testing

You should now be able to execute the unit tests from your IDE, or from the command line. Be sure to export the 
environmental variables in your terminal or IDE runtime configuration first.

```shell
. ./.env
mvn clean test
```

## Spring Boot Run

Spring Boot also needs the environment variables exported to in the terminal or added to your IDE's runtime configuration. 
Once the environment variables are exported, `spring-boot-docker-compose` automatically brings up Docker Compose whenever 
Spring Boot is run.

### Picking Maven and Spring Profiles to Run

The default Maven profile is "ollama," and the default Spring profile is "llama3," so if that is all you need,
simply run `mvn spring-boot:run.`

```shell
. ./.env
mvn spring-boot:run 
```

If you want to use OpenAI, then you must set the Maven profile to "openapi" and Spring profile to "gpt-4o."

```shell
. ./.env
mvn -Popenai spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=gpt-4o"
```

If you want to use Groq, then you must set the Maven profile to "openapi" and Spring profile to "groq."

```shell
. ./.env
mvn -Popenai spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=groq"
```

## Vector Embedding

At this point, the project should be running, but the vector database is empty. There is a CSV file with 15,000 Airbnb listings from Austin, TX.
These need to run through the embedding model and written to the `vector_table`. A `/run-ingestion` was added as a convenient way to do the embedding.
It takes about an hour to run, depending on your hardware. With the Spring Boot application running, paste this URL in your browser:

```shell
http://localhost:8080/run-ingestion
```

Messages like these will start appearing in the Spring Boot console output:

```shell
2024-08-18T19:54:53.398-05:00  INFO 13846 --- [spring-ai-airbnb] [oundedElastic-2] o.s.a.transformer.splitter.TextSplitter  : Splitting up document into 2 chunks.
2024-08-18T19:54:53.762-05:00  INFO 13846 --- [spring-ai-airbnb] [oundedElastic-2] o.s.a.transformer.splitter.TextSplitter  : Splitting up document into 2 chunks.
2024-08-18T19:54:55.383-05:00  INFO 13846 --- [spring-ai-airbnb] [oundedElastic-2] o.s.a.transformer.splitter.TextSplitter  : Splitting up document into 2 chunks.
```

Use SQL to verify the results:

```sql
select count(*) from vector_store;
```

There should be 16,249 rows in the vector table.

To see the embedding value, run this query:

```sql
select content, embedding from vector_store limit 10;
```

# Build your own Spring AI Project from Scratch using OpenAI in under 10-minutes
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



