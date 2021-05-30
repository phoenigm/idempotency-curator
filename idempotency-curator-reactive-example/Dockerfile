FROM adoptopenjdk/openjdk11:alpine-jre

COPY ./build/libs/idempotency-curator.jar idempotency-curator.jar
CMD ["java", "-Xmx128m", "-jar", "idempotency-curator.jar"]
