# Reservelt

**Reservelt** is the application to reserve table online for restaurants.

---

## Table of Contents
1. [Project Structure](#project-structure)
2. [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [How to Run reservelt-backend](#how-to-run-reservelt-backend)
    - [How to Run reservelt-frontend](#how-to-run-reservelt-frontend)
3. [User Credentials](#-user-credentials)
4. [REST API](#rest-api)
    - [User Registration (REST)](#user-registration-rest)
    - [User Login (REST)](#user-login-rest)
    - [Edit User Data (REST)](#edit-user-data-rest)
    - [Change User Password (REST)](#change-user-password-rest)
    - [Get User Profile (REST)](#get-user-profile-rest)
    - [Delete User Account (REST)](#delete-user-account-rest)
    - [Get Restaurant List (REST)](#get-restaurant-list-rest)
    - [Get Restaurant Details by Name (REST)](#get-restaurant-details-by-name-rest)
    - [Reserve Restaurant Table (REST)](#reserve-restaurant-table-rest)
    - [Receive User's Table Reservation History (REST)](#receive-users-table-reservation-history-rest)
    - [Change Restaurant Table Reservation Status (REST)](#change-restaurant-table-reservation-status-rest)
5. [GraphQL API](#graphql-api)
    - [User Registration (GraphQL)](#user-registration-graphql)
    - [User Login (GraphQL)](#user-login-graphql)
    - [Get User Profile (GraphQL)](#get-user-profile-graphql)
    - [Edit User Data (GraphQL)](#edit-user-data-graphql)
    - [Change User Password (GraphQL)](#change-user-password-graphql)
    - [Delete User Account (GraphQL)](#delete-user-account-graphql)

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

### 🚀 User Credentials

- **Email**: `email@mail.com`
- **Password**: `Qwerty123!`


### REST API

#### User Registration (REST)

API
```bash
/api/auth/register
```

Request Body
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

#### User Login (REST)

API
```bash
/api/auth/login
```

Request Body
```bash
{
    "email": "email@mail.de",
    "password": "Qwerty123!"
}
```

Response Body
```bash
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```

#### Get User Profile (REST)

API
```bash
/api/users
```

Request Header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```

Response Body
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

#### Edit User Data (REST)

API
```bash
/api/auth/edit
```

Request Header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```

Request Body
```bash
{
    "firstName": "Thomas",
    "lastName": "Shelby",
    "birthDate": "1972-12-12",
    "country": 1,
    "city": 1
}
```

#### Change User Password (REST)

API
```bash
/api/auth/change-password
```

Request Header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```

Request Body
```bash
{
    "currentPassword": "Qwerty123!",
    "newPassword": "Qwerty123!",
    "newPasswordConfirmation": "Qwerty123!"
}
```

#### Delete User Account (REST)

API
```bash
/api/users/delete
```

Request Header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```

#### Get Restaurant List (REST)

API
```bash
/api/restaurants/search?name=
```

Request Header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```

Response Body
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

#### Get Restaurant Details by Name (REST)

API
```bash
/api/restaurants/search?name=Grammons%20Restaurant
```

Request Header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```

Response Body
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

#### Reserve Restaurant Table (REST)

API
```bash
/api/restaurants/save-table-reservation/1
```

Request Header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```

#### Receive User's Table Reservation History (REST)

API
```bash
/api/restaurants/my-table-reservations
```

Request Header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```

Response Body
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

#### Change Restaurant Table Reservation Status (REST)

API
```bash
/api/restaurants/change-status
```

Request Parameters
```bash
reservationId=2
newStatus=CANCELLED
```

Request Header
```bash
Authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjMzNTEwMiwiZXhwIjoxNzM2MzcxMTAyfQ.rHR4GJ01wUHR_LosWP6zxtW5-cAwHuH7Q86xY0zLr2wpFhBI73QH-TVnd88sKvKbvaocKg9yahILQhbDBJChHg
```


### GraphQL API

API - /graphql

#### User Registration (GraphQL)

Request
```bash
mutation RegisterUser {
  register_user(userRegisterDto: {
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

#### User Login (GraphQL)

Request
```bash
mutation LoginUser {
  login_user(userLoginDto: {
    email: "email@mail.de"
    password: "Qwerty123!"
  })
}
```

Response
```bash
{
  "data": {
    "login_user": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmRlIiwicm9sZSI6WyJVU0VSIl0sImlhdCI6MTczNjM1MDY0MSwiZXhwIjoxNzM2Mzg2NjQxfQ.UKwXq63kxhTfV6jzmR9s46Y45fyJp21XMLZAiEriBdTliJk5YfsdV54J9fRdV3ePwi-3o9tNBGuNH8JqTNxm2w"
  }
}
```

#### Get User Profile (GraphQL)

Header
```bash
{
   "Authorization": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmNvbSIsInJvbGUiOlsiVVNFUiJdLCJpYXQiOjE3MzYzNDg2MTUsImV4cCI6MTczNjM4NDYxNX0.YEe9FNPyoCTAH5ySEPiAyptd6CJr1gULyXnLzdiv8jcpG64goDaPCUfbel7eq0IV9CMrx1QZ6l2X6aEs4323Gw"
}
```

Request
```bash
query {
      user {
        id
        firstName
        lastName
        birthDate
        email
        country {
          name
        }
        city {
          name
        }
      }
    }
```

Response
```bash
{
  "data": {
    "user": {
      "id": "0b5ae614-7ca5-4ea5-a187-3902ce0b49a8",
      "firstName": "John",
      "lastName": "Doe",
      "birthDate": "1999-12-31",
      "email": "email@mail.com",
      "country": {
        "name": "Germany"
      },
      "city": {
        "name": "Dortmund"
      }
    }
  }
}
```

#### Edit User Data (GraphQL)

Header
```bash
{
   "Authorization": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmNvbSIsInJvbGUiOlsiVVNFUiJdLCJpYXQiOjE3MzYzNDg2MTUsImV4cCI6MTczNjM4NDYxNX0.YEe9FNPyoCTAH5ySEPiAyptd6CJr1gULyXnLzdiv8jcpG64goDaPCUfbel7eq0IV9CMrx1QZ6l2X6aEs4323Gw"
}
```

Request 
```bash
mutation UserEditDto {
  edit_user(userEditDto: {
    firstName: "Thomas"
    lastName: "Shelby"
    birthDate: "1974-12-12"
    country: 1
    city: 1
  }) {
    id
  }
}
```

#### Change User Password (GraphQL)

Header
```bash
{
   "Authorization": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmNvbSIsInJvbGUiOlsiVVNFUiJdLCJpYXQiOjE3MzYzNDg2MTUsImV4cCI6MTczNjM4NDYxNX0.YEe9FNPyoCTAH5ySEPiAyptd6CJr1gULyXnLzdiv8jcpG64goDaPCUfbel7eq0IV9CMrx1QZ6l2X6aEs4323Gw"
}
```

Request 
```bash
mutation ChangeUserPasswordDto {
  change_password(changeUserPasswordDto: {
    currentPassword: "Qwerty123!"
    newPassword: "Qwerty123!"
    newPasswordConfirmation: "Qwerty123!"
  })
}
```

Response
```bash
{
  "data": {
    "change_password": "Password changed successfully"
  }
}
```

#### Delete User Account (GraphQL)

Header
```bash
{
   "Authorization": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmNvbSIsInJvbGUiOlsiVVNFUiJdLCJpYXQiOjE3MzYzNDg2MTUsImV4cCI6MTczNjM4NDYxNX0.YEe9FNPyoCTAH5ySEPiAyptd6CJr1gULyXnLzdiv8jcpG64goDaPCUfbel7eq0IV9CMrx1QZ6l2X6aEs4323Gw"
}
```

Request
```bash
mutation {
    delete_user
}
```

Response
```bash
{
  "data": {
    "delete_user": "User deleted successfully"
  }
}
```
