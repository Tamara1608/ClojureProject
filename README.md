# Background removal Project in Clojure

Welcome to the Bookstore "Remove Bg" project ! This is a Clojure-based application built with Leiningen, PostgreSQL and Hiccups. The project includes features such as user authentication (login/register), upload of files, connecting to external API and user profile customization.

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
    CREATE TABLE avatars (
      id SERIAL PRIMARY KEY,
      name varchar(255) NOT NULL,
      user_id INTEGER NOT NULL,
      timestamp timestamp DEFAULT current_timestamp
    );
     
    ```
   

4. Run the application:

    ```bash
    lein ring server
    ```

5. Access the application at [http://localhost:3000](http://localhost:3000/home).





