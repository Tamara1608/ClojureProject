CREATE TABLE if NOT EXISTS book (
  id SERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(255) NOT NULL,
  pages INT,
  price DECIMAL(10, 2),
  user_id INT NOT NULL,
  picture_url VARCHAR(255) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
);