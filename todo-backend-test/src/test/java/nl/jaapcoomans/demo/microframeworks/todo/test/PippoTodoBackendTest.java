package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
class PippoTodoBackendTest extends BaseTodoBackendTest {
    private static ImageFromDockerfile pippoImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../pippo"));

    @Container
    private static GenericContainer pippoContainer = new GenericContainer(pippoImage).withExposedPorts(8080);

    @Override
    GenericContainer getContainer() {
        return pippoContainer;
    }
}
