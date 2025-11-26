FROM bellsoft/liberica-openjdk-alpine-musl:21.0.1
COPY target/ollama-ui-1.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]