import io.restassured.http.ContentType;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class TestClient {

    String clientApiAddress = "http://localhost:8080/";
    String endpoitClient = "cliente";

    @Test
    @DisplayName("When get all clients then list should be empty")
    public void getAllClients() {
        String expectedResponse = "{}";

        given()
                .contentType(ContentType.JSON)
        .when()
                .get(clientApiAddress)
        .then()
                .statusCode(200)
                .assertThat().body(new IsEqual<>(expectedResponse));
    }

    @Test
    @DisplayName("When registering a client, it must be available in the results")
    public void registerClient() {
        String clienteToRegister = "{\n" +
                "  \"id\": 3,\n" +
                "  \"idade\": 24,\n" +
                "  \"nome\": \"Beatriz\",\n" +
                "  \"risco\": 10\n" +
                "}";
        String bodyOfExpectedResponse = "{\"3\":{\"nome\":\"Beatriz\",\"idade\":24,\"id\":3,\"risco\":10}}";
        given()
                .contentType(ContentType.JSON)
                .body(clienteToRegister)
        .when()
                .post(clientApiAddress+endpoitClient)
        .then()
                .statusCode(201)
                .assertThat().body(containsString(bodyOfExpectedResponse));
    }

    @Test
    @DisplayName("When the client is updated, then the new information should appear in the result")
    public void updateClient() {
        String clienttoUpdate = "{\n" +
                "  \"id\": 3,\n" +
                "  \"idade\": 33,\n" +
                "  \"nome\": \"Ivan Carneiro\",\n" +
                "  \"risco\": 1\n" +
                "}";
        String bodyOfExpectedResponse = "{\"3\":{\"nome\":\"Ivan Carneiro\",\"idade\":33,\"id\":3,\"risco\":1}}";
        given()
                .contentType(ContentType.JSON)
                .body(clienttoUpdate)
        .when()
                .put(clientApiAddress+endpoitClient)
        .then()
                .statusCode(200)
                .assertThat().body(containsString(bodyOfExpectedResponse));
    }

}