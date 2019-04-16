-- From the Spring Boot docs:
-- "Spring Boot can automatically create the schema (DDL scripts) of your
-- DataSource and initialize it (DML scripts). It loads SQL from the
-- standard root classpath locations: schema.sql and data.sql, respectively."

CREATE TABLE albums (
    id INT,
    artist VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
