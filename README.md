# CTO Training - Reactive Programming with Spring WebFlux

**Java Spring** is a technology that has been present in the development of backend services for years. The Spring ecosystem has been improved quite a lot during the few last years, it includes new projects like Spring Boot that helps to create the boilerplateof a new project, or Spring Cloud focused on cloud and microservices architectures, and adopting new development paradigmas like `Reactive Programming`.

**Spring MVC** has been during years one of the most popular frameworks of Java language for building web applications, using a synchronous and blocking architecture where each request is managed by only one thread. This approach, yet it is an easy way for applications development, it has been proved inefficient to fulfill the requirements of current applications demanding higher concurrency.

**Spring WebFlux** is a non-blocking and asynchronous framework built on top of the reactive programming principles and available in the last Spring version framework (Spring Framework 5). In spite of the fact that Spring has made a big effort to keep MVC and WebFlux consistent, this new paradigm requires a new approach to develop applications. This course is aimed at backend developers that want to be updated with the  latest  novelties  of  the  Spring  framework,  learn  the  principles  of  reactive  programming  and  the development of microservices using WebFlux. The course combines both a theorical and a practice part to iteratively build a microservice.

## Agenda

- Reactive Programming
  - Reactor: Mono and Flux
  - Exercises with Reactor

- Spring Ecosystem
  - Spring framework
  - Spring Web MVC vs Spring WebFlux
  - Spring Data
  - Spring Cloud

- Creating a Spring project with Spring Boot

- Spring WebFlux
  - Spring WebFlux, why?
  - Spring MVC vs WebFlux
  - Architecture of Spring application: domain, controllers, services, repositories
  - Functional vs Annotations
  - Reactive REST server
  - Processing the request (body, headers, parameters)
  - Generating a response (body, headers, status code)
  - Error handlers
  - WebFlux filters
  - Reactive Web Client
  - Validation with model and JSON schemas
  - Logging with reactive context
  - Unit tests

- Spring Data
  - Reactive repositories
  - Reactive project with JDBC/JPA
  - Advanced queries
  - Exercise: REST API integrated with MongoDB

- Spring WebFlux integrations
  - Reactive flow with Kafka
  - With non-reactive systems
  - Exercise: Integrating a Kafka consumer and producer

## Presentations

See [presentations](presentations) for the training.
