import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

/**
 * Camada de acesso ao banco de dados SQLite.
 * Responsável por criar as tabelas e realizar todas as operações de
 * leitura e escrita (inserir, atualizar, deletar, listar).
 */
public class Database {

    // Caminho do arquivo do banco de dados
    private static final String URL = "jdbc:sqlite:database.db";

    private static Connection conexao = null;

    // ---------------------------------------------------------------
    // Conexão e inicialização
    // ---------------------------------------------------------------

    /**
     * Abre a conexão com o banco e cria as tabelas se ainda não existirem.
     * Deve ser chamado uma única vez no início do programa.
     */
    public static void inicializar() {
        try {
            conexao = DriverManager.getConnection(URL);
            criarTabelas();
            System.out.println("[DB] Banco de dados conectado: database.db");
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao conectar ao banco: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Fecha a conexão com o banco de dados.
     * Deve ser chamado ao encerrar o programa.
     */
    public static void fechar() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("[DB] Conexão encerrada.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao fechar conexão: " + e.getMessage());
        }
    }

    /**
     * Cria as tabelas 'usuarios' e 'veiculos' caso não existam.
     */
    private static void criarTabelas() throws SQLException {
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id             INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome           TEXT    NOT NULL," +
                "email          TEXT    NOT NULL UNIQUE," +
                "senha          TEXT    NOT NULL," +
                "primeiro_login INTEGER NOT NULL DEFAULT 1" +
                ");";

        String sqlVeiculos = "CREATE TABLE IF NOT EXISTS veiculos (" +
                "id                INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome              TEXT    NOT NULL," +
                "marca             TEXT    NOT NULL," +
                "ano_fabricacao    INTEGER NOT NULL," +
                "valor             TEXT    NOT NULL," +
                "detalhes_tecnicos TEXT," +
                "email_usuario     TEXT    NOT NULL," +
                "valor_avaliado    TEXT," +
                "raridade          TEXT" +
                ");";

        try (Statement stmt = conexao.createStatement()) {
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlVeiculos);
        }
    }

    // ---------------------------------------------------------------
    // Operações de Usuários
    // ---------------------------------------------------------------

    /**
     * Insere um novo usuário no banco e atualiza o campo id do objeto.
     */
    public static void inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email, senha, primeiro_login) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setInt(4, usuario.isPrimeiroLogin() ? 1 : 0);
            stmt.executeUpdate();

            // Recupera o ID gerado pelo banco e atribui ao objeto
            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    usuario.setId(chaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao inserir usuário: " + e.getMessage());
        }
    }

    /**
     * Retorna todos os usuários cadastrados no banco.
     */
    public static ArrayList<Usuario> listarUsuarios() {
        ArrayList<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nome, email, senha, primeiro_login FROM usuarios";

        try (Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getInt("primeiro_login") == 1));
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao listar usuários: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Atualiza a senha e o flag de primeiro login de um usuário no banco.
     */
    public static void atualizarSenhaUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET senha = ?, primeiro_login = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getSenha());
            stmt.setInt(2, usuario.isPrimeiroLogin() ? 1 : 0);
            stmt.setInt(3, usuario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao atualizar senha do usuário: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Operações de Veículos
    // ---------------------------------------------------------------

    /**
     * Insere um novo veículo no banco e atualiza o campo id do objeto.
     */
    public static void inserirVeiculo(Veiculo veiculo) {
        String sql = "INSERT INTO veiculos " +
                "(nome, marca, ano_fabricacao, valor, detalhes_tecnicos, email_usuario, valor_avaliado, raridade) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, veiculo.getNome());
            stmt.setString(2, veiculo.getMarca());
            stmt.setInt(3, veiculo.getAnoFabricacao());
            stmt.setString(4, veiculo.getValor().toPlainString());
            stmt.setString(5, veiculo.getDetalhesTecnicos());
            stmt.setString(6, veiculo.getEmailUsuario());
            stmt.setString(7, veiculo.getValorAvaliado() != null ? veiculo.getValorAvaliado().toPlainString() : null);
            stmt.setString(8, veiculo.getRaridade());
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    veiculo.setId(chaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao inserir veículo: " + e.getMessage());
        }
    }

    /**
     * Retorna todos os veículos de um determinado usuário (pelo e-mail).
     */
    public static ArrayList<Veiculo> listarVeiculosPorUsuario(String emailUsuario) {
        ArrayList<Veiculo> lista = new ArrayList<>();
        String sql = "SELECT id, nome, marca, ano_fabricacao, valor, detalhes_tecnicos, " +
                "email_usuario, valor_avaliado, raridade " +
                "FROM veiculos WHERE LOWER(email_usuario) = LOWER(?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, emailUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String valorAvaliadoStr = rs.getString("valor_avaliado");
                    BigDecimal valorAvaliado = (valorAvaliadoStr != null && !valorAvaliadoStr.isEmpty())
                            ? new BigDecimal(valorAvaliadoStr)
                            : null;

                    lista.add(new Veiculo(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getInt("ano_fabricacao"),
                            new BigDecimal(rs.getString("valor")),
                            rs.getString("detalhes_tecnicos"),
                            rs.getString("email_usuario"),
                            valorAvaliado,
                            rs.getString("raridade")));
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao listar veículos: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Retorna todos os veículos cadastrados no banco (para uso do admin).
     */
    public static ArrayList<Veiculo> listarTodosVeiculos() {
        ArrayList<Veiculo> lista = new ArrayList<>();
        String sql = "SELECT id, nome, marca, ano_fabricacao, valor, detalhes_tecnicos, " +
                "email_usuario, valor_avaliado, raridade FROM veiculos";

        try (Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String valorAvaliadoStr = rs.getString("valor_avaliado");
                BigDecimal valorAvaliado = (valorAvaliadoStr != null && !valorAvaliadoStr.isEmpty())
                        ? new BigDecimal(valorAvaliadoStr)
                        : null;

                lista.add(new Veiculo(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("marca"),
                        rs.getInt("ano_fabricacao"),
                        new BigDecimal(rs.getString("valor")),
                        rs.getString("detalhes_tecnicos"),
                        rs.getString("email_usuario"),
                        valorAvaliado,
                        rs.getString("raridade")));
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao listar todos os veículos: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Atualiza os dados editáveis de um veículo (nome, marca, ano, valor, detalhes).
     */
    public static void atualizarVeiculo(Veiculo veiculo) {
        String sql = "UPDATE veiculos SET nome = ?, marca = ?, ano_fabricacao = ?, " +
                "valor = ?, detalhes_tecnicos = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, veiculo.getNome());
            stmt.setString(2, veiculo.getMarca());
            stmt.setInt(3, veiculo.getAnoFabricacao());
            stmt.setString(4, veiculo.getValor().toPlainString());
            stmt.setString(5, veiculo.getDetalhesTecnicos());
            stmt.setInt(6, veiculo.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao atualizar veículo: " + e.getMessage());
        }
    }

    /**
     * Deleta um veículo do banco pelo seu id.
     */
    public static void deletarVeiculo(Veiculo veiculo) {
        String sql = "DELETE FROM veiculos WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, veiculo.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao deletar veículo: " + e.getMessage());
        }
    }

    /**
     * Salva a avaliação (valor avaliado e raridade) de um veículo no banco.
     */
    public static void salvarAvaliacao(Veiculo veiculo) {
        String sql = "UPDATE veiculos SET valor_avaliado = ?, raridade = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, veiculo.getValorAvaliado() != null ? veiculo.getValorAvaliado().toPlainString() : null);
            stmt.setString(2, veiculo.getRaridade());
            stmt.setInt(3, veiculo.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao salvar avaliação: " + e.getMessage());
        }
    }

    /**
     * Verifica se já existe um usuário cadastrado com o e-mail informado.
     */
    public static boolean emailJaCadastrado(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE LOWER(email) = LOWER(?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao verificar e-mail: " + e.getMessage());
        }

        return false;
    }
}
