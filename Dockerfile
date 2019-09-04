FROM openjdk:11-jre-slim

EXPOSE 8080

RUN mkdir /app

COPY build/libs/*.jar /app/spring-boot-application.jar

HEALTHCHECK CMD curl --fail http://localhost:8080/actuator/health | grep \{\"status\":\"UP\"\} || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spring-boot-application.jar"]