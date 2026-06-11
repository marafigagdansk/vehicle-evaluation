import java.math.BigDecimal;
import java.math.RoundingMode;

// Classe que representa um veículo no sistema.
// Além dos dados básicos, agora armazena o e-mail do usuário dono do carro
// e os dados de avaliação feita pelo administrador.
public class Veiculo {

    // Atributos privados: encapsulamento dos dados do veículo.
    private String nome;
    private String marca;
    private int anoFabricacao;
    private BigDecimal valor;
    private String detalhesTecnicos;

    // E-mail do usuário que cadastrou este veículo.
    private String emailUsuario;

    // Dados de avaliação do administrador (null enquanto não avaliado).
    private BigDecimal valorAvaliado;
    private String raridade;

    // Construtor: cria um veículo com os dados básicos e vincula ao usuário dono.
    public Veiculo(String nome, String marca, int anoFabricacao,
                   BigDecimal valor, String detalhesTecnicos, String emailUsuario) {
        this.nome = nome;
        this.marca = marca;
        this.anoFabricacao = anoFabricacao;
        this.valor = valor;
        this.detalhesTecnicos = detalhesTecnicos;
        this.emailUsuario = emailUsuario;
        // Avaliação começa como null (ainda não avaliado pelo admin).
        this.valorAvaliado = null;
        this.raridade = null;
    }

    // Getters dos dados básicos do veículo.
    public String getNome() {
        return nome;
    }

    public String getMarca() {
        return marca;
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getDetalhesTecnicos() {
        return detalhesTecnicos;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    // Setters dos dados básicos do veículo.
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setAnoFabricacao(int anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setDetalhesTecnicos(String detalhesTecnicos) {
        this.detalhesTecnicos = detalhesTecnicos;
    }

    // Getters e Setters dos dados de avaliação do administrador.
    public BigDecimal getValorAvaliado() {
        return valorAvaliado;
    }

    public void setValorAvaliado(BigDecimal valorAvaliado) {
        this.valorAvaliado = valorAvaliado;
    }

    public String getRaridade() {
        return raridade;
    }

    public void setRaridade(String raridade) {
        this.raridade = raridade;
    }

    // Retorna true se o veículo já foi avaliado pelo administrador.
    public boolean isAvaliado() {
        return valorAvaliado != null && raridade != null;
    }

    // Método auxiliar para exibir o valor cadastrado com 2 casas decimais.
    public String getValorFormatado() {
        return valor.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    // Método auxiliar para exibir o valor da avaliação com 2 casas decimais.
    public String getValorAvaliadoFormatado() {
        if (valorAvaliado == null) return "N/A";
        return valorAvaliado.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}