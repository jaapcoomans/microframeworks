package nl.jaapcoomans.demo.microframeworks.helidon.se;

import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;

import java.io.IOException;

public class HelidonApplication {
    public static void main(final String[] args) throws IOException {
        ServerConfiguration serverConfig =
                ServerConfiguration.builder().port(8080).build();

        WebServer server = WebServer.create(serverConfig, createRouting());

        // Try to start the server. If successful, print some info and arrange to
        // print a message at shutdown. If unsuccessful, print the exception.
        server.start()
                .thenAccept(ws -> {
                    System.out.println(
                            "WEB server is up! http://localhost:" + ws.port() + "/hello");
                    ws.whenShutdown().thenRun(()
                            -> System.out.println("WEB server is DOWN. Good bye!"));
                })
                .exceptionally(t -> {
                    System.err.println("Startup failed: " + t.getMessage());
                    t.printStackTrace(System.err);
                    return null;
                });
    }

    private static Routing createRouting() {
        HelloWorldService helloWorldService = new HelloWorldService();

        return Routing.builder()
                .register("/hello", helloWorldService)
                .build();
    }
}
