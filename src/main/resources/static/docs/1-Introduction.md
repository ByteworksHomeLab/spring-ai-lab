# Introduction
[Previous](..%2F..%2F..%2F..%2F..%2FREADME.md) | [Next](2-Requirements.md)

## Audience
The audience or this project is Spring Framework application developers who are new to artificial intelligence. It is
expected that you know your way around the Spring Framework, but we don't assume any AI experience.

## Overview

This project is an introduction to Spring AI. It demonstrates running three LLMs (Large Language Models):
- Llama3 locally on the Ollama platform
- Groq in the cloud
- ChatGPT in the cloud

The project uses a combination of Maven profiles and Spring profiles to switch between the LLMs without having to change
any Java code.

__At the end of this README file, you'll learn how to get up and running with OpenAI in less than 15 minutes__.

## Use Cases
Your employer asks you to:
- Improve your Spring Customer Service App by combining the company's Knowledge base with a natural language AI interface.
- Help Sales and Marketing by enhances your Spring sales application with natural language searching of company marketing material.
- Improve coding efficiency and consistency by embedding existing company code repositories with AI for code generation.
- Host a private LLM so employees can use take advantage of AI internally, without transmitting sensitive company or client data over the public internet.

You probably see a trend here. As a Spring Developer, you may be asked to add AI to your Spring application that augments
an opensource large language model with proprietary company data. These are the situations where Spring AI can help.

## Getting Started
The purpose of this project is to dip your toes into the AI waters. We'll demonstrate using Spring AI calling OpenAI and
Groq via their respective APIs, so you'll need to sign up for free API keys from both. Also, we'll install Ollama to run
the Llama3 LLM locally. Ollama is to LLMs what VirtualBox is to virtual machines or Docker Desktop to containers, but instead
of running virtual machines or containers, Ollama runs LLMs. One caveat is that you'll need realistic expectations about local LLM performance.

### What about GPUs?
AI does lots of parallel vector math, and GPUs handle lots of parallel processes to render displays, which is why they go well together.
The good news is that you can do this lab without a GPU as long as you have patience, although, for longer term use, you'll want a GPU.
Ollama supports Nvidia and AMD GPUs. See the [Ollama GPU Documentation page](https://github.com/ollama/ollama/blob/main/docs/gpu.md).

[Previous](..%2F..%2F..%2F..%2F..%2FREADME.md) | [Next](2-Requirements.md)
