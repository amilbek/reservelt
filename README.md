# Reservelt

**Reservelt** is the application to reserve table online for restaurants.

---

## Table of Contents
1. [Project Structure](#project-structure)
2. [Getting Started](#getting-started)
3. [Backend - ReserVelt Backend](#backend---reservelt-backend)
4. [Frontend - ReserVelt Frontend](#frontend---reservelt-frontend)
5. [Running the Application](#running-the-application)
6. [Technologies Used](#technologies-used)
7. [License](#license)

---

## Project Structure

The repository contains the following directories:

### **`reservelt-backend`**
- **Language/Framework**: Java, Spring Boot
- REST API
- GraphQL
- H2 Embedded Database

### **`reservelt-frontend`**
Contains the frontend components of the project:
1. **`reservelt-vue`**:
   - **Framework**: Vue.js
2. **`reservelt`**:
   - HTML, CSS, JavaScript

---

## Getting Started

Follow these steps to set up and run the project locally.

### Prerequisites
1. **Java** (version 17) for the backend.
2. **Node.js** (version 21.7.3) and **npm** for the frontend.
3. **Vue CLI** (for Vue-based frontend):
   ```bash
   npm install -g @vue/cli

   ```bash
   npm install -g http-server


### How to Run reservelt-backend
```bash
cd reservelt-backend
mvn clean install

Backend is available on http://localhost:8080

Run the project

Rest API Documentation (Open API): http://localhost:8080/swagger-ui/index.html#/
GraphQL Documentation (Graphiql): http://localhost:8080/graphiql?path=/graphql

### How to Run reservelt-frontend
#### reservelt
```bash
cd reserverlt-frontend/reservelt
npx http-server

Frontend (without framework) is available on http://localhost:8081

#### reservelt-vue
```bash
cd reserverlt-frontend/reservelt-vue
npm install
npm run dev

Frontend (with Vue.js) is avaialble on http://localhost:3000/
