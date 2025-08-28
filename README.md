# Google Docs MCP (Model Context Protocol) Server

A full-stack application that provides Google Docs integration through MCP (Model Context Protocol) with a React client interface.

## Project Structure

- **`mcp/`** - Spring Boot Java server that implements MCP tools for Google Docs operations
- **`client/`** - React frontend with Google OAuth authentication

## Features

### MCP Server (Java/Spring Boot)
- **Create Document** - Create new Google Docs with specified titles
- **Read Document** - Retrieve text content from existing Google Docs
- **Append to Document** - Add text to existing Google Docs
- **YouTube Transcript** - Extract transcripts from YouTube videos

### Client (React)
- Google OAuth authentication
- User dashboard interface
- Modern UI with Vite build system

## Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- Maven
- Google Cloud Project with Docs API enabled

### Running the MCP Server
```bash
cd mcp
./mvnw spring-boot:run
```

### Running the Client
```bash
cd client
npm install
npm run dev
```

## Configuration

The MCP server requires Google API credentials. Update the authentication tokens in `GoogleDocsService.java` or use environment variables for production.

## Tech Stack

- **Backend**: Spring Boot 3.5, Spring AI MCP Server, Google APIs
- **Frontend**: React 19, Vite, React Router, Google OAuth
- **Build**: Maven (backend), npm/Vite (frontend)

## API Endpoints

The MCP server exposes the following tools:
- `create_doc(title)` - Creates a new Google Doc
- `read_doc(docId)` - Reads content from a Google Doc
- `append_to_doc(docId, text)` - Appends text to a Google Doc
- `get_transcript(videoUrl)` - Gets YouTube video transcript
