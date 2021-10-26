### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html
- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.



### Notes
- Run tests with `mvn clean test`
- Modifications made to code:
  - Add tests for GET, POST, UPDATE
  - Protect controller end points with user authentication (username: sa, password: 123456)
  - Add caching logic for GET requests
  - Fixed functionality of the 5 API endpoints
- If I had more time:
  - Add test for DELETE, more tests for other endpoints e.g. If userId not found
  - Add conditional caching, eviction policy with EhCache

#### My experience in Java

- I'm a beginner and just recently learned Spring Boot
