FROM openjdk:21
WORKDIR /app
COPY . .
RUN microdnf install -y findutils && microdnf clean all
EXPOSE 8080
RUN chmod +x ./gradlew
ENTRYPOINT ["./gradlew", "bootrun"]
