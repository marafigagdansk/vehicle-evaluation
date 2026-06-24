public class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private boolean primeiroLogin;

    // cria usuario e suas info (novo, sem id ainda)
    public Usuario(String nome, String email, String senha) {
        this.id = 0;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.primeiroLogin = true;
    }

    // cria usuario carregado do banco (com id)
    public Usuario(int id, String nome, String email, String senha, boolean primeiroLogin) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.primeiroLogin = primeiroLogin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public boolean isPrimeiroLogin() {
        return primeiroLogin;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setPrimeiroLogin(boolean primeiroLogin) {
        this.primeiroLogin = primeiroLogin;
    }
}
