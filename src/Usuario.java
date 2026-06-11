// Classe que representa um usuário comum do sistema.
// Usuários são criados pelo administrador e podem cadastrar veículos.
public class Usuario {

    // Atributos privados: encapsulamento dos dados do usuário.
    private String nome;
    private String email;
    private String senha;

    // Construtor: cria um usuário com nome, e-mail e senha.
    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // Getters: permitem ler os atributos de fora da classe.
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    // Setters: permitem alterar os atributos de fora da classe.
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
