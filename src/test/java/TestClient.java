import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;

public class TestClient {

    private static final String URL_API_CLIENT = "http://localhost:8080/";
    private static final String ENDPOINT_CLIENTS = "clientes";
    private static final String ENDPOINT_CLIENT = "cliente";
    private static final String EMPTY_CLIENT_LIST = "{}";
    private static final String FILE_PATH = "src/test/java/";
    private static final String REGISTER_CLIENT_JSON_SCHEMA_FILE_NAME = "RegisterClientJsonSchema.json";

    @Test
    @DisplayName("When get a client. Then the client should be shown in the result")
    public void whenGetAClientThenTheClientShouldBeShownInTheResult() {
        Client clientToGet = new Client("Petrus", 10, 2);
        registerClient(clientToGet);
        given()
                .contentType(ContentType.JSON)
        .when()
                .get(URL_API_CLIENT + ENDPOINT_CLIENT + "/" + clientToGet.getId())
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("nome", equalTo(clientToGet.getNome()))
                .body("idade", equalTo(clientToGet.getIdade()))
                .body("id", equalTo(clientToGet.getId()));
    }

    @Test
    @DisplayName("When get all clients. Then the clients list must be empty")
    public void whenGetAllClientsThenTheClientsListMustBeEmpty() {
        deleteAllClients();
        given()
                .contentType(ContentType.JSON)
        .when()
                .get(URL_API_CLIENT + ENDPOINT_CLIENTS)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo(EMPTY_CLIENT_LIST));
    }

    @Test
    @DisplayName("When registering a client. Then the client should be shown in the result")
    public void whenRegisteringAClientThenTheClientShouldBeShownInTheResult() {
        Client clientToRegister = new Client("Eduardo", 13, 30);
        String jsonFileContent = readFile(FILE_PATH, REGISTER_CLIENT_JSON_SCHEMA_FILE_NAME);
        registerClient(clientToRegister)
                .statusCode(HttpStatus.SC_CREATED)
                .body(JsonSchemaValidator.matchesJsonSchema(jsonFileContent))
                .body(clientToRegister.getId() + ".nome", equalTo(clientToRegister.getNome()))
                .body(clientToRegister.getId() + ".idade", equalTo(clientToRegister.getIdade()))
                .body(clientToRegister.getId() + ".id", equalTo(clientToRegister.getId()));
    }

    @Test
    @DisplayName("When a client is updated. Then the updated client should be shown in the result")
    public void whenAClientIsUpdatedThenTheUpdatedClientShouldBeShownInTheResult() {
        Client clientToUpdate = new Client("Mariana", 2, 23);
        registerClient(clientToUpdate);
        clientToUpdate.setNome("Wilson Pessoa");
        clientToUpdate.setIdade(78);
        given()
                .contentType(ContentType.JSON)
                .body(clientToUpdate)
        .when()
                .put(URL_API_CLIENT + ENDPOINT_CLIENT)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body(clientToUpdate.getId() + ".nome", equalTo(clientToUpdate.getNome()))
                .body(clientToUpdate.getId() + ".idade", equalTo(clientToUpdate.getIdade()));
    }

    @Test
    @DisplayName("When deleting a client. Then the removed client should be shown")
    public void whenDeletingAClientThenTheRemovedClientShouldBeShown() {
        Client clientToDelete = new Client("Beatriz", 6, 24);
        registerClient(clientToDelete);
        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(URL_API_CLIENT + ENDPOINT_CLIENT + "/" + clientToDelete.getId())
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo("CLIENTE REMOVIDO: { NOME: " + clientToDelete.getNome() + ", IDADE: " + clientToDelete.getIdade() + ", ID: " + clientToDelete.getId() + " }"));
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
                .delete(URL_API_CLIENT + ENDPOINT_CLIENT + endpointDeleteAll)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo(EMPTY_CLIENT_LIST));
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
                        .post(URL_API_CLIENT + ENDPOINT_CLIENT)
                .then();
    }

    /*
     * Read a file
     * @param filePath path where the file is
     * @param fileName name of the file to be read
     * @return the contents of the file
     * @author Alvaro Biazon Pessoa
     */
    public String readFile(String filePath, String fileName) {
        String content = null;
        try {
            content = FileUtils.readFileToString(new File(filePath + fileName), "UTF-8");
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return content;
    }

}