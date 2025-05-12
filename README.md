# Movie Watchlist App
## Overview
Movie Watchlist is a full-stack web application designed to help users track and manage their movie-watching experience. The application provides features for both regular users and administrators, allowing for comprehensive movie list management.

## Features
### User Features
- Add movies to "To Watch" or "Watched" lists
- Rate and review movies
- Personalized Dashboard
- My Watchlist section for easy tracking

### Admin Features
- Add new movies to the catalog
- Update existing movie information
- Delete movies from the catalog

## Tech Stack
- **Backend**: 
  - Java 17
  - Spring Boot
  - Hibernate (JPA Provider)
  - MySQL
- **Frontend**: 
  - Static HTML/CSS (via Spring)
- **Development Tools**: 
  - IntelliJ IDEA or Eclipse
  - Maven
  - Postman (API Testing)
  - Git (Version Control)

## Prerequisites
- Java Development Kit (JDK) 17
- MySQL Database
- Maven
- IntelliJ IDEA or Eclipse
- Git

## Getting Started
### 1. Clone the Repository
```bash
git clone https://github.com/arunramasayam/moviewatchlist-app-spring
cd moviewatchlist-app-spring
```

### 2. Database Setup
1. Install MySQL if not already installed
2. Create a new database for the project
```sql
CREATE DATABASE movieswatchlistdb;
```

### 3. Configure Database Connection
Open `src/main/resources/application.properties` and update the following properties:
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/movieswatchlistdb
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD

# Hibernate DDL Auto (first time use 'create', then change to 'update')
spring.jpa.hibernate.ddl-auto=create
# This will help create initial admin credentials on first run
```

### 4. Import Project in IDE
- Open IntelliJ IDEA or Eclipse
- Import the project as a Maven project
- Ensure Maven dependencies are downloaded

### 5. Build the Project
```bash
# First-time build with Maven
mvn clean install
```

### 6. Run the Application
Multiple ways to run the application:
#### Using IDE
- Run the main application class in your IDE

#### Using Terminal
```bash
# Run Maven command to run the application
mvn spring-boot:run
```

### 7. Post First Run
- After the first run, change `spring.jpa.hibernate.ddl-auto` to `update` in `application.properties`
```properties
spring.jpa.hibernate.ddl-auto=update
```

## Application Access
### Base URL
- **Base URL**: `http://localhost:8080`

### Initial Admin Credentials
- **Email**: `Admin@movieswatchlist.com`
- **Password**: `Admin@123`

### User Pages
- `/index.html` - Home Page
- `/login.html` - User Login
- `/register.html` - User Registration
- `/dashboard.html` - User Dashboard
- `/watchlist.html` - My Watchlist

### Admin Pages
- `/login.html` - Admin Login
- `/moviemanagement.html` - Movie Management, add, update and delete movies
- `/loadmovies.html` - Load Movies
- All user pages are also accessible to admins

### Initial Setup
#### Movie Catalog
- On first run, the movie database will be empty
- Admin can load movies using Excel file through `/loadmovies.html`

### API Documentation
- API documentation is available in the project repository
- Swagger/OpenAPI documentation can be accessed at `/swagger-ui/index.html`

