FROM adoptopenjdk/openjdk13:jre-13.0.1_9-alpine

WORKDIR /opt/application

COPY target/libs/* ./libs/
COPY target/application.jar ./application.jar

ENTRYPOINT java -cp "/opt/application/application.jar:/opt/application/libs/*" nl.jaapcoomans.microframeworks.spark.SparkApplication