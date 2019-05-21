CREATE TABLE book (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  title VARCHAR(250) NOT NULL,
  isbn VARCHAR(13) NOT NULL,
  price NUMERIC NOT NULL
);

INSERT INTO book (title, isbn, price) VALUES ('Design Patterns: Elements of Reusable Object-Oriented Software', '9780201633610', 50.38);