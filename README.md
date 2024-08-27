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
4) [Spring AI](src%2Fmain%2Fresources%2Fstatic%2Fdocs%2F4-Spring-AI.md)
5) [Vector Embedding](src%2Fmain%2Fresources%2Fstatic%2Fdocs%2F5-Vector-Embedding.md)

# QUICK START
You should now be able to execute the unit tests from your IDE, or from the command line. Be sure to export the 
environmental variables in your terminal or IDE runtime configuration first.

## Put the Environment Variables at the Root of the Project in a file named '.env'.

Create a file named `.env` like that shown below. Pick whatever you want for the database user and password.
Sign up for API keys from OpenAI and Groq. 

```shell
export DB_USER=my-postgres-username
export DB_PASSWORD=my-postgres-password
export DB_HOST=localhost
export DATABASE_NAME=airbnb
export OLLAMA_HOST=localhost
export OPENAI_API_KEY=my-openai-api-key
export GROQ_API_KEY=my-grok-api-key
```

## Run the Unit Tests
```shell
. ./.env
mvn clean test
```

## Spring Boot Run

Spring Boot needs the environment variables exported to in the terminal or added to your IDE's runtime configuration. 
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

# Try out some Spring AI Features

With 

