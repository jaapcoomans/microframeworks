package nl.jaapcoomans.demo.microframweorks.helidon.mp;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationScoped
@ApplicationPath("/")
public class HelloWorldApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(HelloWorldController.class);
    }
}
