package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
class KumuluzEeTodoBackendTest extends BaseTodoBackendTest {
    private static ImageFromDockerfile kumuluzEeImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../kumuluzee"));

    @Container
    private static GenericContainer kumuluzEeContainer = new GenericContainer(kumuluzEeImage).withExposedPorts(8080);

    @Override
    GenericContainer getContainer() {
        return kumuluzEeContainer;
    }
}
