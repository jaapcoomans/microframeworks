package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
public class QuarkusTodoBackendTest extends BaseTodoBackendTest {
    private static final ImageFromDockerfile quarkusImage = new ImageFromDockerfile()
            .withFileFromPath(".", Paths.get("../quarkus"));

    @Container
    private static final GenericContainer<?> quarkusContainer = new GenericContainer<>(quarkusImage).withExposedPorts(8080);

    @Override
    GenericContainer<?> getContainer() {
        return quarkusContainer;
    }
}