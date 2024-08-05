FROM openjdk:17-oracle
LABEL authors="mrirmag"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} translator-service.jar
ENTRYPOINT ["java","-jar","/translator-service.jar"]