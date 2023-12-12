import io.restassured.http.ContentType;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

public class TestClient {

    String clientApiAddress = "http://localhost:8080/";

    @Test
    @DisplayName("When get all customers then list should be empty")
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

}
