/*
 * POJO class to build client entity for automated tests
 * @author Alvaro Biazon Pessoa
 */
public class Client {

    private String nome;
    private int id, idade;

    public Client(String nome, int id, int idade) {
        this.nome = nome;
        this.id = id;
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }
}
