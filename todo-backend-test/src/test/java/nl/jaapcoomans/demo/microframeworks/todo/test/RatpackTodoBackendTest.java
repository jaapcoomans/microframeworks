package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
class RatpackTodoBackendTest extends BaseTodoBackendTest {
    private static ImageFromDockerfile ratpackImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../ratpack"));

    @Container
    private static GenericContainer ratpackContainer = new GenericContainer(ratpackImage).withExposedPorts(8080);

    @Override
    GenericContainer getContainer() {
        return ratpackContainer;
    }
}
