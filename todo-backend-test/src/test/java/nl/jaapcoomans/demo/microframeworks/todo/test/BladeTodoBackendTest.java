package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
class BladeTodoBackendTest extends BaseTodoBackendTest {
    private static ImageFromDockerfile bladeImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../blade"));

    @Container
    private static GenericContainer bladeContainer = new GenericContainer(bladeImage).withExposedPorts(8080);

    @Override
    GenericContainer getContainer() {
        return bladeContainer;
    }
}
