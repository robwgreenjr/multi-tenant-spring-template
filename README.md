# Multi-Tenant Spring Template

## Project Structure

```
.
├── docker                                        // Local docker setup
├── src                          
│   ├── main                                      // Application
│   │   ├── java
│   │   │   ├── template                          // Main source code
│   │   │   │   ├── [domain]                      // Module for specific domain
│   │   │   │   │   ├── cli                       // CLI commands
│   │   │   │   │   ├── advice
│   │   │   │   │   ├── config
│   │   │   │   │   ├── constants                 // "Enums" for strings 
│   │   │   │   │   ├── controllers               // API endpoint routing
│   │   │   │   │   ├── crons
│   │   │   │   │   ├── dtos                      // Data Transfer Objects
│   │   │   │   │   ├── entities                  // Database entities
│   │   │   │   │   ├── enums                
│   │   │   │   │   ├── events  
│   │   │   │   │   ├── exceptions               
│   │   │   │   │   ├── filters                   // Front facing "middleware"
│   │   │   │   │   ├── helpers                   // Dynamic helper services (depends on services within application)
│   │   │   │   │   ├── interceptors              // Middleware
│   │   │   │   │   ├── listeners                 // Event listeners
│   │   │   │   │   ├── mappers              
│   │   │   │   │   ├── models                    // Business layer classes
│   │   │   │   │   ├── repositories              // Database services
│   │   │   │   │   ├── resolvers
│   │   │   │   │   ├── services                  // Main business services
│   │   │   │   │   └── utilities                 // Non-Dynamic helper services (doesn't use any services)
│   │   │   │   ├── CliRunner.java                // Manages custom CLI commands
│   │   │   │   └── TemplateApplication.java      // Root server file
│   │   ├── resources    
│   │   │   │   ├── db                      
│   │   │   │   │   ├── changlog                  // Database migration scripts
│   │   │   │   │   │   ├── global                       
│   │   │   │   │   │   ├── main                       
│   │   │   │   │   │   └── tenant                     
│   │   │   │   └── application.properties        // Configurations                       
│   ├── test                                      // Test Suite
│   │   ├── java
│   │   │   ├── template                          
│   │   │   │   └── [domain]                      // Tests for specific domain
│   │   ├── resources    
│   │   │   │   ├── sql                           // Test SQL scripts
│   │   │   │   │   └── [domain]                  // SQL scripts specific to domain
│   │   │   │   └── application.properties        // Test configurations    
├── .env.example                                  // Environment template file
├── docker-compose.yml           
├── mvnw                                          // Maven runner
├── pom.xml                                       // Dependency manager
```   

## Docker

```
docker network create spring-template

docker-compose up -d
```

## Local SSL

Linux

```
sudo cp ./docker/local-ssl/minica.pem /usr/local/share/ca-certificates/minica.crt
sudo chmod 644 /usr/local/share/ca-certificates/minica.crt
sudo update-ca-certificates
```

### TODO:

    - Redesign tenant database with tenant_id with all tables, using 2 schemas
        - Setup Internal and Tenant stuff...
            - Add events and needed tests
            - Internal Auth
            - Tenant Auth
            - Allow auth middleware to handle separate internal/tenant auth properly
        - Get all test passing
    - Change tenant generation from creating a new database for new tenants to simply use new design
    - Add query parameter support for list of values, only for EQ, LIKE, and NE filters
    - Add any extra test for parameters/query builder/hypermedia

    - Update README with needed project details
    - Document query parameter builder
    - Document hypermedia