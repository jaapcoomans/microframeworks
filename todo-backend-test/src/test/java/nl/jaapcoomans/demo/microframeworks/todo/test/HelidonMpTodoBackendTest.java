package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
class HelidonMpTodoBackendTest extends BaseTodoBackendTest {
    private static final ImageFromDockerfile helidonImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../helidon-mp"));

    @Container
    private static final GenericContainer<?> helidonContainer = new GenericContainer<>(helidonImage).withExposedPorts(8080);

    @Override
    GenericContainer<?> getContainer() {
        return helidonContainer;
    }
}
