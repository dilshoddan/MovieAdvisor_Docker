# MovieAdvisor

Simple project using Docker, Gradle, Spring Boot with MySQL with simple API.

Technologies Used

- Docker
- Gradle
- Spring Boot
- Spring Data JPA
- Lombok
- MySQL

How to Run this application

Clean build the project

```shell
$ ./gradlew clean build
```

fetch and build docker containers,

```shell
$ docker-compose build
```

Start the containers and access the website using http://localhost:8080

```shell
$ docker-compose up
```

### Used resources

- [Docker Compose For Spring Boot with MySQL](https://javatodev.com/docker-compose-for-spring-boot-with-mysql/)
- [Docker Compose For Spring Boot with MariaDB](https://javatodev.com/docker-compose-for-spring-boot-with-mariadb/)
