FROM maven:3.8-amazoncorretto-19 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:19 as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} flight_service.jar
RUN java -Djarmode=layertools -jar flight_service.jar extract
LABEL authors="Михаил Дюсов ИУ7-11М"

FROM amazoncorretto:19
ENV PORT=8060
WORKDIR flight_service
COPY --from=builder flight_service/spring-boot-loader/ ./
COPY --from=builder flight_service/dependencies/ ./
COPY --from=builder flight_service/snapshot-dependencies/ ./
COPY --from=builder flight_service/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]