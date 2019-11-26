package nl.jaapcoomans.demo.microframeworks.jooby;

import io.jooby.Context;
import io.jooby.ServerOptions;

import static io.jooby.Jooby.runApp;

public class JoobyApplication {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        runApp(args, app -> {
            app.setServerOptions(new ServerOptions().setPort(PORT));

            app.get("/hello", JoobyApplication::helloWorld);
            app.get("/hello/{name}", JoobyApplication::hello);
        });
    }

    private static String helloWorld(Context context) {
        return "Hello World!";
    }

    private static String hello(Context context) {
        var name = context.path("name").value();
        return String.format("Hello, %s!", capitalize(name));
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}
