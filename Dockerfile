FROM eclipse-temurin:22-jammy AS builder
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

RUN mkdir -p /root/.m2 && mkdir /root/.m2/repository
COPY settings.xml /root/.m2

RUN ./mvnw dependency:go-offline
COPY ./src ./src
ENV SPRING_PROFILES_ACTIVE=deploy
RUN ./mvnw clean install #-DskipTests


FROM eclipse-temurin:22-jammy

RUN addgroup evs2ops; adduser  --ingroup evs2ops --disabled-password evs2ops1
USER evs2ops1

WORKDIR /opt/app
EXPOSE 8018
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=deploy", "-jar", "/opt/app/*.jar" ]

