# Spring AI Lab
#### https://github.com/ByteworksHomeLab/spring-ai-lab
This project is an introduction to Spring AI. It demonstrates running two LLMs, Llama 3 on the Ollama platform and OpenAI, using a combination of Maven profiles and Spring profiles to switch back and forth.
## Audience
The audience or this project is Spring Framework application developers who are new to artificial intelligence. It is expected that you know your way around the Spring Framework, but we don't assume any AI experience.

The main takeaway is that as a Spring developer, you can start using AI in your Spring applications today as easily as you use the other Spring Framework libraries.
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

### 1) PostgreSQL

PostgreSQL is used as the vector database for the project. To run the project, install PostgreSQL with the PGVector extension.

Next, create two databases:
- __airbnb__—This is the database the REST API. You connect to the `airbnb` database when running `mvn spring-boot:run`.
- __airbnb_test__—Since the Airbnb Listing model uses a vector column, and the H2 database doesn't support the vector column type, this project uses PostgreSQL for Junit tests too. The Junit tests connect to the `airbnb_test` database.

You'll need to set up the username and password for the Airbnb databases. I used Homebrew to install PostgreSQL on my Macbook, 
so it used my MaxOS username. Normally, the username is "postgres." 

![PostgreSQL Databases](src/main/resources/static/postgresdbs.png)

To test the Postgres credentials, as shown above, I added connections to the Intellij Database console for both the `airbnb` and `airbnb_test` databases. 

#### Load the "Prod" Schema
The last piece of the PostgreSQL set up is to load the schema found in [src/main/resources/data/schema.sql](src%2Fmain%2Fresources%2Fdata%2Fpostgres-schema.ddl) 
into the `airbnb` database. Don't worry about the schema for the `airbnb_test` database. Spring Test takes care of that for you.

### 2) Ollama

For this lab, we are running LLMs on a Docker image of [Ollama](https://ollama.com/).

Ollama is an opensource platform for running LLMs (Large Language Models) locally. It makes it easier to get started with AI by hiding the complexities 
of running a LLM (Large Language Model). You can download the [Ollama Docker image](https://hub.docker.com/r/ollama/ollama), or you can [download the binary to your operating system](https://ollama.com/download/). 
Choose the best runtime option for your situation.

Ollama supports many different LLMs. Visit the [Ollama models page](https://ollama.com/library) for the list of supported LLMs, ranked by popularity.

![ollama-models.png](src/main/resources/static/ollama-models.png)

For this example, we will use [Meta's llama 3](https://llama.meta.com/llama3/).

Run these commands to get started.

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

### 3) OpenIA

In addition to running LLM models locally on Ollama, this project also demonstrates connecting to the OpenAPI key, so create an OpenAI API Key.

### 4) Set your environment variables.

Before using the command `mvn spring-boot run` in a terminal, you must export the variables needed by the `application.yml` file:

```shell
export DB_USER=my-postgres-username
export DB_PASSWORD=my-postgres-password
export OPENAI_API_KEY=my-openai-api-key
```

Likewise, to run the JUnit tests or to run the application from your IDE, add the environment variables to the runtime configuration in your IDE. 
This screenshot shows my configuration for the JUnit tests to run inside Intellij. Do the same thing for Application.java if you like.

![IntelliJ Runtime](src/main/resources/static/intellij-runtime.png)

## Project Testing

You should now be able to execute the unit test from your IDE, or from the command line. If using the command line, be sure to export the 
environmental variables in your terminal.

```shell
export DB_USER=my-postgres-username
export DB_PASSWORD=my-postgres-password
export OPENAI_API_KEY=my-openai-api-key

mvn clean test
```

## Spring Boot Execution

Spring Boot also needs the environment variables exported to in the terminal or added to your IDE's runtime configuration.

The default Maven profile is "ollama," and the default Spring profile is "llama3," so if that is all you need, then a simple `mvn spring-boot:run` 
is all you need.
```shell
# Default Maven profile is "ollama" and the default Spring profile is "llama3"
mvn spring-boot:run 
```

If you want to use OpenAI, then you must set the Maven profile to "openapi" and Spring profile to "gpt-40."

```shell
# Sets Maven profile to "openapi" and spring profile to "gpt-40"
mvn -Popenai -spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=gpt-4o"
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


