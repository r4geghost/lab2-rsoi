FROM maven:3.8-amazoncorretto-19 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:19 as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} ticket_service.jar
RUN java -Djarmode=layertools -jar ticket_service.jar extract
LABEL authors="Михаил Дюсов ИУ7-11М"

FROM amazoncorretto:19
ENV PORT=8070
WORKDIR ticket_service
COPY --from=builder ticket_service/spring-boot-loader/ ./
COPY --from=builder ticket_service/dependencies/ ./
COPY --from=builder ticket_service/snapshot-dependencies/ ./
COPY --from=builder ticket_service/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]