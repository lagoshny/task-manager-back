# TaskManagerBack

Example of the Spring rest full application builded with Spring HATEOAS.

## Development

Run `mvn spring-boot:run` app will be started at `localhost:8080`.

Navigate to HAL browser `http://localhost:8080/api/v1` (auth is required) and explore server API.  
To pass auth need to create new user. For that start [TaskManagerFront](https://github.com/lagoshny/task-manager-front) application and register an user.


## Running tests

Run `mvn test` to execute unit tests.

Run `mvn -P api-test test` to execute unit tests and API tests.
