FROM maven:3.9.6 AS build
COPY . .
WORKDIR .
RUN mvn clean install -DskipTests
EXPOSE 8081
CMD ["mvn", "spring-boot:run"]