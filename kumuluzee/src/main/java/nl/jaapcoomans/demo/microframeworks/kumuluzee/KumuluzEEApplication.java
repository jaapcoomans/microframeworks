package nl.jaapcoomans.demo.microframeworks.kumuluzee;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("")
public class KumuluzEEApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(HelloWorldController.class);
        classes.add(TodoController.class);
        return classes;
    }
}
