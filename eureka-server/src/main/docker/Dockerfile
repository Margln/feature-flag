FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY eureka-server-1.0-SNAPSHOT.jar eureka-server.jar
COPY start.sh start.sh
RUN mkdir config
ENTRYPOINT ["java","-jar","-Xms256m","-Xmx512m","-Dspring.profiles.active=pro","eureka-server.jar","--spring.config.additional-location=/app/config/"]