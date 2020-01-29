package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
class MinijaxTodoBackendTest extends BaseTodoBackendTest{
    private static ImageFromDockerfile minijaxImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../minijax"));

    @Container
    private static GenericContainer minijaxContainer = new GenericContainer(minijaxImage).withExposedPorts(8080);

    @Override
    GenericContainer getContainer() {
        return minijaxContainer;
    }
}
