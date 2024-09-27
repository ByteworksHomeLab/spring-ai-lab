# Ollama
[Previous](2-Requirements.md) | [Next](4-Spring-AI.md)

In the [Requirements](2-Requirements.md) section, you launched Docker Compose with Ollama. It is running my custom image, 
`stevecmitchell/ollama-llama31-all-minilm:1.0.1`. My custom image already has the Llama 3.1 LLM installed along with the `all-minilm` embedding model.

The following steps discuss how that image was build using the standard `ollama/ollama` image from Docker Hub.

First, install an embedding model using the `ollama` CLI installed on the Ollama Docker container.

```shell
docker exec -it ollama ollama pull all-minilm
```

Next, install the LLama3 LLM

```shell
docker exec -it ollama ollama run llama3
```

The first time you issue the Ollama `run` command it downloads and installs the LLM. Your terminal session may time out during the installation, but the LLM was probably successfully installed.
Reconnect to the Ollama Docker container to rerun the `ollama run llama3` command. The Ollama prompt should return quickly since the llama3 LLM is already installed. Try asking it a question.

```shell
docker exec -it ollama ollama run llama3

>>> You are a newly installed LLM. Please tell me your version in six lines or less.
I’m excited to share!
My version is Meta AI’s LLaMA, the latest Large Language Model.
I’m built on top of the Transformers library and trained on a massive dataset of text from the internet.
My training data includes a wide range of texts, from books and articles to social media posts and more.
I’ve been fine-tuned for conversational dialogue and can understand and respond to natural language inputs.
I’m still learning and improving every day, but I’m ready to chat with you!
>>> /bye
```

These are all the commands for the Ollama CLI:
```
Available Commands:
  serve       Start ollama
  create      Create a model from a Modelfile
  show        Show information for a model
  run         Run a model
  pull        Pull a model from a registry
  push        Push a model to a registry
  list        List models
  ps          List running models
  cp          Copy a model
  rm          Remove a model
  help        Help about any command
```

In the steps above, you pulled an embedding model, `all-minilm`. Ollama supports [three embedding models](https://ollama.com/blog/embedding-models).

| Embedding Model                                                   | Parameters   |
|-------------------------------------------------------------------|--------------|
| [mxbai-embed-large](https://ollama.com/library/mxbai-embed-large) | 334M         |
| [nomic-embed-text](https://ollama.com/library/nomic-embed-text)   | 137M         |
| [all-minilm](https://ollama.com/library/all-minilm)               | 23M          |

Whenever you change the embedding model, follow these steps. We've already made these changes for the `all-minilm` embedding model.

1) __Pull the Embedding Model__—Use the Ollama CLI to pull down the embedding model that you select. For instance, `docker exec -it ollama ollama pull all-minilm`.
2) __Update the Ollama Embedding Model Property__—Update `spring.ai.ollama.embedding.model` to specify the new embedding model.
3) __Update the Vector Store Dimensions__—Update `spring.ai.vectorstore.pgvector.dimensions` to match the new embedding model.
4) __Drop the Vector Storage Table__—Run `DROP TABLE` on the vector storage table.

#### 1) Pull the Embedding Model
Here is a detailed review of the steps above for your future reference.

Use the `ollama pull` command to pull down the embedding model.

```shell
docker exec -it ollama ollama pull all-minilm
```

#### 2) Update the Ollama Embedding Model Property—Update
Update `spring.ai.ollama.embedding.model` to specify the new embedding model.

```yaml
spring:
  ai:
    ollama:
      base-url: http://${OLLAMA_HOST}:11434
      chat:
        options:
          model: llama3
      embedding:
        enabled: true
        model: all-minilm
```

### 3) Update the Vector Store Dimensions
Update `spring.ai.vectorstore.pgvector.dimensions` to match the new embedding model. If it doesn't match, you will get
an error. That is how I knew what dimensions to use (384 dimensions) after switching the embedding model to `all-minilm.`

```yaml
spring:
  ai:
    vectorstore:
      pgvector:
        index-type: hnsw
        dimensions: 384
        distance-type: cosine_distance
```

### 4) Drop the Vector Storage Table
Connect to the airbnb database in PostgreSQL and locate the `vector_table`, then drop it.

```sql
DROP TABLE vector_store;
```

You don't need to recreate the vector storage table. Spring Boot AI recreates the vector database the next time you
do a `mvn spring-boot-run` based on the application properties above.

[Previous](2-Requirements.md) | [Next](4-Spring-AI.md)