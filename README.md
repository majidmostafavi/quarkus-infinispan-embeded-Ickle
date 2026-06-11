# Quarkus + Infinispan Embedded (Ickle Query Example)

A simple example demonstrating how to use Quarkus with Infinispan Embedded to store indexed entities and execute Ickle queries.
## Tech Stack

- Java (project is configured with `maven.compiler.release=25`)
- Quarkus `3.36.1`
- Quarkus REST
- Infinispan Embedded (`io.quarkiverse.infinispan:quarkus-infinispan-embedded:2.0.1`)
- Maven Wrapper

| Component                     | Version          |
| ----------------------------- | ---------------- |
| Java                          | 25               |
| Quarkus                       | 3.36.1           |
| Infinispan Embedded Extension | 2.0.1            |
| Maven                         | Wrapper Included |
| REST API                      | Quarkus REST     |


## Project Structure

- `src/main/java/BookResource.java`  
  REST endpoints for loading sample data and querying books.
- `src/main/java/search/*`  
  Indexed domain model (`Book`, `Author`, `Review`) and Protobuf schema generator.
- `src/main/resources/caches.xml`  
  Infinispan cache configuration (cache name: `book`, indexing enabled).
- `src/main/resources/application.properties`  
  Points Quarkus to the Infinispan XML config.

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   ├── BookResource.java
│   │   └── search/
│   │       ├── Author.java
│   │       ├── Book.java
│   │       ├── Review.java
│   │       └── ...
│   └── resources/
│       ├── application.properties
│       └── caches.xml
```
## Cache Configuration

The `book` local cache is configured with:

- Expiration (`lifespan="600000"`)
- Indexing enabled (`storage="local-heap"`)
- Indexed entity: `search.Book`

## REST Endpoints

Base path: `/books`

### 1) Initialize sample data

- **Method:** `PUT`
- **Path:** `/books`

Loads generated book data into the `book` cache.

Example:
bash curl -X PUT [http://localhost:8080/books](http://localhost:8080/books)


### 2) Get all books

- **Method:** `GET`
- **Path:** `/books`

Example:
bash curl [http://localhost:8080/books](http://localhost:8080/books)

### 3) Search by description term

- **Method:** `GET`
- **Path:** `/books/description/{term}`

Example:

bash curl [http://localhost:8080/books/description/java](http://localhost:8080/books/description/java)

## Run Locally

### Dev mode
bash ./mvnw quarkus:dev

Windows:


bash mvnw.cmd quarkus:dev

App will be available at:

- `http://localhost:8080`

## Build

bash ./mvnw clean package