FROM openjdk:17
COPY target/studentgrades*.jar /usr/src/studentgrades.jar
COPY src/main/resources/application.properties /opt/conf/application.properties
CMD ["java", "-jar", "/usr/src/studentgrades.jar", "--spring.config.location=file:/opt/conf/application.properties"]

