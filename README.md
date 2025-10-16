# Parking Management System

A Spring Boot application for managing parking operations with REST APIs, security, and H2 database integration.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd ParkingManagementSystem
```

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Features

- RESTful APIs for parking management system
- Spring Security with basic authentication
- H2 in-memory database
- JPA/Hibernate for data persistence
- Actuator endpoints for monitoring

## API Authentication

All API endpoints require basic authentication:
- **Username**: `admin`
- **Password**: `password`

Protected endpoints under `/api/**` require basic authentication.

## Database Access

H2 Console is available at: `http://localhost:8080/h2-console`

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:PMSDatabase`
- Username: `sa`
- Password: (leave empty)

## Project Structure

```
src/main/java/com/aniket/pms/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── entities/        # JPA entities
├── enums/          # Enum definitions
├── exceptions/     # Custom exceptions
├── model/          # DTOs and models
├── repository/     # Data repositories
├── service/        # Business logic
└── ParkingManagementSystemApp.java
```

## API Endpoints

### Vehicles
- `POST /api/vehicles/create` - Register a new vehicle
- `GET /api/vehicles/{id}` - Get vehicle by ID
- `GET /api/vehicles` - Get all vehicles

### Parking Slots
- `POST /api/slots` - Create a new parking slot
- `GET /api/slots` - List parking slots (with optional filters)

### Parking Operations
- `POST /api/park?vehicleId={id}` - Park a vehicle
- `POST /api/unpark/{ticketId}` - Unpark a vehicle
- `GET /api/tickets/{id}` - Get parking ticket details

## Validation Constraints

- License plates must be unique
- Slot numbers must be unique
- Vehicles can only park in matching slot types (CAR/BIKE/TRUCK)
- Cannot park already parked vehicles
- Cannot unpark already unparked vehicles

## Technologies Used

- Spring Boot 3.1.4
- Spring Security
- Spring Data JPA
- H2 Database
- Lombok
- Maven
- Jakarta Validation
- SLF4J for logging

## Development

### Running Tests
```bash
mvn test
```

### Building JAR
```bash
mvn clean package
```
The JAR file will be created in the `target/` directory.

## API Testing

### Using cURL Collection
Use `pmsApi.json` file which contains:
 `You can import this file in postman for easy test and verifcationjson of PMS application`
- Complete API endpoint documentation
- Sample cURL commands for all operations
- Validation test scenarios
- End-to-end testing workflows

### Sample Commands
```bash
# Create a parking slot
curl -X POST http://localhost:8080/api/slots -u admin:password \
  -H 'Content-Type: application/json' \
  -d '{"slotNumber": "A1", "slotType": "CAR"}'

# Register a vehicle
curl -X POST http://localhost:8080/api/vehicles/create -u admin:password \
  -H 'Content-Type: application/json' \
  -d '{"licensePlate": "ABC123", "ownerName": "aniket", "vehicleType": "CAR"}'

# Park a vehicle
curl -X POST 'http://localhost:8080/api/park?vehicleId={VEHICLE_ID}' -u admin:password
```