package nl.jaapcoomans.microframeworks.helidon.mp;

import io.helidon.microprofile.server.Server;

public class HelidonMpApplication {
    public static void main(String[] args) {
        Server.builder()
                .addApplication(new HelloWorldApplication())
                .addApplication(new TodoApplication())
                .build()
                .start();
    }
}
