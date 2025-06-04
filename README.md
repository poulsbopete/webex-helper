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

2. Create a `.env` file in the project root directory:
```bash
touch .env
```

3. Add your API keys to the `.env` file:
```bash
ES_API_KEY=your-elasticsearch-api-key
OPENAI_API_KEY=your-openai-api-key
```

4. Load the environment variables:
```bash
# For Linux/Mac
source .env

# For Windows (PowerShell)
Get-Content .env | ForEach-Object {
    if ($_ -match '^([^=]+)=(.*)$') {
        $name = $matches[1]
        $value = $matches[2]
        [Environment]::SetEnvironmentVariable($name, $value, 'Process')
    }
}
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
- `.env` - Environment variables file (not tracked in git)
- `.gitignore` - Git ignore rules for sensitive and generated files

## Dependencies

- Elasticsearch Java Client
- OpenAI Java Client
- Jackson for JSON processing
- SLF4J for logging

## Security Notes

- The `.env` file is included in `.gitignore` to prevent accidental commit of sensitive information
- Never commit your actual API keys to version control
- Keep your `.env` file secure and don't share it publicly

## License

[Your License Here] 