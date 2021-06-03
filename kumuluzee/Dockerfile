FROM adoptopenjdk/openjdk15:jre-15.0.2_7-alpine

WORKDIR /opt/application

COPY target/libs/* ./libs/
COPY target/application.jar ./application.jar

ENTRYPOINT java --illegal-access=warn -cp "/opt/application/application.jar:/opt/application/libs/*" com.kumuluz.ee.EeApplication