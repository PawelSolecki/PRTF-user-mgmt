
# User Management Service

## Service Overview

The user-mgmt service is responsible for managing user accounts in the portfolio management system.
It acts as a dedicated client for Keycloak, meaning it is the only service that communicates directly with Keycloak for authentication and authorization purposes.

Main features:
- Handles user registration and login processes.
- Supports token refresh operations.
- Maintains its own user-related data (e.g. name address, last update timestamp) in the database.
- Exposes APIs for other services to retrieve or update user information.

This service uses Spring WebFlux with Mono and Flux, enabling reactive programming.
In this paradigm, instead of blocking threads, the application processes data asynchronously as it becomes available.

## Main README

For the overall project description and demo instructions, see the main README [here](https://github.com/PawelSolecki/PRTF#).

## Tech Stack
![Java 17](https://img.shields.io/badge/Java_17-007396?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Spring WebFlux](https://img.shields.io/badge/Spring_WebFlux-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white)
![OAuth2](https://img.shields.io/badge/OAuth2-EB5424?style=flat-square&logo=oauth&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat-square&logo=postgresql&logoColor=white)
![R2DBC](https://img.shields.io/badge/R2DBC-003B57?style=flat-square&logo=databricks&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=flat-square&logo=flyway&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-A42929?style=flat-square&logo=lombok&logoColor=white)
![Keycloak](https://img.shields.io/badge/Keycloak-00A3E0?style=flat-square&logo=keycloak&logoColor=white)



## Setup & Configuration

### 1. Run with Docker Compose (recommended)
1.	Make sure you have Docker and Docker Compose installed.
2.	From the project root, run:
```shell
docker compose up --build
   ```
3.	The service will be available at:
      http://localhost:8081


**Environment variables are defined in the .env file.**

Example:
```
DB_USER=user
DB_PASSWORD=password
KC_CLIENT_SECRET=secret
```


