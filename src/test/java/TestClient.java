import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TestClient {

    String urlApiClient = "http://localhost:8080/";
    String endpointClients = "clientes";
    String endpoitClient = "cliente";
    String emptyClientList = "{}";

    @Test
    @DisplayName("When get all clients. Then list should be empty")
    public void whenGetAllClientsThenListShouldBeEmpty() {
        deleteAllClients();
        given()
                .contentType(ContentType.JSON)
        .when()
                .get(urlApiClient+endpointClients)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body(new IsEqual<>(emptyClientList));
    }

    @Test
    @DisplayName("When registering a client. Then the client must be available in the results")
    public void whenRegisteringAClientThenTheClientMustBeAvailableInTheResults() {
        Client clientToRegister = new Client("Ivan", 5, 33);
        registerClient(clientToRegister)
                .statusCode(HttpStatus.SC_CREATED)
                .body(clientToRegister.getId() + ".nome", equalTo(clientToRegister.getNome()))
                .body(clientToRegister.getId() + ".idade", equalTo(clientToRegister.getIdade()))
                .body(clientToRegister.getId() + ".id", equalTo(clientToRegister.getId()));
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
                .put(urlApiClient+endpoitClient)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body(containsString(bodyOfExpectedResponse));
    }

    @Test
    @DisplayName("When deleting a client, then the information of the removed client should be shown")
    public void deleteAClient() {
        int clientId = 3;
        String bodyOfExpectedResponse = "CLIENTE REMOVIDO: { NOME: Beatriz, IDADE: 24, ID: "+clientId+" }";
        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(urlApiClient+endpoitClient+"/"+clientId)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body(containsString(bodyOfExpectedResponse));
    }

    /*
     * Support function
     * @author Alvaro Biazon Pessoa
     */
    public void deleteAllClients() {
        String endpointDeleteAll = "/apagaTodos";
        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(urlApiClient+endpoitClient+endpointDeleteAll)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body(new IsEqual<>(emptyClientList));
    }

    /*
     * Register a client
     * @param clientToRegister client type object to be registered
     * @return response to the HTTP request made
     * @author Alvaro Biazon Pessoa
     */
    public ValidatableResponse registerClient(Client clientToRegister) {
        return given()
                        .contentType(ContentType.JSON)
                        .body(clientToRegister)
                .when()
                        .post(urlApiClient+endpoitClient)
                .then();
    }

}