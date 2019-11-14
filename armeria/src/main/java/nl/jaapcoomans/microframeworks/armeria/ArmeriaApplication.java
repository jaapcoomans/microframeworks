package nl.jaapcoomans.microframeworks.armeria;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.AbstractHttpService;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.logging.LoggingService;

import java.util.concurrent.CompletableFuture;

public class ArmeriaApplication {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        ServerBuilder sb = Server.builder();
        sb.http(PORT);

        sb.service("/hello", ArmeriaApplication::helloWorld);
        sb.service("/hello/{name}", ArmeriaApplication::hello);

        Server server = sb.build();
        CompletableFuture<Void> future = server.start();
        future.join();
    }

    private static HttpResponse helloWorld(ServiceRequestContext context, HttpRequest request) {
        return HttpResponse.of("Hello World!");
    }

    private static HttpResponse hello(ServiceRequestContext context, HttpRequest request) {
        String name = context.pathParam("name");
        return HttpResponse.of("Hello, %s!", capitalize(name));
    }

    private static String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}
