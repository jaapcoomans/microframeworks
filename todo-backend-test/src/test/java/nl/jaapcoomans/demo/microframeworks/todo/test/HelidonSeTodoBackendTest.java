package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
class HelidonSeTodoBackendTest extends BaseTodoBackendTest {
    private static ImageFromDockerfile helidonImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../helidon-se"));

    @Container
    private static GenericContainer helidonContainer = new GenericContainer(helidonImage).withExposedPorts(8080);

    @Override
    GenericContainer getContainer() {
        return helidonContainer;
    }
}
