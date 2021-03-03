FROM openjdk:11-jre-slim

MAINTAINER arefe.com

COPY target/HumanResourcePlatform-0.0.1-SNAPSHOT.jar recruitment-server-1.0.0.jar

ENTRYPOINT ["java","-jar","/recruitment-server-1.0.0.jar"]






