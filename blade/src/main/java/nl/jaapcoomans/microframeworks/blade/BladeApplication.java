package nl.jaapcoomans.microframeworks.blade;

import com.blade.Blade;
import com.blade.mvc.RouteContext;

public class BladeApplication {
    public static void main(String[] args) {
        Blade.of()
                .get("/hello", BladeApplication::helloWorld)
                .get("/hello/:name", BladeApplication::hello)
                .start(BladeApplication.class, args);
    }

    private static void helloWorld(RouteContext context) {
        context.text("Hello World!");
    }

    private static void hello(RouteContext context) {
        String name = context.pathString("name");
        context.text(String.format("Hello, %s!", capitalize(name)));
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}
