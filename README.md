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
If you don't want to read the detailed instructions, here is a quick start guide.

## Store your Environment Variables

Create a file named `.env` at the root of this project like that shown below. Pick whatever you want for the database user and password.
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
Verify that everything is working by running the unit tests.

```shell
. ./.env
mvn clean test
```

## Spring Boot Run

Spring Boot needs the environment variables exported to in the terminal or added to your IDE's runtime configuration. 
Once the environment variables are exported, `spring-boot-docker-compose` automatically brings up Docker Compose whenever 
Spring Boot is started.

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

## Call the GPT-4o with Spring AI

Start Spring Boot with the `openai` Maven profile and the `gpt-4o` Spring profile.  
```shell
. ./.env
mvn -Popenai spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=gpt-4o"
```

Use Spring AI to call the GPT-4o model. 
```shell
http ":8080/chat?message=Tell me a dad joke about animals?"
```

## Call Groq with Spring AI

Restart Spring Boot with the `openai` Maven profile and the `groq` Spring profile.
```shell
. ./.env
mvn -Popenai spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=groq"
```
Use Spring AI to call the Groq model.
```shell
http ":8080/chat?message=Tell me a dad joke about animals?"
```

## Call Llama3 running locally on Ollama

Start Spring Boot with the default profiles, `ollama` Maven profile and the `llama3` Spring profile.
```shell
. ./.env
mvn spring-boot:run
```

Use Spring AI to call the Llama3 model running locally on Ollama.
```shell
http ":8080/chat?message=When did the Apple II come out"
```

## Rewrite an Airbnb Listing Description using RAG

This one takes a little setup. First, you need to load the Airbnb CSV file into the vector store.
The process takes about 50 minutes, depending on your machine. Start Spring Boot if it is not already running.

```shell
. ./.env
mvn spring-boot:run
```

Next, kick off the embedding process.

```shell
http ":8080/run-ingestion"
```
You can monitor its process by checking the console logs. When it is done, you can call the RAG model.

```shell
http ":8080/rag?message=2-bedroom 2-bathroom close to downtown Austin"
```

It may take more than two minutes to complete if you are running on a laptop, less time if you are on a machine with a GPU.

## Hold Conversational State

For this example, you may use any of the LLMs shown above, gpt-4o, groq, or llama3. You need to ask a question that 
requires a follow-up question. The key is to use the `chatId` parameter to hold the conversational state. 

Here we ask when the Apple II came out and then ask what other computers came out that year. The second question is 
dependent on the first question because it assumes that the LLE knows we are talking about the year that the Apple II came out.
```shell
http ":8080/conversation?message=When did the Apple II come out?&chatId=1234"
http ":8080/conversation?message=What other computers came out that year?&chatId=1234"
```