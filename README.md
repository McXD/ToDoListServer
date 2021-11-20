# Mobile ToDo List

Project for COMP4342-Mobile Computing at The Hong Kong Polytechnic University.

## How to Build

To build the project, run `./gradlew build`. This will compile the code, test it and publish test report and finally assemble a runnable jar file with all necessary dependencies.

## How to Run

There are several ways that you can run the ToDoList server.

### Run via Jar

Navigate to `./build/libs` and you can find the jar file output from the previous build process. The server needs to connect to a MongoDB, for which the configuration can be provided either by:

- providing a command line argument: `--spring.data.mongodb.uri=$uri`. For example, if there is a mongodb instance running on your machine with default port 27017, you can run the server with `java -jar ToDoListServer.jar --spring.data.mongodb.uri=mongodb://localhost:27017/todo`

- setting an environment variable: `spring.data.mongodb.uri`.

### Run via Gradle

Alternatively, run the server with gradle supported by the SpringBoot Gradle plugin. You also need to supply database uri if yours is not the default (`spring.data.mongodb.uri=mongodb://localhost:27017/todo`). Command: `./gradlew bootRun --args='$args'`

### Run via Docker

If you don't have a MongoDB instance pre-configured, this method is the most convenient, with the prerequisite that you have the proper Docker toolchain installed. The docker-compose file will build the application's docker image, install the mongodb image, stand up a network and connect the containers for you.

Simply navigate to the project root directory and run:

```shell
docker compose up
```

### Interact with Server

If the application starts up properly, it will listen on port **8080**. Open the web browser and try to interact with the swagger-ui for the RESTful API. For example, if the server is running on the localhost, the url is: `http://localhost:8080/swagger-ui/`.