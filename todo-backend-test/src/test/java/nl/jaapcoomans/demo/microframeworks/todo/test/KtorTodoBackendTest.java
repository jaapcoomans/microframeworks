package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
class KtorTodoBackendTest extends BaseTodoBackendTest {
    private static ImageFromDockerfile ktorImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../ktor"));

    @Container
    private static GenericContainer ktorContainer = new GenericContainer(ktorImage).withExposedPorts(8080);

    @Override
    GenericContainer getContainer() {
        return ktorContainer;
    }
}
