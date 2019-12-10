package nl.jaapcoomans.demo.microframeworks.pippo;

import ro.pippo.core.Pippo;
import ro.pippo.core.route.RouteContext;

public class PippoApplication {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        Pippo pippo = new Pippo();
        pippo.getServer().setPort(PORT);

        pippo.GET("/hello", PippoApplication::helloWorld);
        pippo.GET("/hello/{name}", PippoApplication::hello);
        pippo.start();
    }

    private static void helloWorld(RouteContext context) {
        context.text().send("Hello World!");
    }

    private static void hello(RouteContext context) {
        var name = context.getParameter("name").toString();
        context.text().send(String.format("Hello, %s!", capitalize(name)));
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}
