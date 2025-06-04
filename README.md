# AI Assistant Java Application

This Java application provides an AI assistant that combines Elasticsearch vector database capabilities with OpenAI's GPT model to provide intelligent responses to user queries.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Elasticsearch API key
- OpenAI API key

## Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd ai-assistant
```

2. Set up environment variables:
```bash
export ES_API_KEY="your-elasticsearch-api-key"
export OPENAI_API_KEY="your-openai-api-key"
```

## Building the Application

Build the application using Maven:
```bash
mvn clean package
```

## Running the Application

Run the application using:
```bash
java -jar target/ai-assistant-1.0-SNAPSHOT.jar
```

## Usage

1. Start the application
2. Enter your questions when prompted
3. Type 'exit' to quit the application

## Features

- Elasticsearch integration for semantic search
- OpenAI GPT-3.5 Turbo integration for natural language processing
- Context-aware responses based on search results
- Professional and technical response formatting
- Error handling and logging

## Project Structure

- `src/main/java/com/webex/helper/config/AppConfig.java` - Configuration and client initialization
- `src/main/java/com/webex/helper/service/AIAssistantService.java` - Main service logic
- `src/main/java/com/webex/helper/Main.java` - Application entry point

## Dependencies

- Elasticsearch Java Client
- OpenAI Java Client
- Jackson for JSON processing
- SLF4J for logging

## License

[Your License Here] 