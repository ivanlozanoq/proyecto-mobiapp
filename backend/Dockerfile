# Stage 1: Builder - compila el JAR usando el Gradle Wrapper del proyecto
FROM eclipse-temurin:21-jdk AS builder

# Metadata de build inyectada por CI
ARG GIT_SHA="unknown"
ARG APP_VERSION="unknown"

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

# Labels OCI para verificar qué commit/versión corre en runtime
ARG GIT_SHA="unknown"
ARG APP_VERSION="unknown"
LABEL org.opencontainers.image.revision=$GIT_SHA \
      org.opencontainers.image.version=$APP_VERSION \
      org.opencontainers.image.title="pedidos-api" \
      org.opencontainers.image.source="https://github.com/ivanlozanoq/proyecto-mobiapp"

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

