package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
class JoobyTodoBackendTest extends BaseTodoBackendTest {
    private static final ImageFromDockerfile joobyImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../jooby"));

    @Container
    private static final GenericContainer<?> joobyContainer = new GenericContainer<>(joobyImage).withExposedPorts(8080);

    @Override
    GenericContainer<?> getContainer() {
        return joobyContainer;
    }
}
