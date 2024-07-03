# Spring AI Lab
The project is based on work by [DaShaun Carter](linkedin.com/in/dashaun) See the DaShaun's
video ["A Tiny Taste of Spring AI with Ollama"](https://www.linkedin.com/video/live/urn:li:ugcPost:7213736555662753792/). Also see [Dan Vega's AI Workshop](https://github.com/danvega/spring-ai-workshop).

## Getting Started
The purpose of this project is to dip your toes into the AI waters, and you'll need realistic expectations about performance. 

### What about GPUs?
The good news is that you can do this lab without a GPU as long as you have patience. 

[Meta's Lama 3 Requirements](https://llamaimodel.com/requirements/) say you can get by with an NVidia GeForce RTX 3000 series GPU, which has about 12 GB RAM. NLP Cloud's article [How to Install and Deploy LLaMA 3 Into Production?](https://nlpcloud.com/how-to-install-and-deploy-llama-3-into-production.html) recommends 20 GB RAM on the GPU, like an NVidia A10, for production use.

### Ollama LLM Platform
For this lab we are running LLMs on a Docker image of [Ollama](https://ollama.com/). Ollama is not meant to be used in production. 

Ollama is an open source platform for running LLMs (Large Language Models) locally. It makes it easier to get started with AI by hiding the complexities of running an LLM.  Choose the best runtime option for your situation. You can download the [Ollama Docker image](https://hub.docker.com/r/ollama/ollama), or you can [download the binary to your operating system](https://ollama.com/download/).

Ollama supports many different LLMs. Visit the [Ollama models page](https://ollama.com/library) for the list of supported LLMs, ranked by popularity. 

![ollama-models.png](src/main/resources/static/ollama-models.png)

For this example, we will use [Meta's llama 3](https://llama.meta.com/llama3/).

Run these commands to get started.

```Shell
docker pull ollama/ollama
docker run -d -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama
docker exec -it ollama ollama run llama3
```

The last command above installs llama3 on your Ollama Docker container and starts an interactive terminal. Spring AI will call the Ollama API, but you can test the command line now.

```shell
>>> tell me a joke
Here's one:

Why couldn't the bicycle stand up by itself?

(wait for it...)

Because it was two-tired!

Hope that made you smile! Do you want to hear another?

>>>/bye
```
You can install more than one LLM on Ollama, but I filled up my Docker volume when I tried.

```shell
docker exec -it ollama ollama run mixtral:8x22b
pulling manifest
pulling d0eeef8264ce...  61% ▕███████████████████████████████████                       ▏  48 GB/ 79 GB   27 MB/s  18m36s
Error: write /root/.ollama/models/blobs/sha256-d0eeef8264ce10a7e578789ee69986c66425639e72c9855e36a0345c230918c9-partial: no space left on device
```

## Spring AI
Next, set up a Spring AI project, starting with Java, Maven and an IDE.

### Java JVM
Grab the latest version of Java. [SDK Man](https://sdkman.io/) is the easiest way to switch around SDK versions.

```Shell
sdk install java 22.0.1-tem
sdk use java 22.0.1-tem
```

### Spring AI Library
Use [Spring Initializr](https://start.spring.io) to create a project with the dependencies shown below:

![start.spring-io.jpeg](src/main/resources/static/start.spring-io.jpeg)
