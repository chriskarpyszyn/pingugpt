# PinguGpt

PinguGpt is a web-based chat application that integrates with OpenAI's GPT models to provide an interactive chatbot experience. The application is built using Spring Boot and uses HTMX for dynamic content updates.

## Features

- Real-time chat interface
- Integration with OpenAI's GPT models
- Server-Sent Events (SSE) for streaming responses
- In-memory chat history
- Responsive design using Tailwind CSS

## Technology Stack

- Backend: Spring Boot
- Frontend: HTML, JavaScript, HTMX
- CSS: Tailwind CSS
- AI Integration: Spring AI
- Build Tool: Maven (assumed)

## Setup and Configuration

1. Clone the repository
2. Set up your OpenAI API key:
    - Create an environment variable named `REC_OPENAI_API_KEY` with your OpenAI API key
3. Configure the application properties:
    - The `application.properties` file should contain:
      ```
      spring.application.name=pingugpt
      spring.ai.openai.api-key=${REC_OPENAI_API_KEY}
      spring.ai.openai.chat.options.model=gpt-4
      ```

## Running the Application

To run the application:

1. Ensure you have Java and Maven installed
2. Navigate to the project directory
3. Run `mvn spring-boot:run`
4. Open a web browser and go to `http://localhost:8080`

## Usage

1. Upon opening the application, you'll be presented with a chat interface
2. Type your message in the input field at the bottom of the screen
3. Press the send button or hit enter to send your message
4. The AI will process your message and stream the response back to you

## Project Structure

- `PinguGptController.java`: Main controller handling HTTP requests and AI interactions
- `index.html`: Main HTML template for the chat interface
- `application.properties`: Configuration file for Spring Boot and OpenAI integration

## Contributing

Contributions to PinguGpt are welcome! Please feel free to submit a Pull Request.

## License

[Add your chosen license here]

## Acknowledgements

- This project uses [Spring AI](https://docs.spring.io/spring-ai/reference/index.html) for AI integration
- Frontend is enhanced with [HTMX](https://htmx.org/)
- Styling is done with [Tailwind CSS](https://tailwindcss.com/)