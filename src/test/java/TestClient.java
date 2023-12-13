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
    @DisplayName("When the client is updated. Then the new information should appear in the result")
    public void whenTheClientIsUpdatedThenTheNewInformationShouldAppearInTheResult() {
        Client clientToUpdate = new Client("Mariana", 2, 23);
        registerClient(clientToUpdate);
        clientToUpdate.setNome("Wilson Pessoa");
        clientToUpdate.setIdade(78);
        given()
                .contentType(ContentType.JSON)
                .body(clientToUpdate)
        .when()
                .put(urlApiClient+endpoitClient)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body(clientToUpdate.getId() + ".nome", equalTo(clientToUpdate.getNome()))
                .body(clientToUpdate.getId() + ".idade", equalTo(clientToUpdate.getIdade()));
    }

    @Test
    @DisplayName("When deleting a client. Then the information of the removed client should be shown")
    public void whenDeletingAClientThenTheInformationOfTheRemovedClientShouldBeShown() {
        Client clientToDelete = new Client("Beatriz", 6, 24);
        registerClient(clientToDelete);
        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(urlApiClient+endpoitClient+"/"+clientToDelete.getId())
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo("CLIENTE REMOVIDO: { NOME: "+clientToDelete.getNome()+", IDADE: "+clientToDelete.getIdade()+", ID: "+clientToDelete.getId()+" }"));
    }

    /*
     * Support function to delete all clients
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