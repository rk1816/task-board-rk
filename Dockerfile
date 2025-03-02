FROM openjdk:21-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y maven

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests && \
    mvn dependency:copy-dependencies -DoutputDirectory=target/dependency

EXPOSE 8081

CMD ["java", "-cp", "target/task-board-rk-1.0-SNAPSHOT.jar:target/dependency/*", "com.taskboard.TaskBoardApplication"]