FROM eclipse-temurin:21-jre-alpine
COPY target/*jar-with-dependencies.jar /usr/app/database-intro.jar
EXPOSE 8080
CMD ["java","-cp","/usr/app/database-intro.jar", "es.fplumara.dam1.alumnos.Main"]