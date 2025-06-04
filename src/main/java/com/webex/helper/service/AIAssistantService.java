package com.webex.helper.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AIAssistantService {
    private static final Logger logger = LoggerFactory.getLogger(AIAssistantService.class);
    private static final String INDEX_NAME = "search-webex-help";
    private static final int MAX_RESULTS = 3;

    private final ElasticsearchClient esClient;
    private final OpenAiService openAiService;

    public AIAssistantService(ElasticsearchClient esClient, OpenAiService openAiService) {
        this.esClient = esClient;
        this.openAiService = openAiService;
    }

    public String processQuery(String question) {
        try {
            // Get results from Elasticsearch
            List<Map<String, Object>> searchResults = getElasticsearchResults(question);
            
            // Create context from search results
            String context = createContextFromResults(searchResults);
            
            // Generate response using OpenAI
            return generateOpenAIResponse(context, question);
        } catch (Exception e) {
            logger.error("Error processing query", e);
            return "I apologize, but I encountered an error while processing your request.";
        }
    }

    private List<Map<String, Object>> getElasticsearchResults(String query) throws Exception {
        SearchResponse<Map> response = esClient.search(s -> s
                .index(INDEX_NAME)
                .size(MAX_RESULTS)
                .query(q -> q
                        .multiMatch(m -> m
                                .query(query)
                                .fields("title")
                        )
                ),
                Map.class
        );

        List<Map<String, Object>> results = new ArrayList<>();
        for (Hit<Map> hit : response.hits().hits()) {
            results.add(hit.source());
        }
        return results;
    }

    private String createContextFromResults(List<Map<String, Object>> results) {
        StringBuilder context = new StringBuilder();
        for (Map<String, Object> result : results) {
            // Add relevant fields to context
            if (result.containsKey("title")) {
                context.append("Title: ").append(result.get("title")).append("\n");
            }
            if (result.containsKey("body")) {
                context.append("Body: ").append(result.get("body")).append("\n");
            }
            if (result.containsKey("meta_description")) {
                context.append("Description: ").append(result.get("meta_description")).append("\n");
            }
            context.append("---\n");
        }
        return context.toString();
    }

    private String generateOpenAIResponse(String context, String question) {
        String systemPrompt = """
            Instructions:
            
            - You are a senior solutions architect at a cloud company. Provide concise, step-by-step technical answers. Prioritize production-safe recommendations. For YAML, JSON, or CLI, output only the relevant snippet. If the answer isn't certain, say so and suggest next steps. Tone: professional, approachable, slightly nerdy.
            - Answer questions truthfully and factually using only the context presented.
            - If you don't know the answer, just say that you don't know, don't make up an answer.
            - You must always cite the document where the answer was extracted using inline academic citation style [], using the position.
            - Use markdown format for code examples.
            - You are correct, factual, precise, and reliable.
            
            Context:
            """ + context;

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemPrompt));
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), question));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .build();

        return openAiService.createChatCompletion(request)
                .getChoices().get(0).getMessage().getContent();
    }
} 