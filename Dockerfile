FROM openjdk:17-alpine
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY ./scripts/docker-spring-env.sh /scripts/docker-spring-entrypoint.sh
RUN chmod +x /scripts/docker-spring-entrypoint.sh
ENTRYPOINT ["java","-jar","/app.jar"]