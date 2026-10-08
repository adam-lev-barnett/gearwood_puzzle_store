# Gearwood Puzzle Store

_A full-stack Spring Boot puzzle store e-commerce application highlighting proficiency in:_
- **Backend languages/frameworks**: Java, Spring Boot, Spring Security (stateless JWT auth)
- **Frontend**: HTML, CSS, Javascript, Thymeleaf
- **Architecture**: RESTful API with MVC, backed by MySQL

## Key features 
- **Dynamic catalog search & filtering:** Keyword search with multi-attribute filtering across category, difficulty, and price bounds
- **Session-based cart persistence::** Cart maintains state before and after logging in
- **In-Page Administrator Controls:** Role-protected catalog management (CRUD, active/inactive toggling)

## Tech Stack
- **Frontend:** Thymeleaf, HTML5, CSS3, JavaScript
- **Backend:** Java 21, Spring Boot 3.5, Spring MVC, Spring Security 6, Spring Data JPA, JJWT
- **Database & Persistence:** MySQL 8, Hibernate ORM

## Technical Highlights
- **Stateless JWT Security Architecture:** 
  - Token-based authentication using JWTs stored in secure `HttpOnly` cookies
  - Method-level authorization (`@PreAuthorize`) for redundancy to prevent failures
  - Custom JSON exception handlers for 401 Unauthorized and 403 Forbidden events.
- **Session-based cart persistence::** Cart maintains state before and after logging in
- **Multi-Criteria Query-handling:** Spring Data JPA queries handling all permutations of category, difficulty, keyword, and price ranges.
- **Sliding-Window Rate Limiting:** thread-safe `ConcurrentHashMap` request-throttling filter guarding sensitive endpoints against brute force and DDoS traffic.

## Quick Start

### Prerequisites
- Java Development Kit (JDK) 21+
- Apache Maven 3.8+ (or use included `./mvnw`)
- MySQL Server 8.0+

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/adam-lev-barnett/gearwood_puzzle_store.git
   cd gearwood_puzzle_store
   ```

2. Configure MySQL Database:
   ```sql
   CREATE DATABASE puzzle_store CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. Set Environment Variables:
   Update database credentials in `.env` (a template is available in `stuff.env`):
   ```env
   DB_USER=your_mysql_username
   DB_PASSWORD=your_mysql_password
   JWT_SECRET=your_secure_base64_secret_key
   JWT_EXP=your_jwt_expiration
   ```

4. Run the application:
   ```bash
   ./mvnw clean spring-boot:run
   ```
   Navigate to `http://localhost:8080/` in your browser.

### Demo Credentials & Payment Testing
- **Administrator Account:** `admin.user@gmail.com` / `Admin1234`
- **Customer Account:** Register a new user via `/register`
- **Payment Gateway Testing:** Use any standard 16-digit card number for approval (e.g., `4111 1111 1111 1111`); use `4000000000000002` or `4111111111111112` to test simulated card decline handling.