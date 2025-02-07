## Name

BCI Exercise API - Alexander Szyrej

## Description

This is a simple exercise for a JWT based service using H2 DB to store all data. 
Users are created and assigned a JWT token in the Sign-up method.
Login acts as a "get user" endpoint and retrieves the information associated to the token present in the request header.

## Installation

There's no database needed to run this microservice since it integrates with H2 memory database.
However, it is possible to configure the JWT Secret Key, DB Username and Password in the environment variables

### Environment Variables that should be set

    JWT_SECRET_KEY -> Strong enough secret key for creating JWT tokens.
    DB_USER -> Username for H2 connection
    DB_PASS -> Password for H2 connection

## Commands

./gradlew build
./gradlew test
./gradlew bootRun

## Diagrams

[Components_Diagram]()
![[api]Components_Diagram](docs/BCI_Exercise_Components_Diagram.png)

[Sequence_Diagram]()
![[api]Sequence_Diagram](docs/BCI_Exercise_Sequence_Diagram.png)

## Java 11 features included
String functions:
strip()
isBlank()
repeat()