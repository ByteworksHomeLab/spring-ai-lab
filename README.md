# Spring AI Lab
#### https://github.com/ByteworksHomeLab/spring-ai-lab
This project is an introduction to Spring AI. It demonstrates running two LLMs, Llama 3 on the Ollama platform and OpenAI, using a combination of Maven profiles and Spring profiles to switch back and forth.

The project relies on Docker for simplicity.

![Spring AI Ollama.png](src%2Fmain%2Fresources%2Fstatic%2FSpring%20AI%20Ollama.png)

The JUnit tests each use Docker Test Containers to dynamically spin up a Postgres PGVector Docker instance for testing.
Likewise, the Spring Boot AI application relies on Docker Compose to spin up an Ollama instance and a Postgres PGVector instance. Well, technically. you have to spin up Docker Compose yourself.

## Audience
The audience or this project is Spring Framework application developers who are new to artificial intelligence. It is expected that you know your way around the Spring Framework, but we don't assume any AI experience.

## Use Cases
Your employer asks you to:
- Improve your Spring Customer Service App by combining the company's Knowledge base with a natural language AI interface.
- Help Sales and Marketing by enhances your Spring sales application with natural language searching of company marketing material.
- Improve coding efficiency and consistency by embedding existing company code repositories with AI for code generation.
- Host a private LLM (Large Language Model) so employees can use take advantage of AI internally, without transmitting sensitive company or client data over the public internet.

You probably see a trend here. As a Spring Developer, you may be asked to add AI to your Spring application that augments an opensource large 
language model with proprietary company data. These are the situations where Spring AI can help.

## Getting Started
The purpose of this project is to dip your toes into the AI waters, and you'll need realistic expectations about performance.

### What about GPUs?
The good news is that you can do this lab without a GPU as long as you have patience. Performance is terrible on my 2019 Intel Macbook Pro, 
but AI response time is tolerable on my M1 MacBook Pro.

[Meta's Lama 3 Requirements](https://llamaimodel.com/requirements/) say you can get by with an NVidia GeForce RTX 3000-series GPU, which has about 12 GB RAM. 
NLP Cloud's article [How to Install and Deploy LLaMA 3 Into Production?](https://nlpcloud.com/how-to-install-and-deploy-llama-3-into-production.html) recommends 20 GB RAM on the GPU, like an NVidia A10, for production use. 

My 10-year-old home lab workstations didn't justify an expensive GPU, so I picked up an Nvidia GeForce 1080 for $120 on eBay, 
and it performs great in my home lab.

## Preconditions

This project demonstrates a few different concepts, and as a result, it has a few prerequisites.

### 1) Set your environment variables.

Before using the commands `docker compose up,` `mvn spring-boot,` or `mvn clean test` in a terminal, you must first export the variables needed by the `docker-compose.yaml` and  `application.yml` files.

Create a new file in the root of the project named `env.sh.` It is already included in the `.gitignore` file. Add these three export statements. We'll talk about the OpenAI API key in a bit.

```shell
export DB_USER=my-postgres-username
export DB_PASSWORD=my-postgres-password
export OPENAI_API_KEY=my-openai-api-key
```

Use the ". ./path" syntax to on Mac or Windows to add the environment variables to the terminal session.

```shell
. ./env.sh
```

To run the application, tests, or Docker Compose from your IDE, add the environment variables to the runtime configuration in your IDE.
This screenshot shows my configuration for the JUnit tests to run inside Intellij. Do the same thing for Application.java if you like.

![IntelliJ Runtime](src/main/resources/static/intellij-runtime.png)

### 2) PostgreSQL

PostgreSQL is used as the vector database for the project. On macOS, run this command from the root of the project to start Postgres. On Linux, the command is `docker-compose up.`

```shell
. ./env.sh
docker compose up -d
```

If you use Intellij, you can right-click the `docker-compose.yaml` YAML file and then select `run.` 

The Docker Compose file uses a volume, so the records and embeddings are preserved between runs.

```yaml
services:
  postgres:
    image: 'pgvector/pgvector:pg16'
    ports:
      - 5432:5432
    restart: always
    shm_size: 128mb
    volumes:
      - db:/var/lib/postgresql/data
    environment:
      - 'POSTGRES_USER=${DB_USER}'
      - 'POSTGRES_DB=airbnb'
      - 'POSTGRES_PASSWORD=${DB_PASSWORD}'
    labels:
      - "org.springframework.boot.services-connection=postgres"
      - '5432'
volumes:
  db:
    driver: local
```

### 3) Ollama

For this lab, we are running LLMs on a Docker image of [Ollama](https://ollama.com/). 

Ollama is an opensource platform for running LLMs (Large Language Models) locally. It makes it easier to get started with AI by hiding the complexities 
of running a LLM (Large Language Model). You can download the [Ollama Docker image](https://hub.docker.com/r/ollama/ollama), or you can [download the binary to your operating system](https://ollama.com/download/). 
Choose the best runtime option for your situation.

Ollama supports many different LLMs. Visit the [Ollama models page](https://ollama.com/library) for the list of supported LLMs, ranked by popularity.

![ollama-models.png](src/main/resources/static/ollama-models.png)

For this example, we will use [Meta's llama 3](https://llama.meta.com/llama3/).

### Launch Ollama and PGVector Together Using Docker Compose
We've taken care of Ollama for you by including it in the Docker Compose file. Run these commands to launch it and then install the llama3 LLM model.

```shell
. ./env.sh
docker compose up -d
docker exec -it ollama ollama run llama3
```

### Launch Ollama as a separate Docker Container
If you choose to run Ollama outside of Docker Compose, follow these commands.

```Shell
docker pull ollama/ollama
docker run -d -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama
docker exec -it ollama ollama run llama3
```

The last command above installs llama3 on your Ollama Docker container and starts an interactive terminal. Spring AI will call the Ollama API, 
but you can test the command line now.

```shell
>>> tell me a joke
Here's one:

Why couldn't the bicycle stand up by itself?

(wait for it...)

Because it was two-tired!

Hope that made you smile! Do you want to hear another?

>>>/bye
```
You can install more than one LLM on Ollama, but I filled up my Docker volume when I tried it on my Intel MacBook. There was no problem running Llama 3
and Minstral on my M1 MacBook. Choose the best runtime option for your situation. 

Confirm that the Ollama API is ready by navigating to [http://localhost:11434](http://localhost:11434) in a web browser. It returns the text 
"Ollama is running."

### 4) OpenIA

In addition to running LLM models locally on Ollama, this project also demonstrates connecting to the OpenAPI key, so create an OpenAI API Key.

## Project Testing

You should now be able to execute the unit test from your IDE, or from the command line. If using the command line, be sure to export the 
environmental variables in your terminal.

```shell
. ./env.sh
mvn clean test
```

## Spring Boot Execution

Spring Boot also needs the environment variables exported to in the terminal or added to your IDE's runtime configuration.

The default Maven profile is "ollama," and the default Spring profile is "llama3," so if that is all you need, then a simple `mvn spring-boot:run` 
is all you need.
```shell
. ./env.sh
mvn spring-boot:run 
```

If you want to use OpenAI, then you must set the Maven profile to "openapi" and Spring profile to "gpt-3.5-turbo."

```shell
. ./env.sh
mvn -Popenai spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=gpt-3.5-turbo"
```

## Spring AI
Okay, now that know how to run everything in the GitHub Project, set up a Spring AI project, starting with Java, Maven and an IDE.

### Java JVM
Grab the latest version of Java. [SDK Man](https://sdkman.io/) is the easiest way to switch around SDK versions.

```Shell
sdk install java 22.0.1-tem
sdk use java 22.0.1-tem
```

### Spring AI Library
Use [Spring Initializr](https://start.spring.io) to create a project with the dependencies shown below:

![start.spring-io.jpeg](src/main/resources/static/start.spring-io.jpeg)

### Set up the Application Properties

A full [application.yml](src%2Fmain%2Fresources%2Fapplication.yml) file is included in this project. A bare-bones properties file for Ollama might look like this.

```yaml
spring:
  application:
    name: spring-ai-airbnb
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        model: llama3
      embedding.model: llama3
```

For OpenAI it might look like this:

```yaml
spring:
  application:
    name: spring-ai-airbnb
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: gpt-4o
```


