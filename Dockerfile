# Java Image
FROM openjdk:8-jdk-slim

# copy the webapp.jar to workdir
COPY webapp/build/libs/webapp.jar webapp.jar

# publish port 8080
EXPOSE 8080

# run java -jar /webapp.jar
ENTRYPOINT ["java","-jar","/webapp.jar"]
