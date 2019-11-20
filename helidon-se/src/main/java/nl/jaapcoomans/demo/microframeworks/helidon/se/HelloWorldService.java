package nl.jaapcoomans.demo.microframeworks.helidon.se;

import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

public class HelloWorldService implements Service {
    @Override
    public void update(Routing.Rules rules) {
        rules
                .get("/", this::helloWorld)
                .get("/{name}", this::hello);
    }

    private void helloWorld(ServerRequest request, ServerResponse response) {
        response.send("Hello World!");
    }

    private void hello(ServerRequest request, ServerResponse response) {
        response.send(String.format("Hello, %s!", capitalize(request.path().param("name"))));
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}
