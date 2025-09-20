# Stage 1: Builder - compila el JAR usando el Gradle Wrapper del proyecto
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /workspace

# Copiamos solo lo necesario para que Gradle resuelva el proyecto multi-módulo
COPY settings.gradle ./
COPY gradlew ./
COPY gradle ./gradle
COPY backend ./backend

RUN chmod +x ./gradlew \
    && ./gradlew :backend:bootJar --no-daemon

# Normalizamos el nombre del artefacto a app.jar para simplificar el stage runtime
# Seleccionar el JAR que no sea el "-plain" de manera compatible con sh
RUN for f in backend/build/libs/*.jar; do \
      case "$f" in *-plain.jar) ;; *) cp "$f" app.jar; break;; esac; \
    done

# Stage 2: Runtime - imagen ligera para ejecutar el JAR
FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app
COPY --from=builder /workspace/app.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

