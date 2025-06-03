FROM amazoncorretto:17
EXPOSE 8080
COPY build/libs/*plain.jar /app/spring-app.jar
ENTRYPOINT ["java","-jar","/app/spring-app.jar"]