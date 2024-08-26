package com.ahead.airbnb;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.List;

@TestConfiguration(proxyBeanMethods = false)
public class ContainersConfiguration {

	private static final Logger log = LoggerFactory.getLogger(ContainersConfiguration.class);

	private static final String OLLAMA_LLAMA3_EMBEDDING = "ollama_llama3_embedding";

	@Bean
	@ServiceConnection("ollama")
	@RestartScope
	OllamaContainer ollama() throws IOException, InterruptedException {
		// See https://gist.github.com/glaforge/dd772c31bf7e40a35bc22658924b43eb
		List<Image> listImagesCmd;
		try (DockerClient dockerClient = DockerClientFactory.lazyClient()) {
			listImagesCmd = dockerClient.listImagesCmd().withImageNameFilter(OLLAMA_LLAMA3_EMBEDDING).exec();

			if (listImagesCmd.isEmpty()) {
				log.info("Creating a new Ollama container with Llama 3.1 and all-minilm image...");
				OllamaContainer ollama = new OllamaContainer(
						"docker pull ghcr.io/thomasvitale/ollama-llama3-1:sha256-c74ca94fd32e2c72cfb016ac3766a3803fae9992f94410caa7c2477131a60da3");
				log.info("Starting Ollama container...");
				ollama.start();
				log.info("Pulling all-minilm embedding model...");
				ollama.execInContainer("ollama", "pull", "all-minilm");
				log.info("Committing Ollama container with Llama 3.1 and all-minilm image...");
				ollama.commitToImage(OLLAMA_LLAMA3_EMBEDDING);
				return ollama;
			}
			else {
				log.info("Using existing Ollama container with Llama 3.1 and all-minilm image...");
				return new OllamaContainer(
						DockerImageName.parse(OLLAMA_LLAMA3_EMBEDDING).asCompatibleSubstituteFor("ollama/ollama"));
			}
		}
	}

	@Bean
	@ServiceConnection("postgres")
	@RestartScope
	PostgreSQLContainer<?> postgres() {
		return new PostgreSQLContainer<>("pgvector/pgvector:pg16");
	}

}
