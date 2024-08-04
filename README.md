# Spring AI Lab
#### https://github.com/ByteworksHomeLab/spring-ai-lab
This project is an introduction to Spring AI. It demonstrates running three LLMs with Spring AI: Llama 3 on the Ollama platform, plus Groq and OpenAI in the Cloud. We use a combination of Maven profiles and Spring 
profiles to switch between the LLMs without having to change any Java code.

The project relies on Docker for simplicity.

![Spring AI Ollama.png](src%2Fmain%2Fresources%2Fstatic%2FSpring%20AI%20Ollama.png)

JUnit tests use Docker Test Containers to dynamically spin up Postgres PGVector. The Spring Boot AI application spins up Docker Compose automatically when it runs providing both Ollama and Postgres PGVector. 
The only thing not automated is the installation of the Llama3 LLM, but that is a one-time task (unless the Docker volume gets deleted).

__At the end of this README file, you'll learn how to get up and running with OpenAI in less than 15 minutes__.

## Audience
The audience or this project is Spring Framework application developers who are new to artificial intelligence. It is expected that you know your way around the Spring Framework, 
but we don't assume any AI experience.

## Use Cases
Your employer asks you to:
- Improve your Spring Customer Service App by combining the company's Knowledge base with a natural language AI interface.
- Help Sales and Marketing by enhances your Spring sales application with natural language searching of company marketing material.
- Improve coding efficiency and consistency by embedding existing company code repositories with AI for code generation.
- Host a private LLM (Large Language Model) so employees can use take advantage of AI internally, without transmitting sensitive company or client data over the public internet.

You probably see a trend here. As a Spring Developer, you may be asked to add AI to your Spring application that augments an opensource large 
language model with proprietary company data. These are the situations where Spring AI can help.

## Getting Started
The purpose of this project is to dip your toes into the AI waters. We'll demonstrate using Spring AI calling OpenAI and Groq via their respective APIs, so you'll need to sign up for free API keys from both.
Also, we'll install Ollama to run the Llama3 LLM locally. Ollama is Like VirtualBox or Docker Desktop, but instead of running virtual machines or containers, it runs LLMs.
One caveat is that you'll need realistic expectations about local LLM performance.

### What about GPUs?
The good news is that you can do this lab without a GPU as long as you have patience. Performance is terrible on my 2019 Intel Macbook Pro, 
but AI response time is tolerable on my M1 MacBook Pro.

[Meta's Lama 3 Requirements](https://llamaimodel.com/requirements/) say you can get by with an NVidia GeForce RTX 3000-series GPU, which has about 12 GB RAM. 
This NLP Cloud's article [How to Install and Deploy LLaMA 3 Into Production?](https://nlpcloud.com/how-to-install-and-deploy-llama-3-into-production.html) recommends 20 GB RAM on the GPU, like an NVidia A10, for production use. 

My 10-year-old home lab workstations didn't justify an expensive GPU, so I bought a used Nvidia GeForce 1080 for $120 on eBay, 
and it performs great! My response time dropped from tens of seconds on my M1 MacBook Pro to close to one second on my Ubuntu workstation with the GeForce 1080.

## Preconditions

This project demonstrates a few different concepts, and as a result, it has a few prerequisites.

### 1) Java

This project is using Java 22, but a lower version of Java should work too if you update the Java version in the `pom.xml` file. [SDK Man](https://sdkman.io/) is the 
easiest way to switch around SDK versions:

```Shell
sdk install java 22.0.1-tem
sdk use java 22.0.1-tem
```

### 2) Maven

This project was built using Maven 3.9.8, but any recent version of Maven will be fine.

### 3) PostgreSQL

PostgreSQL is used as the vector database for the project. Specifically, we are using the PGVector Docker image that includes vector database support.
PGVector is defined in the `docker-compose.yaml` file at the root of the project.

### 4) Ollama

[Ollama](https://ollama.com) is an opensource platform for running LLMs locally. It makes it easier to get started with AI by hiding the complexities
of running a LLM (Large Language Model). Choose between the [Ollama Docker image](https://hub.docker.com/r/ollama/ollama), or [downloading the binary to your operating system](https://ollama.com/download/).

Ollama is also defined in the `docker-compose.yaml` file at the root of the project.

Ollama supports many different LLMs. Visit the [Ollama models page](https://ollama.com/library) for the list of supported LLMs, ranked by popularity.

![ollama-models.png](src/main/resources/static/ollama-models.png) 

For this example, we will use [Meta's llama 3.1:8b](https://ollama.com/library/llama3.1). 

### 5) OpenAI

In addition to running LLM models locally, this project demonstrates connecting to the OpenAI API. For that, you'll need to create an OpenAI API Key. You can start with a free 
OpenAI account. Later, when you bump up against rate limiting, you can upgrade to a pay-as-you-go account. 

[Here is the link to create an OpenAI API key](https://platform.openai.com/settings/profile?tab=api-keys). Be sure to save the API Key somewhere safe when you create it.

### 6) Groq

This project demonstrates connecting to the Groq API too. You'll need to create a Groq API Key. 

[Here is the link to create a Groq API key](https://console.Groq.com/keys). Be sure to save the API Key.

### 7) Set your environment variables.

You need to export variables into your terminal or IDE runtime environment. The [docker-compose.yml](docker-compose.yml) and [application.yml](src%2Ftest%2Fresources%2Fapplication.yml) files both need environment variables to be set.

It's more convenient to run the variable exports inside a script file. Let's assume that you put the export statements in a file named `env.sh` at the root of the project, like this:

```shell
#!/bin/bash
export DB_USER=my-postgres-username
export DB_PASSWORD=my-postgres-password
export OPENAI_API_KEY=my-openai-api-key
export Groq_API_KEY=my-grok-api-key
```

Use any credentials you want for Postgres, plus the Groq and OpenAI API keys created above. The `gitignore` file already contains and entry for a file named `env.sh.` Use the ". ./path" syntax on Mac or Linux to add the environment variables to the terminal session, as shown here:

```shell
. ./env.sh
```
To run the application, tests, or `Docker Compose` from your IDE, add the environment variables to the runtime configurations in your IDE too. This screenshot shows the 
configuration for the JUnit tests in Intellij. Do the same thing for Application.java or Docker if you like.

![IntelliJ Runtime](src/main/resources/static/intellij-runtime.png)

### Launch Ollama and PGVector Together Using Docker Compose for the First Time
Now you are ready to try out the `Docker Compose` file. Normally, running Spring Boot starts Docker Compose automatically because of the `spring-boot-docker-compose` library, but we want to do some housekeeping first.

Start `Docker Compose` from the root of the project as shown:

```shell
. ./env.sh
docker compose up -d
```

Verify that Postgres and Ollama are working by using the `docker compose ps` command.

```shell
docker compose ps

NAME       IMAGE                    COMMAND                  SERVICE    CREATED          STATUS          PORTS
ollama     ollama/ollama            "/bin/ollama serve"      ollama     28 minutes ago   Up 28 minutes   0.0.0.0:11434->11434/tcp
postgres   pgvector/pgvector:pg16   "docker-entrypoint.s…"   postgres   29 minutes ago   Up 28 minutes   0.0.0.0:5432->5432/tcp
```

You can also confirm that the Ollama API is ready by navigating to [http://localhost:11434](http://localhost:11434) in a web browser. It should return the message: "Ollama is running."

Next, install the LLama3 LLM using the `ollama` CLI installed on the Ollama Docker container.

```shell
docker exec -it ollama ollama run llama3.1:8b
```

The first time you issue the Ollama `run` command it downloads and installs the LLM. Your terminal session may time out during the installation, but the LLM was probably successfully installed. 
Reconnect to the Ollama Docker container to rerun the `ollama run llama3.1:8b` command. The Ollama prompt should return quickly since the llama3 LLM is already installed. Try asking it a question.

```shell
docker exec -it ollama ollama run llama3.1:8b

>>> You are a newly installed LLM. Please tell me your version in six lines or less.

I'm an LLaMA 13 model, specifically trained for conversation and creativity. My knowledge cutoff is September 2022, and I have been fine-tuned to provide accurate
and engaging responses. I can generate text on a wide range of topics, from science and history to entertainment and culture.

>>>/bye
```

You also need to pull the embedding model, `all-minilm.` Ollama supports [three embedding models](https://ollama.com/blog/embedding-models).

| Embedding Model                                                   | Parameters   |
|-------------------------------------------------------------------|--------------|
| [mxbai-embed-large](https://ollama.com/library/mxbai-embed-large) | 334M         |
| [nomic-embed-text](https://ollama.com/library/nomic-embed-text)   | 137M         |
| [all-minilm](https://ollama.com/library/all-minilm)               | 23M          |

Use the `ollama pull` command to pull down the embedding model.

```shell
docker exec -it ollama ollama pull all-minilm
```

In addition, you must match the Vector Store to the embedding model dimensions.

```sql
CREATE TABLE IF NOT EXISTS public.vector_store
(
    id        uuid default uuid_generate_v4() not null
        primary key,
    content   text,
    metadata  json,
    embedding vector(384)
);
```

Or alter the embedding column.

```sql
ALTER TABLE public.vector_store
    ALTER COLUMN embedding TYPE vector(384);
```

Finally, make sure the embedding model and embedding type are part of the application properties.

```yaml
spring:
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: llama3.1:8b
      embedding:
        enabled: true
        model: all-minilm
    vectorstore:
      pgvector:
        index-type: hnsw
        dimensions: 384
        distance-type: cosine_distance
```

Shutdown Docker Compose, so you can demonstrate that the Spring Boot `spring-boot-docker-compose` library automatically starts Docker Compose. From the root directory of the project, do this:

```shell
docker compose down
```

That's it. Llama3 is ready to use with Spring AI. Follow the same process to run other [models found on the Ollama website](https://ollama.com/library).

## Project Testing

You should now be able to execute the unit tests from your IDE, or from the command line. Be sure to export the 
environmental variables in your terminal or IDE runtime configuration.

```shell
. ./env.sh
mvn clean test
```

## Spring Boot Run

Spring Boot also needs the environment variables exported to in the terminal or added to your IDE's runtime configuration. Once the environment variables are exported, `spring-boot-docker-compose` automatically brings up Docker Compose whenever Spring Boot is run.

The default Maven profile is "ollama," and the default Spring profile is "llama3," so if that is all you need, then a simple `mvn spring-boot:run` 
will suffice.

```shell
. ./env.sh
mvn spring-boot:run 
```
The output should look like this:

![spring-boot-run-docker-compose.png](src%2Fmain%2Fresources%2Fstatic%2Fspring-boot-run-docker-compose.png)

If you want to use OpenAI, then you must set the Maven profile to "openapi" and Spring profile to "gpt-4o."

```shell
. ./env.sh
mvn -Popenai spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=gpt-4o"
```

If you want to use Groq, then you must set the Maven profile to "openapi" and Spring profile to "groq."

```shell
. ./env.sh
mvn -Popenai spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=groq"
```

# Build your own Spring AI Project
Okay, now that you know how to run everything in this [GitHub project](https://github.com/ByteworksHomeLab/spring-ai-lab), it's your turn to set up your own Spring AI project. This will take less than 15 minutes.

## Spring AI Library
Use [Spring Initializr](https://start.spring.io) to create a project with the dependencies shown below. To keep it simple, we'll start with just OpenAPI and Spring Web.

![start-spring-io.png](src%2Fmain%2Fresources%2Fstatic%2Fstart-spring-io.png)

## Set up the Application Properties

Paste these bare-bones properties file for ChatGPT into the `application.yml` file.

```yaml
spring:
  application:
    name: demo
  ai:
    openai:
      api-key: YOUR_OPENAI_API_KEY_HERE
      chat:
        options:
          model: gpt-4o
```

Next, define a Spring Bean for the Spring AI ChatClient.

```shell
@Configuration
public class Config {
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

}
```

Finally, add a REST endpoint that calls the autowired `chatClient.`

```shell
@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(final ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/")
    public String simplePrompt(@RequestParam(value = "message", defaultValue = "Tell me a dad joke") String message) {
        return this.chatClient.prompt()
                .user(message)
                .call()
                .content(); 
    }

}
```

That is all you need to get started. Make sure the unit test runs, then start the project and use ChatGPT.

1) Run `mvn spring-boot:run`.
2) Open http://localhost:8080?message=You are a Spring Developer Advocate. Tell me about Spring AI. Use HTML for the response.

Next, take a look at this project and experiment with other features like Ollama and a vector database. Have fun!



