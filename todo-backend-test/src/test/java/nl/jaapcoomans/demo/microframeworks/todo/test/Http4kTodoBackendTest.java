package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
class Http4kTodoBackendTest extends BaseTodoBackendTest {
    private static ImageFromDockerfile http4kImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../http4k"));

    @Container
    private static GenericContainer http4kContainer = new GenericContainer(http4kImage).withExposedPorts(8080);

    final GenericContainer getContainer() {
        return http4kContainer;
    }
}
