FROM maven:3.8-amazoncorretto-19 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:19 as builder
WORKDIR gateway
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} gateway.jar
RUN java -Djarmode=layertools -jar gateway.jar extract
LABEL authors="Михаил Дюсов ИУ7-11М"

FROM amazoncorretto:19
ENV PORT=8080
WORKDIR gateway
COPY --from=builder gateway/spring-boot-loader/ ./
COPY --from=builder gateway/dependencies/ ./
COPY --from=builder gateway/snapshot-dependencies/ ./
COPY --from=builder gateway/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]