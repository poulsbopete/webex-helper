# Webex AI Assistant Java Application

This Java application provides an AI assistant that uses Elasticsearch semantic search capabilities to provide intelligent responses to user queries about Webex documentation.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Elasticsearch Cloud deployment (with API key and endpoint)

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

3. Add your Elasticsearch configuration to the `.env` file. **Make sure to use `export` for each variable**:
```bash
export ES_HOST=your-elasticsearch-host
export ES_API_KEY=your-elasticsearch-api-key
```
- `ES_HOST` should be your Elasticsearch Cloud endpoint (e.g. `ai-assistants-xxxxxx.es.us-east-1.aws.elastic.cloud`)
- `ES_API_KEY` is your Elasticsearch Cloud API key (not a password)

4. Load the environment variables:
```bash
# For Linux/Mac
source .env

# For Windows (PowerShell)
Get-Content .env | ForEach-Object {
    if ($_ -match '^export ([^=]+)=(.*)$') {
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

Or, if you want to pass environment variables inline (recommended for one-off runs):
```bash
ES_HOST=your-elasticsearch-host ES_API_KEY=your-elasticsearch-api-key java -jar target/ai-assistant-1.0-SNAPSHOT.jar
```

## Usage

1. Start the application
2. Enter your questions about Webex when prompted
3. Type 'exit' to quit the application

## Features

- Elasticsearch semantic search integration for intelligent document retrieval
- Multi-field search across title, body, headings, and semantic text
- Highlighted search results with relevant content snippets
- Professional response formatting with source citations
- Error handling and logging

## How It Works

The application uses Elasticsearch's semantic search capabilities to:
1. Search across multiple fields (title, body, headings, semantic_text)
2. Use boolean queries to combine semantic and keyword matching
3. Return highlighted results with relevant content
4. Format responses with source information and URLs

## Troubleshooting

- **Elasticsearch authentication errors:**
    - Ensure you are using an API key (not a password) for `ES_API_KEY`.
    - Make sure `ES_HOST` is set to your Elasticsearch Cloud endpoint (without protocol, e.g. `ai-assistants-xxxxxx.es.us-east-1.aws.elastic.cloud`).
    - The application uses the API key in the `Authorization: ApiKey ...` header, not as a username/password.
    - If you see `unable to authenticate user [elastic]`, double-check your `.env` values and restart your shell or reload the environment variables.
    - **If you see `ES_API_KEY environment variable is not set`, ensure your `.env` file uses `export` for each variable and that you run `source .env` before starting the app.**

## Project Structure

- `src/main/java/com/webex/helper/config/AppConfig.java` - Configuration and Elasticsearch client initialization
- `src/main/java/com/webex/helper/service/AIAssistantService.java` - Main service logic with semantic search
- `src/main/java/com/webex/helper/Main.java` - Application entry point
- `.env` - Environment variables file (not tracked in git)
- `.gitignore` - Git ignore rules for sensitive and generated files

## Dependencies

- Elasticsearch Java Client
- Jackson for JSON processing
- SLF4J for logging

## Security Notes

- The `.env` file is included in `.gitignore` to prevent accidental commit of sensitive information
- Never commit your actual API keys to version control
- Keep your `.env` file secure and don't share it publicly

## License

[Your License Here] 