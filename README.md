# **Bank Cards Management System (BMS)**

## **Description:** 

RESTful API backend application for managing bank cards, users and transfers.

The system provides secure authentication, role-based access control and operations with bank cards such as creation, blocking and transfers between user accounts.

---

## **Tech Stack**

- Java 21
- Spring Boot
- Spring Security + JWT 
- PostgreSQL
- Liquibase
- Docker
- Testcontainers
- Maven 

---

## **Features**

- User authentication with JWT
- Role-based access (ADMIN / USER)
- Card management (create, block, activate, delete)
- Transfer between user's own cards
- Pagination and filtering 
- Global exception handling
- Database migrations with Liquibase
- Integration testing with Testcontainers

---

## API Documentation

Swagger UI is available after application startup:

http://localhost:8081/swagger-ui/index.html

OpenAPI JSON:

http://localhost:8081/v3/api-docs

### Authorization

Protected endpoints require JWT token.

Steps:
1. Call `POST /auth/login`
2. Copy returned token
3. Click **Authorize** in Swagger UI
4. Paste token (Bearer will be added automatically)

---

## Main API Flow

1. Register user  
   `POST /auth/register`

2. Login and receive JWT token  
   `POST /auth/login`

3. Authorize in Swagger UI

4. Create card (ADMIN only)  
   `POST /cards`

5. Get all cards  
   `GET /cards`

6. Transfer between cards  
   `POST /cards/transfer`

---

## **How to Run**

### 1. Set environment variables

> **DB_URL = your_database_url**

> **DB_USER = your_username**

> **DB_PASSWORD = your_password**

> **JWT_SECRET = your_secret_key**

### 2. Start database (Docker)

**Make sure Docker is running in the background!**
> **docker-compose up -d**

### 3. Run the application

> **mvn spring-boot:run**

Application will start at: 

> http://localhost:8081

### JWT Secret Generation

JWT secret must be at least **256 bits (32+ bytes)**.

Generate using OpenSSL (Git Bash / Linux / Mac):

bash
> **openssl rand -base64 125**

---

## **Testing**

Run tests: 

> **mvn test**

Includes: 

- Liquibase migration test
- PostgreSQL container (Testcontainers)
- Database integration validation

---

## **Security**

- JWT-based authentication
- Role-based authorization
- Card number masking 
- Users can only access their own cards

--- 

## **Notes**

- Transfers are allowed only between user's personal cards
- Card numbers are stored securely and returned in masked format
- Liquibase manages all database schema changes