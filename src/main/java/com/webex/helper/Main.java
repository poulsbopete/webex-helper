package com.webex.helper;

import com.webex.helper.config.AppConfig;
import com.webex.helper.service.AIAssistantService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize configuration and services
            AppConfig config = new AppConfig();
            AIAssistantService assistant = new AIAssistantService(
                    config.getEsClient()
            );

            // Create scanner for user input
            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the Webex AI Assistant! Type 'exit' to quit.");
            System.out.println("This assistant uses Elasticsearch semantic search to find relevant information.");

            while (true) {
                System.out.print("\nEnter your question: ");
                String question = scanner.nextLine().trim();

                if (question.equalsIgnoreCase("exit")) {
                    break;
                }

                if (question.isEmpty()) {
                    continue;
                }

                // Process the question and get response
                String response = assistant.processQuery(question);
                System.out.println("\nResponse:");
                System.out.println(response);
            }

            scanner.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 