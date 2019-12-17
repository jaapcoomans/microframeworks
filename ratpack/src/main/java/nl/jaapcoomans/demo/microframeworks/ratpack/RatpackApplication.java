package nl.jaapcoomans.demo.microframeworks.ratpack;

import ratpack.handling.Context;
import ratpack.server.RatpackServer;
import ratpack.server.RatpackServerSpec;
import ratpack.server.ServerConfig;

public class RatpackApplication {
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        RatpackServer.start(RatpackApplication::init);
    }

    private static void init(RatpackServerSpec server) {
        server.serverConfig(ServerConfig.embedded().port(PORT))
                .handlers(chain -> chain
                        .get("hello", RatpackApplication::helloWorld)
                        .get("hello/:name", RatpackApplication::hello)
                );
    }

    private static void helloWorld(Context context) {
        context.getResponse()
                .contentType("text/plain")
                .send("Hello World");
    }

    private static void hello(Context context) {
        var name = context.getPathTokens().get("name");
        context.getResponse()
                .contentType("text/plain")
                .send(String.format("Hello, %s!", capitalize(name)));
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}
