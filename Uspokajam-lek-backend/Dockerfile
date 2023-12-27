FROM maven:3.8.4-openjdk-17-slim
WORKDIR /app
COPY Uspokajam-lek-backend/pom.xml ./pom.xml
COPY Uspokajam-lek-backend/src ./src
RUN mvn package -DskipTests
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/uspokajam-lek-backend-0.0.1-SNAPSHOT.jar"]
