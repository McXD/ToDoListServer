FROM openjdk:8-jdk-alpine
WORKDIR /todo
COPY ./build/libs .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ToDoListServer.jar"]