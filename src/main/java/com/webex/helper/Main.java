package com.webex.helper;

import com.webex.helper.config.AppConfig;
import com.webex.helper.service.AIAssistantService;

import java.util.List;
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
            System.out.println("\nCommands:");
            System.out.println("- Type 'languages' to see available languages");
            System.out.println("- Type 'regions' to see available regions");
            System.out.println("- Type 'help' to see this help message");
            System.out.println("- Use format: 'language:en region:us question' to filter results");
            System.out.println("- Example: 'language:en region:us How do I schedule a meeting?'");

            while (true) {
                System.out.print("\nEnter your question: ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                if (input.isEmpty()) {
                    continue;
                }

                if (input.equalsIgnoreCase("help")) {
                    System.out.println("\nCommands:");
                    System.out.println("- Type 'languages' to see available languages");
                    System.out.println("- Type 'regions' to see available regions");
                    System.out.println("- Type 'help' to see this help message");
                    System.out.println("- Use format: 'language:en region:us question' to filter results");
                    System.out.println("- Example: 'language:en region:us How do I schedule a meeting?'");
                    continue;
                }

                if (input.equalsIgnoreCase("languages")) {
                    try {
                        List<String> languages = assistant.getAvailableLanguages();
                        System.out.println("\nAvailable languages:");
                        for (String lang : languages) {
                            System.out.println("- " + lang);
                        }
                    } catch (Exception e) {
                        System.err.println("Error getting languages: " + e.getMessage());
                    }
                    continue;
                }

                if (input.equalsIgnoreCase("regions")) {
                    try {
                        List<String> regions = assistant.getAvailableRegions();
                        System.out.println("\nAvailable regions:");
                        for (String region : regions) {
                            System.out.println("- " + region);
                        }
                    } catch (Exception e) {
                        System.err.println("Error getting regions: " + e.getMessage());
                    }
                    continue;
                }

                // Parse input for language and region filters
                String language = null;
                String region = null;
                String question = input;

                // Check for language filter
                if (input.toLowerCase().startsWith("language:")) {
                    int langEnd = input.indexOf(" ", 9); // "language:" is 9 characters
                    if (langEnd != -1) {
                        language = input.substring(9, langEnd).trim();
                        question = input.substring(langEnd + 1).trim();
                    }
                }

                // Check for region filter
                if (question.toLowerCase().startsWith("region:")) {
                    int regionEnd = question.indexOf(" ", 7); // "region:" is 7 characters
                    if (regionEnd != -1) {
                        region = question.substring(7, regionEnd).trim();
                        question = question.substring(regionEnd + 1).trim();
                    }
                }

                // Process the question and get response
                String response = assistant.processQuery(question, language, region);
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