# Multi-Tenant Spring Template

This is a template to show a generic multi-tenant spring project. Covering a lot
of standard parts of building an application e.g. test suite, clean
architecture,
ORM, migrations, etc.

If you were to apply this to a real multi-tenant project you may want to split
the Internal & Tenant application code into separate repos, the combination of
the two in one is simply to show an example.

This project originally started out as a how-to setup a multi-tenant project
when tenants are using completely separate databases. After that was achieved I
went ahead and changed it to default to mixing tenants in the same database (
tenant_id in each row).

You could always use the tenant_id per row method and migrate later to having
tenants in their own database or separate tenants within schemas.

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

## CLI Commands

### TODO:

    - Add query parameter support for list of values, only for EQ, LIKE, and NE filters
    - Add any extra test for parameters/query builder/hypermedia

    - Update README with needed project details
    - Document query parameter builder
    - Document hypermedia