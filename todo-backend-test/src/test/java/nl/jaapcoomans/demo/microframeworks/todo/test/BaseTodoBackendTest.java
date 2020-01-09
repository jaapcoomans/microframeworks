package nl.jaapcoomans.demo.microframeworks.todo.test;

import io.restassured.RestAssured;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.GenericContainer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
abstract class BaseTodoBackendTest {
    private static final String API_ROOT = "/todos";

    abstract GenericContainer getContainer();

    String getBaseUrl() {
        return "http://" + getContainer().getContainerIpAddress() + ":" + getContainer().getFirstMappedPort();
    }

    String replacePort(String url) {
        return url.replaceFirst("[1-9][0-9]{1,4}", getContainer().getFirstMappedPort().toString());
    }

    @Test
    @Order(1)
    @DisplayName("the api root responds to a GET (i.e. the server is up and accessible, CORS headers are set up)")
    final void getApiRoot() {
        RestAssured.given()
                .header("Access-Control-Request-Headers", HttpHeaders.CONTENT_TYPE)
                .header("Access-Control-Request-Method", "GET")
                .header("Origin", getBaseUrl())
                .baseUri(getBaseUrl())
                .when().options(API_ROOT)
                .then()
                .header("Access-Control-Allow-Origin", notNullValue())
                .header("Access-Control-Allow-Headers", equalToIgnoringCase(HttpHeaders.CONTENT_TYPE))
                .header("Access-Control-Allow-Methods", "GET");

        RestAssured.given()
                .header(HttpHeaders.ACCEPT, "application/json")
                .baseUri(getBaseUrl())
                .when().get(API_ROOT)
                .then()
                .statusCode(200)
                .body("$", empty());
    }

    @Test
    @Order(2)
    @DisplayName("the api root responds to a POST with the todo which was posted to it")
    void postApiRoot() {
        RestAssured.given()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .baseUri(getBaseUrl())
                .body("{\"title\": \"Test 123\", \"order\": 1 }")
                .when().post(API_ROOT)
                .then()
                .statusCode(200)
                .body("id", not(emptyOrNullString()))
                .body("title", equalTo("Test 123"))
                .body("url", startsWith("http://"))
                .body("order", equalTo(1))
                .body("completed", equalTo(false));
    }

    @Test
    @Order(3)
    @DisplayName("the api root responds successfully to a DELETE, after a DELETE the api root responds to a GET with a JSON representation of an empty array")
    void deleteApiRoot() {
        RestAssured.given()
                .baseUri(getBaseUrl())
                .when().delete(API_ROOT)
                .then()
                .statusCode(204);
    }

    @Test
    @Order(4)
    @DisplayName("after a DELETE the api root responds to a GET with a JSON representation of an empty array")
    void afterDeleteApiRoot() {
        RestAssured.given()
                .header(HttpHeaders.ACCEPT, "application/json")
                .baseUri(getBaseUrl())
                .when().get(API_ROOT)
                .then()
                .statusCode(200)
                .body("$", empty());
    }

    @Test
    @Order(5)
    @DisplayName("adds a new todo to the list of todos at the root url")
    void addTodo() {
        RestAssured.given()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .baseUri(getBaseUrl())
                .body("{\"title\": \"Test 1\", \"order\": 1 }")
                .when().post(API_ROOT)
                .then()
                .statusCode(200);

        RestAssured.given()
                .header(HttpHeaders.ACCEPT, "application/json")
                .baseUri(getBaseUrl())
                .when().get(API_ROOT)
                .then()
                .statusCode(200)
                .body("$", hasSize(1));
    }

    @Test
    @Order(6)
    @DisplayName("sets up a new todo as initially not completed")
    void todoInitiallyNotCompleted() {
        RestAssured.given()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .baseUri(getBaseUrl())
                .body("{\"title\": \"Test 2\", \"order\": 2 }")
                .when().post(API_ROOT)
                .then()
                .statusCode(200)
                .body("completed", equalTo(false));
    }

    @Test
    @Order(7)
    @DisplayName("each new todo has a url")
    void newTodoHasUrl() {
        RestAssured.given()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .baseUri(getBaseUrl())
                .body("{\"title\": \"Test 3\", \"order\": 3 }")
                .when().post(API_ROOT)
                .then()
                .statusCode(200)
                .body("url", startsWith("http://"));
    }

    @Test
    @Order(8)
    @DisplayName("each new todo has a url, which returns a todo")
    void newTodoHasUrlWhichReturnsTodo() {
        var response = RestAssured.given()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .baseUri(getBaseUrl())
                .body("{\"title\": \"Test 4\", \"order\": 4 }")
                .when().post(API_ROOT);

        response
                .then()
                .statusCode(200)
                .body("url", startsWith("http://"));

        var url = response.body().jsonPath().get("url").toString();

        RestAssured.given()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .baseUri(replacePort(url))
                .when().get()
                .then()
                .statusCode(200)
                .body("id", not(emptyOrNullString()))
                .body("title", equalTo("Test 4"))
                .body("url", equalTo(url))
                .body("order", equalTo(4))
                .body("completed", equalTo(false));
    }

    @Test
    @Order(9)
    @DisplayName("can navigate from a list of todos to an individual todo via urls")
    void navigateFromListToIndividual() {
        var url = RestAssured.given()
                .header(HttpHeaders.ACCEPT, "application/json")
                .baseUri(getBaseUrl())
                .when().get(API_ROOT)
                .body().jsonPath().get("[0].url").toString();

        RestAssured.given()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .baseUri(replacePort(url))
                .when().get()
                .then()
                .statusCode(200)
                .body("id", not(emptyOrNullString()))
                .body("title", not(emptyOrNullString()))
                .body("url", equalTo(url))
                .body("order", notNullValue())
                .body("completed", equalTo(false));
    }

    @Test
    @Order(10)
    @DisplayName("can change the todo's title by PATCHing to the todo's url")
    void changeTitle() {
        var url = RestAssured.given()
                .header(HttpHeaders.ACCEPT, "application/json")
                .baseUri(getBaseUrl())
                .when().get(API_ROOT)
                .body().jsonPath().get("[0].url").toString();

        RestAssured.given()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .baseUri(replacePort(url))
                .body("{\"title\": \"Changed\"}")
                .when().patch()
                .then()
                .statusCode(200)
                .body("title", equalTo("Changed"));
    }

    @Test
    @Order(11)
    @DisplayName("can change the todo's completedness by PATCHing to the todo's url")
    void changeCompleted() {
        var url = RestAssured.given()
                .header(HttpHeaders.ACCEPT, "application/json")
                .baseUri(getBaseUrl())
                .when().get(API_ROOT)
                .body().jsonPath().get("[0].url").toString();

        RestAssured.given()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .baseUri(replacePort(url))
                .body("{\"completed\": true}")
                .when().patch()
                .then()
                .statusCode(200)
                .body("completed", equalTo(true));
    }

    @Test
    @Order(12)
    @DisplayName("changes to a todo are persisted and show up when re-fetching the todo")
    void changesArePersisted() {
        var url = RestAssured.given()
                .header(HttpHeaders.ACCEPT, "application/json")
                .baseUri(getBaseUrl())
                .when().get(API_ROOT)
                .body().jsonPath().get("[0].url").toString();

        RestAssured.given()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .baseUri(replacePort(url))
                .body("{\"title\": \"Persisted\"}")
                .when().patch()
                .then().statusCode(200);

        RestAssured.given()
                .header(HttpHeaders.ACCEPT, "application/json")
                .baseUri(replacePort(url))
                .when().get()
                .then().body("title", equalTo("Persisted"));
    }

    @Test
    @Order(13)
    @DisplayName("can delete a todo making a DELETE request to the todo's url")
    void deleteTodo() {
        var url = RestAssured.given()
                .header(HttpHeaders.ACCEPT, "application/json")
                .baseUri(getBaseUrl())
                .when().get(API_ROOT)
                .body().jsonPath().get("[0].url").toString();

        RestAssured.given()
                .baseUri(replacePort(url))
                .when().delete()
                .then().statusCode(204);

        RestAssured.given()
                .header(HttpHeaders.ACCEPT, "application/json")
                .baseUri(replacePort(url))
                .when().get()
                .then().statusCode(404);
    }
}
