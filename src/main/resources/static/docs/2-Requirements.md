# Requirements
[Previous](1-Introduction.md) | [Next](3-Ollama.md)

This project demonstrates a few different concepts, and as a result, it has a few prerequisites.

### 1) Java

This project is using Java 22, but a lower version of Java should work too if you update the Java version in the `pom.xml`
file. [SDK Man](https://sdkman.io/) is the easiest way to switch around SDK versions:

```Shell
sdk install java 22.0.1-tem
sdk use java 22.0.1-tem
```

### 2) Maven

This project was built using Maven 3.9.8, but any recent version of Maven will be fine.

### 3) PostgreSQL

PostgreSQL is used as the vector database for the project. Specifically, we are using the PGVector Docker image that
includes vector database support. PGVector is defined in the `docker-compose.yaml` file at the root of the project.

### 4) Ollama

[Ollama](https://ollama.com) is an opensource platform for running LLMs locally. It makes it easier to get started with AI by hiding
the complexities of running a LLM (Large Language Model). Choose between the [Ollama Docker image](https://hub.docker.com/r/ollama/ollama), or
[downloading the binary to your operating system](https://ollama.com/download/).

Ollama is also defined in the `docker-compose.yaml` file at the root of the project.

```shell
services:
  ollama:
    container_name: ollama
    image: stevecmitchell/ollama-llama31-all-minilm:1.0.1
```

Notice we are not using the standard ollama/ollama image. Instead, we are using a custom image that includes the llama 3.1 model and the all-minilm embedding model.

### 5) OpenAI

In addition to running LLM models locally, this project demonstrates connecting to the OpenAI API. For that, you'll need
to create an OpenAI API Key. You can start with a free OpenAI account. Later, when you bump up against rate limiting,
you can upgrade to a pay-as-you-go account.

[Here is the link to create an OpenAI API key](https://platform.openai.com/settings/profile?tab=api-keys). Be sure to save the API Key somewhere safe when you create it.

### 6) Groq

This project demonstrates connecting to the Groq API too. You'll need to create a Groq API Key.

[Here is the link to create a Groq API key](https://console.Groq.com/keys). Be sure to save the API Key.

### 7) Set your environment variables.

You need to export variables into your terminal or IDE runtime environment. The [docker-compose.yml](docker-compose.yml) and [application.yml](src%2Ftest%2Fresources%2Fapplication.yml)
files both need environment variables to be set.

It's more convenient to run the variable exports inside a script file. Put the export statements in a file named `.env`
at the root of the project, like this:

```shell
export DB_USER=my-postgres-username
export DB_PASSWORD=my-postgres-password
export DB_HOST=localhost
export DATABASE_NAME=airbnb
export OLLAMA_HOST=localhost
export OPENAI_API_KEY=my-openai-api-key
export GROQ_API_KEY=my-grok-api-key
```

Use any credentials you want for Postgres, plus the Groq and OpenAI API keys created above. The `gitignore` file already
contains and entry for a file named `.env.` Use the ". ./.env" syntax on Mac or Linux to add the environment variables
to the terminal session, as shown here:

```shell
. ./.env
```
To run the application, tests, or `Docker Compose` from your IDE, add the environment variables inside the IDE runtime
configurations too. This screenshot shows the configuration for the JUnit tests in Intellij. Do the same
thing for Application.java or Docker if you like.

![IntelliJ Runtime](src/main/resources/static/intellij-runtime.png)

### Launch Ollama and PGVector Together Using Docker Compose for the First Time
Now, you are ready to try out the `Docker Compose` file. Normally, running Spring Boot starts Docker Compose automatically
because of the `spring-boot-docker-compose` library, but we want to do some housekeeping first.

Start `Docker Compose` from the root of the project as shown:

```shell
. ./.env
docker compose up -d
```

Verify that Postgres and Ollama are working by using the `docker compose ps` command.

```shell
docker compose ps

docker compose ps
NAME       IMAGE                                            COMMAND                  SERVICE    CREATED          STATUS          PORTS
ollama     stevecmitchell/ollama-llama31-all-minilm:1.0.1   "/bin/ollama serve"      ollama     19 minutes ago   Up 22 seconds   0.0.0.0:11434->11434/tcp
postgres   pgvector/pgvector:pg16                           "docker-entrypoint.s…"   postgres   2 hours ago      Up 22 seconds   0.0.0.0:5432->5432/tcp
```

Confirm that the Ollama API is ready by navigating to [http://localhost:11434](http://localhost:11434) in a web browser, or with Hppie.

```shell
http :11434
HTTP/1.1 200 OK
Content-Length: 17
Content-Type: text/plain; charset=utf-8
Date: Mon, 26 Aug 2024 19:58:25 GMT

Ollama is running
```

[Previous](1-Introduction.md) |  [Next](3-Ollama.md)