package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
public class MicronautTodoBackendTest extends BaseTodoBackendTest {
    private static ImageFromDockerfile micronautImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../micronaut"));

    @Container
    private static GenericContainer micronautContainer = new GenericContainer(micronautImage).withExposedPorts(8080);

    @Override
    GenericContainer getContainer() {
        return micronautContainer;
    }
}
