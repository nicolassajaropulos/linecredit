FROM openjdk:11 as build

MAINTAINER nicolaszavala

COPY target/linecredit-0.0.1-SNAPSHOT.jar linecredit-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "linecredit-0.0.1-SNAPSHOT.jar"]