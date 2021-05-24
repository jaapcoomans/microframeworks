package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
class JavalinTodoTest extends BaseTodoBackendTest {
    private static final ImageFromDockerfile javalinImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../javalin"));

    @Container
    private static final GenericContainer<?> javalinContainer = new GenericContainer<>(javalinImage).withExposedPorts(7000);

    final GenericContainer<?>getContainer() {
        return javalinContainer;
    }
}
