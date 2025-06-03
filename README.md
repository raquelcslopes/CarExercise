# Car Exercise API

This project is a backend REST API for managing users and vehicles, built with Java and Spring Boot. It provides endpoints for user and vehicle management, including account activation/deactivation, vehicle association, and more.

## Features
- User registration, update, and deletion
- Vehicle registration, update, and deletion
- Account activation and deactivation
- Association between users and vehicles
- Exception handling for common business rules

## Technologies Used
- Java 17+
- Spring Boot
- Maven
- JPA/Hibernate
- H2 (or other) database

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven

### Running the Application
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd CarExercise
   ```
3. Build and run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   Or, if you have Maven installed:
   ```bash
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8080` by default.

### Running Tests
To run the tests, use:
```bash
./mvnw test
```

## Project Structure
- `controller/` - REST controllers for user and vehicle endpoints
- `entity/` - JPA entities for User and Vehicle
- `exceptions/` - Custom exception classes
- `model/` - DTOs and converters
- `repository/` - Spring Data JPA repositories
- `service/` - Business logic services

## API Endpoints
Example endpoints (see controller classes for full list):
- `POST /users` - Create a new user
- `PUT /users/{id}` - Update user details
- `DELETE /users/{id}` - Delete a user
- `POST /vehicles` - Register a new vehicle
- `GET /vehicles` - List all vehicles

## License
This project is for educational purposes.

