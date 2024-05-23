 # Banking Application
 This is the first simple project in my Spring Boot revision series. The aim of this series is to efficiently understand and revise Spring Boot through a few projects of increasing complexity. This banking application is intentionally designed with limited parameters in 
 the entities to keep it simple and focused on core concepts. After completing this revision series, I plan to dive into more complex projects.

## 1. Overview
 The Banking Application is a straightforward and efficient Spring Boot project designed to handle basic banking operations. It includes functionalities such as account creation, transaction processing, and funds transfer between accounts. This project demonstrates the 
 use of Spring Boot's features for building robust and maintainable applications.

## 2. Features
  #### 2.1 Account Management: Create and manage bank accounts.
  #### 2.2 Transactions: Process various types of transactions, including deposits and withdrawals.
  #### 2.3 Funds Transfer: Securely transfer funds between accounts.
  #### 2.4 Exception Handling: Implement robust error handling mechanisms to manage exceptions gracefully.
  #### 2.5 DTOs and Mappers: Utilize Data Transfer Objects (DTOs) and mappers for efficient data handling.
  #### 2.6 Unit and Integration Tests: Include comprehensive test cases to ensure the application's reliability.
  #### 2.7 API Documentation: Detailed API documentation is provided to help users understand and use the available endpoints.
**Note**
Security has not been implemented in this project for the REST APIs. The focus is on understanding and revising Spring Boot fundamentals.

## 3. Configuration
 ####  3.1 application.properties: Configuration settings for the application.
  Please note that I've used PostgreSQL with pgAdmin for database management. You can use any database management system, but you will need to make suitable changes in the application.properties file.
 #### 3.2 ORM Mapping: Spring Data JPA is used for ORM mapping.
 #### 3.3 Java Version: Java 17
 #### 3.4 Spring Boot Version: 3.2.5
 #### 3.5 Database: PostgreSQL with pgAdmin. You can use any database management system.

## 4. Build and Dependencies
  4.1 pom.xml: Maven configuration file listing all dependencies and plugins used in the project.

## 5. Getting Started

### 5.1 Prerequisites

####  5.1.1 Java 17+
####  5.1.2 Maven 3.6.3 or higher
####  5.1.3 An IDE such as IntelliJ IDEA or Eclipse
####  5.1.4 PostgreSQL and pgAdmin (or any other preferred database management system)
####  5.1.5 Postman (or any other REST client tool) for testing the APIs

### 5.2 Running the Application

 ####   5.2.1 Clone the repository: 
  ```git clone https://github.com/your-username/banking-app.git ```
 ####   5.2.2 Navigate to the project directory:
 ```cd banking-app```
 ####   5.2.3 Build the project using Maven:
``` mvn clean install```
 ####   5.2.4 Run the application:
```mvn spring-boot:run```



## 6. API Endpoints

####  6.1 Create Account: POST /api/accounts/create-account
####  6.2 Get Account by ID: GET /api/accounts/{id}
####  6.3 Deposit Amount: PUT /api/accounts/{id}/deposit
####  6.4 Withdraw Amount: PUT /api/accounts/{id}/withdraw
####  6.5 Get All Accounts: GET /api/accounts
####  6.6 Delete Account: DELETE /api/accounts/delete/{id}
####  6.7 Transfer Funds: POST /api/accounts/transfer
####  6.8 Get Account Transactions: GET /api/accounts/{accountId}/transactions

## 7. Testing

The application includes unit and integration tests to ensure functionality and reliability.
### Tests can be run using:
```mvn test```
 ##### The Banking Application project boasts comprehensive test coverage:
- **Controller Layer:** 100% coverage.
- **Service Layer:** 100% coverage.
- **Overall Coverage:** 91% in methods, 90% in lines.
   
   ## 8. Contributing
   
   Contributions are welcome! Please fork the repository and submit a pull request for any enhancements or bug fixes.
