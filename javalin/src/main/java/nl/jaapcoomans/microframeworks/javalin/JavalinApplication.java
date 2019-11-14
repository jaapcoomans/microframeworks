package nl.jaapcoomans.microframeworks.javalin;

import io.javalin.Javalin;
import io.javalin.http.Context;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class JavalinApplication {
    private static final int PORT = 7000;

    public static void main(String[] args) {
        Javalin.create()
                .routes(() -> {
                    path("/hello", () -> {
                        get(JavalinApplication::helloWorld);
                        get("/:name", JavalinApplication::hello);
                    });
                })
                .start(PORT);
    }

    private static void hello(Context context) {
        context.result(String.format("Hello, %s!", capitalize(context.pathParam("name"))));
    }

    private static void helloWorld(Context context) {
        context.result("Hello World!");
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}
