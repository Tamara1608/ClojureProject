# Bookstore Project in Clojure

Welcome to the Bookstore "Libre Nation" project ! This is a Clojure-based application built with Leiningen, PostgreSQL and Hiccups. The project includes features such as user authentication (login/register), book management (add, delete, edit), and user profile customization.

## Technologies Used
- Clojure
- Leiningen
- PostgreSQL
- Docker
- JavaScript

## Getting Started

### Prerequisites
- Clojure installed
- Leiningen installed
- PostgreSQL installed
- Docker installed

### Setup
1. Clone the repository:

    ```bash
    git clone https://github.com/tamara1608/bookstore-clojure.git
    cd bookstore-clojure
    ```

2. Initialize the database using Docker:

    ```bash
    docker pull postgres
    ```

     ```bash
    docker run --name database -e POSTGRES_PASSWORD=1234 -p 5432:5432 -d postgres
    ```
     ```bash
    psql -U postgres -d database

    CREATE DATABASE database;
    
    CREATE TABLE users (
      id    SERIAL PRIMARY KEY,
      email varchar(255) NOT NULL UNIQUE,
      name  varchar(255) NOT NULL,
      encrypted_password varchar(255),
      timestamp timestamp DEFAULT current_timestamp
     );
    ```
   

4. Run the application:

    ```bash
    lein ring server
    ```

5. Access the application at [http://localhost:3000](http://localhost:3000).

## Features

### 1. User Authentication

- **Login:** Users can log in with their credentials. (Password has minimum 6 characters)
  
- **Register:** New users can register by providing necessary details.

### 2. Book Management

- **Add Book:** Add new books to the bookstore with details like title, author, and price.

- **Delete Book:** Remove books from the inventory.

- **Edit Book:** Modify existing book details.

### 3. User Profile

- **Edit Profile:** Users can customize their profiles by adding avatars, changing their names, or updating passwords.

## Usage

1. **User Authentication:**
    - Visit the login page to access your account.
    - If you are new, register with a unique username and password.

2. **Book Management:**
    - Navigate to the "Add Book" section to add new books to the inventory.
    - Navigate to profile page to see books you have added.
        - Use the "Delete Book" option to remove books from the store.
        - Modify existing book details through the "Edit Book" feature.

3. **User Profile:**
    - Access your profile to update information.
    - Add avatars, change your name, or update your password.
    - Delete account. 



