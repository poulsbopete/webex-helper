package com.webex.helper.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
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

    public AIAssistantService(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    public String processQuery(String question) {
        try {
            // Get results from Elasticsearch using enhanced search
            List<Map<String, Object>> searchResults = getElasticsearchResults(question);
            
            // Create response from search results
            return createResponseFromResults(searchResults, question);
        } catch (Exception e) {
            logger.error("Error processing query", e);
            return "I apologize, but I encountered an error while processing your request.";
        }
    }

    private List<Map<String, Object>> getElasticsearchResults(String query) throws Exception {
        // Create the enhanced search query with multiple field matching
        SearchResponse<Map> response = esClient.search(s -> s
                .index(INDEX_NAME)
                .size(MAX_RESULTS)
                .query(q -> q
                        .bool(b -> b
                                .should(sh -> sh
                                        .match(m -> m
                                                .field("semantic_text")
                                                .query(query)
                                        )
                                )
                                .should(sh -> sh
                                        .multiMatch(m -> m
                                                .query(query)
                                                .fields("body", "headings", "title")
                                        )
                                )
                        )
                )
                .highlight(h -> h
                        .fields("body", f -> f
                                .numberOfFragments(2)
                                .fragmentSize(150)
                        )
                        .fields("headings", f -> f
                                .numberOfFragments(1)
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

    private String createResponseFromResults(List<Map<String, Object>> results, String question) {
        if (results.isEmpty()) {
            return "I couldn't find any relevant information to answer your question about '" + question + "'. Please try rephrasing your question or ask about a different topic.";
        }

        StringBuilder response = new StringBuilder();
        response.append("Based on the search results, here's what I found:\n\n");

        for (int i = 0; i < results.size(); i++) {
            Map<String, Object> result = results.get(i);
            response.append("**Result ").append(i + 1).append(":**\n");
            
            // Add title if available
            if (result.containsKey("title")) {
                response.append("**Title:** ").append(result.get("title")).append("\n");
            }
            
            // Add meta description if available
            if (result.containsKey("meta_description")) {
                response.append("**Description:** ").append(result.get("meta_description")).append("\n");
            }
            
            // Add URL if available
            if (result.containsKey("url")) {
                response.append("**Source:** ").append(result.get("url")).append("\n");
            }
            
            // Add body content (truncated if too long)
            if (result.containsKey("body")) {
                String body = (String) result.get("body");
                if (body != null && !body.isEmpty()) {
                    String truncatedBody = body.length() > 300 ? body.substring(0, 300) + "..." : body;
                    response.append("**Content:** ").append(truncatedBody).append("\n");
                }
            }
            
            response.append("\n---\n\n");
        }

        response.append("**Note:** These results are based on semantic search through the Webex documentation. ");
        response.append("For the most up-to-date and detailed information, please refer to the official Webex documentation.");

        return response.toString();
    }
} 