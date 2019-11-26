package nl.jaapcoomans.demo.microframeworks.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.reactivex.Single;

@Controller("/hello")
public class HelloWorldController {
    @Get(produces = MediaType.TEXT_PLAIN)
    public Single<String> helloWorld() {
        return Single.just("Hello World!");
    }

    @Get(uri = "/{name}", produces = MediaType.TEXT_PLAIN)
    public Single<String> helloWorld(@PathVariable String name) {
        return Single.just(String.format("Hello, %s!", capitalize(name)));
    }

    private String capitalize(String name) {
        if (name.length() < 2) {
            return name.toUpperCase();
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}
