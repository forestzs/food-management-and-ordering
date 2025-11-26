# Food Management & Ordering Web App

A full-stack food ordering platform with a merchant-facing backend for order management and an admin frontend built with Vue.js. The app focuses on real-time order status, performance optimization through caching, and clean service/repository design.

---

## Features

- Merchant-facing backend for managing menus, orders, and categories  
- Real-time order status updates and basic order workflow  
- Role-based access control (admin/staff)  
- Vue.js-based admin frontend consuming RESTful APIs  
- Redis caching to offload frequent queries from MySQL  
- Refactored service & repository layers using MyBatis-Plus across 15+ entities for better maintainability  

---

## Tech Stack

**Backend**

- Java, Spring Boot  
- MyBatis-Plus  
- Redis  
- MySQL  

**Frontend**

- Vue.js  
- Axios  
- Element UI / custom components  

**Infrastructure**

- Deployed as a Spring Boot application (local or cloud)  
- MySQL as primary relational database  
- Redis for caching frequently accessed data  

---

## Architecture Overview

- **Domain Model**  
  - 15+ entities for users, roles, categories, dishes, set meals, orders, order details, shopping carts, etc.

- **Service & Repository Layers**  
  - MyBatis-Plus is used to reduce boilerplate for CRUD operations.  
  - Service layer encapsulates business logic, while mapper interfaces handle DB access.

- **Caching**  
  - Redis cache for high-traffic queries (e.g., menu listings, category data).  
  - Offloads reads from MySQL and improves response time for frequently accessed endpoints.

- **Frontend**  
  - Vue.js admin interface for managing dishes, categories, and orders.  
  - Consumes well-defined RESTful APIs and follows consistent JSON contracts (e.g., unified `R<T>` response wrapper).

---

## Project Structure

A simplified structure (may vary slightly in your repo):

```text
food-management-and-ordering/
├── pom.xml
├── src/
│   ├── main/java/com/itheima/reggie/
│   │   ├── ReggieApplication.java        # Spring Boot entry
│   │   ├── common/                       # Global exception handler, context, response wrapper
│   │   ├── config/                       # MVC, MyBatis-Plus configuration
│   │   ├── controller/                   # REST controllers (dish, order, user, etc.)
│   │   ├── dto/                          # DTOs (e.g., DishDto, SetmealDto)
│   │   ├── entity/                       # JPA / MyBatis-Plus entities
│   │   ├── mapper/                       # MyBatis-Plus mappers
│   │   ├── service/                      # Service interfaces
│   │   └── service/impl/                 # Service implementations
│   └── main/resources/
│       ├── application.yml               # Spring, DB, Redis configuration
│       ├── backend/                      # Admin/static frontend
│       └── front/                        # Customer-facing static frontend
└── ...
