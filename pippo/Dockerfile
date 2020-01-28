FROM adoptopenjdk/openjdk13:jre-13.0.1_9-alpine

WORKDIR /opt/application

COPY target/libs/* ./libs/
COPY target/application.jar ./applicatioón.jar

ENTRYPOINT java -cp "/opt/application/application.jar:/opt/application/libs/*" nl.jaapcoomans.demo.microframeworks.pippo.PippoApplication
