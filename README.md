# Reservelt

**Reservelt** is the application to reserve table online for restaurants.

---

## Table of Contents
1. [Project Structure](#project-structure)
2. [Getting Started](#getting-started)
3. [Backend - Reservelt Backend](#backend---reservelt-backend)
4. [Frontend - Reservelt Frontend](#frontend---reservelt-frontend)
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
   ```

   ```bash
   npm install -g http-server
   ```

### How to Run reservelt-backend
```bash
cd reservelt-backend
```
```bash
mvn clean install
```
```bash
mvn spring-boot:run
```
Backend is available on http://localhost:8080

Rest API Documentation (Open API): http://localhost:8080/swagger-ui/index.html#/

GraphQL Documentation (Graphiql): http://localhost:8080/graphiql?path=/graphql

### How to Run reservelt-frontend
#### reservelt
```bash
cd reserverlt-frontend/reservelt
```
```bash
npx http-server -p 8081
```

Frontend (without framework) is available on http://localhost:8081

#### reservelt-vue
```bash
cd reserverlt-frontend/reservelt-vue
```

```bash
npm install
```

```bash
npm run dev
```

Frontend (with Vue.js) is avaialble on http://localhost:3000/


### REST API
#### register 
api
```bash
/api/auth/register
```
request body
```bash
{
    "firstName": "Thomas",
    "lastName": "Shelby",
    "birthDate": "1972-12-12",
    "country": 1,
    "city": 1,
    "email": "email@mail.de",
    "password": "Qwerty123!",
    "passwordConfirmation": "Qwerty123!"
}
```

#### login
api
```bash
/api/auth/login
```
request body
```bash
{
    "email": "email@mail.de",
    "password": "Qwerty123!"
}
```
response body
```bash
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```

#### edit user data 
api
```bash
/api/auth/edit
```
request header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```
request body
```bash
{
    "firstName": "Thomas",
    "lastName": "Shelby",
    "birthDate": "1972-12-12",
    "country": 1,
    "city": 1,
}
```

#### user profile
api
```bash
/api/users
```
request header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```
response body
```bash
{
    "id": "a7f4f21f-22a2-4061-8ebf-3394b7658218",
    "firstName": "John",
    "lastName": "Doe",
    "birthDate": "1989-12-31",
    "country": {
        "id": 1,
        "name": "Germany"
    },
    "city": {
        "id": 1,
        "name": "Dortmund",
        "country": {
            "id": 1,
            "name": "Germany"
        }
    },
    "email": "email@mail.com"
}
```

#### delete user account
api
```bash
/api/users/delete
```
request header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```

#### change password 
api
```bash
/api/auth/change-password
```
request header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```
request body
```bash
{
    "currentPassword": "Qwerty123!",
    "newPassword": "Qwerty123!",
    "newPasswordConfirmation": "Qwerty123!"
}
```
#### receiven restaurant list
```bash
/api/restaurants/search?name=
```
request header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```
response body
```bash
[
    {
        "id": 1,
        "name": "Grammons Restaurant",
        "address": "Wieckesweg 29, 44309 Dortmund",
        "phoneNumber": "+49 231 93144465",
        "rating": "4.6",
        "foods": [
            {
                "id": 1,
                "name": "Hamburger",
                "description": "Hamburger mit Butter und Salat",
                "price": 8.20
            },
            {
                "id": 2,
                "name": "Pizza",
                "description": "Pizza mit Tomaten und Paprika",
                "price": 10.50
            }
        ]
    },
    {
        "id": 2,
        "name": "The Stage",
        "address": "Dula Center, Nortkirchenstraße 53, 44263 Dortmund",
        "phoneNumber": "+49 231 2222550",
        "rating": "4.7",
        "foods": [
            {
                "id": 3,
                "name": "Burger",
                "description": "Burger mit Butter und Kasse",
                "price": 7.50
            },
            {
                "id": 4,
                "name": "Pizza suchi",
                "description": "Pizza mit Tomaten und Paprika",
                "price": 10.50
            }
        ]
    }
]
```

#### receive restaurant by name
```bash
/api/restaurants/search?name=Grammons%20Restaurant
```
request header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```
response body
```bash
[
    {
        "id": 1,
        "name": "Grammons Restaurant",
        "address": "Wieckesweg 29, 44309 Dortmund",
        "phoneNumber": "+49 231 93144465",
        "rating": "4.6",
        "foods": [
            {
                "id": 1,
                "name": "Hamburger",
                "description": "Hamburger mit Butter und Salat",
                "price": 8.20
            },
            {
                "id": 2,
                "name": "Pizza",
                "description": "Pizza mit Tomaten und Paprika",
                "price": 10.50
            }
        ]
    }
]
```

#### save table reservations
api
```bash
/api/restaurants/save-table-reservation/1
```
request header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```

#### receive table reservation history
api
```bash
/api/restaurants/my-table-reservations
```
request header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```
response body
```bash
[
    {
        "id": 1,
        "restaurantName": "The Stage",
        "tableReservationStatus": "COMPLETED"
    },
    {
        "id": 2,
        "restaurantName": "Grammons Restaurant",
        "tableReservationStatus": "RESERVED"
    }
]
```

#### change reservation status
```bash
http://localhost:8080/api/restaurants/change-status?reservationId=2&newStatus=CANCELLED
```
request header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```


### GraphQL API
#### register
request 
```bash
mutation RegisterUser {
  registerUser(input: {
    firstName: "Thomas"
    lastName: "Shelby"
    birthDate: "1972-12-12"
    country: 1
    city: 1
    email: "email@mail.de"
    password: "Qwerty123!"
    passwordConfirmation: "Qwerty123!"
  }) {
    id
  }
}
```

#### login
```bash
mutation LoginUser {
  loginUser(input: {
    email: "email@mail.de"
    password: "Qwerty123!"
  }) {
    token
  }
}
```

#### user profile
