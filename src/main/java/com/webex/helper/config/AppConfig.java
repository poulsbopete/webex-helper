package com.webex.helper.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.theokanning.openai.service.OpenAiService;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class AppConfig {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private static final String ES_HOST = System.getenv("ES_HOST");
    private static final int ES_PORT = 443;
    private static final String ES_SCHEME = "https";

    private final ElasticsearchClient esClient;
    private final OpenAiService openAiService;

    public AppConfig() {
        this.esClient = createElasticsearchClient();
        this.openAiService = createOpenAiService();
    }

    private ElasticsearchClient createElasticsearchClient() {
        try {
            String apiKey = System.getenv("ES_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new IllegalStateException("ES_API_KEY environment variable is not set");
            }
            if (ES_HOST == null || ES_HOST.isEmpty()) {
                throw new IllegalStateException("ES_HOST environment variable is not set");
            }

            // Create the low-level client
            RestClient restClient = RestClient.builder(
                    new HttpHost(ES_HOST, ES_PORT, ES_SCHEME))
                    .setDefaultHeaders(new BasicHeader[]{
                            new BasicHeader("Authorization", "ApiKey " + apiKey)
                    })
                    .build();

            // Create the transport with a Jackson mapper
            ElasticsearchTransport transport = new RestClientTransport(
                    restClient, new JacksonJsonpMapper());

            // Create the API client
            return new ElasticsearchClient(transport);
        } catch (Exception e) {
            logger.error("Failed to create Elasticsearch client", e);
            throw new RuntimeException("Failed to create Elasticsearch client", e);
        }
    }

    private OpenAiService createOpenAiService() {
        try {
            String apiKey = System.getenv("OPENAI_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new IllegalStateException("OPENAI_API_KEY environment variable is not set");
            }
            return new OpenAiService(apiKey, Duration.ofSeconds(30));
        } catch (Exception e) {
            logger.error("Failed to create OpenAI service", e);
            throw new RuntimeException("Failed to create OpenAI service", e);
        }
    }

    public ElasticsearchClient getEsClient() {
        return esClient;
    }

    public OpenAiService getOpenAiService() {
        return openAiService;
    }
} 