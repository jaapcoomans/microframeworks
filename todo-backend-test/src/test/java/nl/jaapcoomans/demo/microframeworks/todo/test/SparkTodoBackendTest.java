package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
class SparkTodoBackendTest extends BaseTodoBackendTest {
    private static ImageFromDockerfile sparkImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../spark"));

    @Container
    private static GenericContainer sparkContainer = new GenericContainer(sparkImage).withExposedPorts(8080);

    @Override
    GenericContainer getContainer() {
        return sparkContainer;
    }
}
