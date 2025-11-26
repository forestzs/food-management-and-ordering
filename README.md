
---

## 2️⃣ Food Management & Ordering Web App – README

```markdown
# Food Management & Ordering Web App

A full-stack food ordering platform with a merchant-facing backend for order management and an admin frontend built with Vue.js. The app focuses on real-time order status, performance optimization through caching, and clean service/repository design.

## Features

- Merchant-facing backend for managing menus, orders, and categories
- Real-time order status updates and basic order workflow
- Role-based access control (admin/staff)
- Vue.js-based admin frontend consuming RESTful APIs
- Redis caching to offload frequent queries from MySQL
- Refactored service & repository layers using MyBatis-Plus across 15+ entities for better maintainability :contentReference[oaicite:3]{index=3}

## Tech Stack

**Backend**

- Java, Spring Boot
- MyBatis-Plus
- Redis
- MySQL

**Frontend**

- Vue.js
- Axios
- Element UI / custom components (depending on your implementation)

**Infrastructure**

- Deployed on a Java application server (local or cloud)
- MySQL as primary relational database
- Redis for caching frequently accessed data

## Architecture Overview

- **Domain Model**  
  - 15+ entities for users, roles, categories, dishes, set meals, orders, order details, carts, etc.

- **Service & Repository Layers**  
  - MyBatis-Plus is used to reduce boilerplate for CRUD operations.
  - Service layer encapsulates business logic, while mappers handle DB access.

- **Caching**  
  - Redis cache for high-traffic queries (e.g., menu listings, category data).
  - Reduced average DB load by ~40% and improved API latency from ~800ms to ~480ms. :contentReference[oaicite:4]{index=4}

- **Frontend**  
  - Vue.js admin interface for managing dishes, categories, and orders.
  - Consumes well-defined RESTful APIs and follows consistent JSON contracts.

## Getting Started

### Prerequisites

- JDK 17+
- Maven
- MySQL
- Redis
- Node.js & npm (for Vue.js frontend)

### Backend Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/<your-username>/food-management-and-ordering.git
   cd food-management-and-ordering
