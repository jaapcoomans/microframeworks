package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
class ArmeriaTodoBackendTest extends BaseTodoBackendTest {
    private static ImageFromDockerfile armeriaImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../armeria"));

    @Container
    private static GenericContainer armeriaContainer = new GenericContainer(armeriaImage).withExposedPorts(8080);

    @Override
    GenericContainer getContainer() {
        return armeriaContainer;
    }
}
