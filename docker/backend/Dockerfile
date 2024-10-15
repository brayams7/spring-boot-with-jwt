FROM maven:3.8.7-openjdk-18-slim AS build
WORKDIR /build

COPY pom.xml .

# este comando descarga los modulos de dependencias
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

#runtime stage
FROM amazoncorretto:17

ARG PROFILE=dev
ARG APP_VERSION=1.0.1

#define few things
WORKDIR /app
COPY --from=build /build/target/library-api-*.jar /app/

EXPOSE 8088

# ENV sirve para definir variables de entorno

ENV DB_URL=jdbc:postgresql://postgres-sql-lsn:5432/library_social_network_db
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV EMAIL_HOSTNAME=mail-dev-bsn
ENV EMAIL_USERNAME=brayck
ENV EMAIL_PASSWORD=password

# este comando corre el jar ( app )
CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} /app/library-api-${JAR_VERSION}.jar
