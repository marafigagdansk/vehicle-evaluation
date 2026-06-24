import java.math.BigDecimal;
import java.math.RoundingMode;

public class Veiculo {

    private int id;
    private String nome;
    private String marca;
    private int anoFabricacao;
    private BigDecimal valor;
    private String detalhesTecnicos;
    private String emailUsuario;
    private BigDecimal valorAvaliado;
    private String raridade;

    // funcao contruir veiculo e suas info (novo, sem id ainda)
    public Veiculo(String nome, String marca, int anoFabricacao,
            BigDecimal valor, String detalhesTecnicos, String emailUsuario) {
        this.id = 0;
        this.nome = nome;
        this.marca = marca;
        this.anoFabricacao = anoFabricacao;
        this.valor = valor;
        this.detalhesTecnicos = detalhesTecnicos;
        this.emailUsuario = emailUsuario;
        this.valorAvaliado = null;
        this.raridade = null;
    }

    // funcao contruir veiculo carregado do banco (com id)
    public Veiculo(int id, String nome, String marca, int anoFabricacao,
            BigDecimal valor, String detalhesTecnicos, String emailUsuario,
            BigDecimal valorAvaliado, String raridade) {
        this.id = id;
        this.nome = nome;
        this.marca = marca;
        this.anoFabricacao = anoFabricacao;
        this.valor = valor;
        this.detalhesTecnicos = detalhesTecnicos;
        this.emailUsuario = emailUsuario;
        this.valorAvaliado = valorAvaliado;
        this.raridade = raridade;
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

    // funcao retorna true caso tenha avaliacao
    public boolean isAvaliado() {
        return valorAvaliado != null && raridade != null;
    }

    // funcao retorna o valor formatado em reais
    public String getValorFormatado() {
        return valor.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    // funcao retorna valor avaliado formatado em reais
    public String getValorAvaliadoFormatado() {
        if (valorAvaliado == null)
            return "N/A";
        return valorAvaliado.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}