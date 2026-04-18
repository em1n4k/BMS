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

## **How to Run**

### 1. Set environment variables

> **DB_URL = your_database_url**
> **DB_USER = your_username**
> **DB_PASSWORD = your_password**
> **JWT_SECRET = your_secret_key**

### 2. Start database (Docker)

**Make sure Docker is running in the background!**

### 3. Run the application

> **mvn spring-boot:run**

Application will start at: 

> http://localhost:8081

---

## **Testing**

Run tests: 

> **mvn test**

Includes: 

- Liquibase migration test
- PostgreSQL container (Testcontainers)
- Database integration valiation

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