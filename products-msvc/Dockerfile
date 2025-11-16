FROM amazoncorretto:23-alpine-jdk
WORKDIR /app

ADD ./target/products-msvc-0.0.1-SNAPSHOT.jar products-msvc.jar

ENTRYPOINT ["java", "-jar", "products-msvc.jar"]