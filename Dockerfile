FROM openjdk:11 as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY infrastructure/pom.xml infrastructure/pom.xml
COPY domain/pom.xml domain/pom.xml
COPY infrastructure/src infrastructure/src
COPY domain/src domain/src
RUN chmod +x mvnw
RUN ./mvnw clean install -DskipTests

RUN mkdir -p infrastructure/target/dependency && (cd infrastructure/target/dependency; jar -xf ../*.jar)

FROM openjdk:11
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/infrastructure/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","-Dspring.profiles.active=prod","org.example.VoteApplication"]