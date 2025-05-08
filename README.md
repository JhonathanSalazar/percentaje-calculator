# Percentage Calculator API

This is a Spring Boot application that provides APIs for calculating percentages and retrieving call history with pagination.

## Features

- RESTful API for percentage calculations
- API call history with pagination
- PostgreSQL database for persistent storage
- Caching with Caffeine
- Docker and Docker Compose support
- OpenAPI documentation

## Getting Started

### Prerequisites

- Java 21 or higher
- Docker and Docker Compose
- Gradle (optional, wrapper included)

### Running the Application

#### Using Docker Compose (Recommended)

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd percentaje-calculator
   ```

2. Build and start the application using Docker Compose:
   ```bash
   docker-compose up -d
   ```

   This will:
   - Build the application container
   - Start a Postgresql database
   - Connect the application to the database
   - Expose the application on port 8080

## API Documentation

The API documentation is available via Swagger UI at http://localhost:8080/swagger-ui.html when the application is running.

### Endpoints

#### Percentage Calculator

- **GET /api/v1/percentage/calculate**
  - Calculates a percentage based on two input numbers
  - Parameters:
    - `numA` (required): First number for calculation
    - `numB` (required): Second number for calculation
  - Example: `GET /api/v1/percentage/calculate?numA=100&numB=50`

#### Call History

- **GET /api/v1/call-history**
  - Retrieves a paginated list of API call history records
  - Parameters:
    - `page` (optional, default: 0): Page number (zero-based)
    - `size` (optional, default: 10): Number of items per page
  - Example: `GET /api/v1/call-history?page=0&size=10`

### Understanding Pagination

The Call History API supports pagination to efficiently retrieve large sets of data. The response includes:

- `content`: Array of call history items for the requested page
- `page`: Current page number (zero-based)
- `size`: Number of items per page
- `totalElements`: Total number of records
- `totalPages`: Total number of pages
- `last`: Boolean indicating if this is the last page

#### Example Pagination Response

```json
{
  "data": {
    "content": [
      {
        "id": 1,
        "callDate": "2023-06-01T12:34:56",
        "endpoint": "/api/v1/percentage/calculate",
        "parameters": "numA=100&numB=50",
        "response": "75"
      },
      {
        "id": 2,
        "callDate": "2023-06-01T12:35:10",
        "endpoint": "/api/v1/percentage/calculate",
        "parameters": "numA=200&numB=25",
        "response": "250"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 42,
    "totalPages": 5,
    "last": false
  }
}
```

#### Pagination Testing in Postman

To test pagination in Postman:

1. **Basic Request**: `GET http://localhost:8080/api/v1/call-history`
   - This returns the first page with 10 items

2. **Custom Page and Size**: `GET http://localhost:8080/api/v1/call-history?page=1&size=5`
   - This returns the second page with 5 items per page

3. **Last Page**: Use the `totalPages` value from a previous response
   - `GET http://localhost:8080/api/v1/call-history?page=<totalPages-1>`

4. **Verify Pagination**: Check that:
   - The correct number of items is returned in the `content` array
   - The `page` and `size` values match your request
   - The `totalElements` and `totalPages` values are consistent
   - The `last` flag correctly indicates if you're on the last page

## Configuration

The application can be configured through the `application.properties` file:

- `percentage-api.url`: URL for the external percentage API
- `spring.cache.caffeine.spec`: Cache configuration
- Database connection settings
- Async task execution pool settings