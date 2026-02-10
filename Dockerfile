FROM eclipse-temurin:21
EXPOSE 8080
ARG JAR_FILE="target/spring-crud-generator-demo-0.0.1-SNAPSHOT.jar"
ADD ${JAR_FILE} spring-crud-generator-demo.jar
ENTRYPOINT [ "java", "-jar", "/spring-crud-generator-demo.jar" ]