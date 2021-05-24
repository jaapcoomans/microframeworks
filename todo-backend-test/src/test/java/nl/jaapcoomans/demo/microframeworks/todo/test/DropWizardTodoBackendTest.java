package nl.jaapcoomans.demo.microframeworks.todo.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

@Testcontainers
class DropWizardTodoBackendTest extends BaseTodoBackendTest {
    private static final ImageFromDockerfile dropWizardImage = new ImageFromDockerfile().withFileFromPath(".", Paths.get("../dropwizard"));

    @Container
    private static final GenericContainer<?> dropWizardContainer = new GenericContainer<>(dropWizardImage).withExposedPorts(8080);

    @Override
    GenericContainer<?> getContainer() {
        return dropWizardContainer;
    }
}
