# -------- Stage 1: Build --------
FROM eclipse-temurin:21-jdk AS buildlayer
WORKDIR /app

# cache dependencies
COPY gradle gradle
COPY gradlew .
COPY build.gradle settings.gradle ./
RUN ./gradlew --no-daemon dependencies || true  

# build the application
COPY src src
RUN ./gradlew --no-daemon bootJar

# Install xargs (in the findutils package)
# RUN microdnf install -y findutils && microdnf clean all

# -------- Stage 2: Runtime --------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create user and switch to non-root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=buildlayer ./app/build/libs/mongo-crud-0.0.1-SNAPSHOT.jar app.jar

# JVM optimization for containers
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport"

EXPOSE 8080

# Health check using wget (built into alpine)
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
      CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1


ENTRYPOINT ["java", "-jar", "/app/app.jar"]
