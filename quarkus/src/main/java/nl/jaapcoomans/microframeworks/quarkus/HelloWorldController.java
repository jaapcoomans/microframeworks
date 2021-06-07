package nl.jaapcoomans.microframeworks.quarkus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class HelloWorldController {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return "Hello World!";
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@PathParam("name") String name) {
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
