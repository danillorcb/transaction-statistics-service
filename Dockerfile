FROM openjdk:8
ADD target/transaction-statistics-service.jar transaction-statistics-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","transaction-statistics-service.jar"]

