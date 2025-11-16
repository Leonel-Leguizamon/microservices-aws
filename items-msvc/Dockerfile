FROM amazoncorretto:23-alpine-jdk
WORKDIR /app

ADD ./target/items-msvc-0.0.1-SNAPSHOT.jar items-msvc.jar

ENTRYPOINT ["java", "-jar", "items-msvc.jar"]