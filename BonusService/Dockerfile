FROM maven:3.8-amazoncorretto-19 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:19 as builder
WORKDIR bonus_service
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} bonus_service.jar
RUN java -Djarmode=layertools -jar bonus_service.jar extract
LABEL authors="Михаил Дюсов ИУ7-11М"

FROM amazoncorretto:19
ENV PORT=8050
WORKDIR bonus_service
COPY --from=builder bonus_service/spring-boot-loader/ ./
COPY --from=builder bonus_service/dependencies/ ./
COPY --from=builder bonus_service/snapshot-dependencies/ ./
COPY --from=builder bonus_service/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]