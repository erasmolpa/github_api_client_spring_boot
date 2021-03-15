FROM openjdk:8-jdk-alpine as build

WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM openjdk:8-jdk-alpine

EXPOSE ${CONTAINER_PORT}
EXPOSE 8443

WORKDIR application
COPY --from=build application/dependencies/ ./
COPY --from=build application/spring-boot-loader/ ./
COPY --from=build application/snapshot-dependencies/ ./
COPY --from=build application/application/ ./

HEALTHCHECK --start-period=30s --interval=30s --timeout=3s --retries=3 \
	CMD curl -v --fail http://localhost:${CONTAINER_PORT}/actuator/health || exit 1

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]