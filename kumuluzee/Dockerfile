FROM adoptopenjdk/openjdk8:jre8u232-b09-alpine

WORKDIR /opt/application

COPY target/libs/* ./libs/
COPY target/application.jar ./application.jar

ENTRYPOINT java -cp "/opt/application/application.jar:/opt/application/libs/*" com.kumuluz.ee.EeApplication