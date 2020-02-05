package nl.jaapcoomans.microframeworks.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import nl.jaapcoomans.demo.microframeworks.todo.domain.TodoService;
import nl.jaapcoomans.demo.microframeworks.todo.peristsence.InMemoryTodoRepository;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class DropWizardApplication extends Application<Configuration> {
    public static void main(String[] args) throws Exception {
        var application = new DropWizardApplication();
        application.run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        this.enableCors(environment);

        var helloWorldController = new HelloWorldController();
        environment.jersey().register(helloWorldController);

        var todoRestController = createTodoBackend();
        environment.jersey().register(todoRestController);
    }

    private void enableCors(Environment environment) {
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "Content-Type");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        // Do not pass preflight requests on to application.
        cors.setInitParameter(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, "false");
    }

    private TodoRestController createTodoBackend() {
        var repository = new InMemoryTodoRepository();
        var todoService = new TodoService(repository);

        return new TodoRestController(todoService, "http://localhost:8080/todos");
    }
}
