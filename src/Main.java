import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.Scanner;

// Classe principal do programa.
// Contém o fluxo de login (Admin/Usuário) e todos os menus do sistema.
public class Main {

    // Scanner compartilhado para leitura de entradas do terminal.
    private static final Scanner scanner = new Scanner(System.in);

    // Lista global de veículos (cada veículo sabe a qual usuário pertence).
    private static final ArrayList<Veiculo> veiculos = new ArrayList<>();

    // Lista de usuários criados pelo administrador.
    private static final ArrayList<Usuario> usuarios = new ArrayList<>();

    // Credenciais fixas do administrador.
    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_SENHA  = "admin123";

    // Número máximo de tentativas de login antes de voltar ao menu inicial.
    private static final int MAX_TENTATIVAS_LOGIN = 3;

    // =========================================================================
    // PONTO DE ENTRADA
    // =========================================================================

    public static void main(String[] args) {
        int opcao;

        do {
            mostrarTelaInicial();
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    // Fluxo de login do administrador.
                    fazerLoginAdmin();
                    break;
                case 2:
                    // Fluxo de login do usuário comum.
                    fazerLoginUsuario();
                    break;
                case 3:
                    mostrarCabecalho("ENCERRANDO O PROGRAMA");
                    System.out.println("Programa finalizado. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida. Escolha entre 1 e 3.");
            }

        } while (opcao != 3);

        scanner.close();
    }

    // Exibe a tela inicial de escolha de perfil.
    private static void mostrarTelaInicial() {
        mostrarCabecalho("SISTEMA DE CADASTRO DE VEÍCULOS");
        System.out.println("1. Entrar como Administrador");
        System.out.println("2. Entrar como Usuário");
        System.out.println("3. Sair");
        System.out.println("=".repeat(50));
    }

    // =========================================================================
    // LOGIN DO ADMINISTRADOR
    // =========================================================================

    // Realiza até MAX_TENTATIVAS_LOGIN tentativas de login como admin.
    private static void fazerLoginAdmin() {
        mostrarCabecalho("LOGIN — ADMINISTRADOR");

        for (int tentativa = 1; tentativa <= MAX_TENTATIVAS_LOGIN; tentativa++) {
            System.out.print("Login: ");
            String login = scanner.nextLine().trim();
            System.out.print("Senha: ");
            String senha = scanner.nextLine().trim();

            if (login.equals(ADMIN_LOGIN) && senha.equals(ADMIN_SENHA)) {
                System.out.println("Login realizado com sucesso!");
                menuAdmin();
                return;
            }

            int restantes = MAX_TENTATIVAS_LOGIN - tentativa;
            if (restantes > 0) {
                System.out.println("Login ou senha incorretos. " + restantes + " tentativa(s) restante(s).");
            } else {
                System.out.println("Número máximo de tentativas atingido. Voltando ao menu inicial.");
            }
        }
    }

    // =========================================================================
    // LOGIN DO USUÁRIO COMUM
    // =========================================================================

    // Realiza até MAX_TENTATIVAS_LOGIN tentativas de login como usuário.
    private static void fazerLoginUsuario() {
        mostrarCabecalho("LOGIN — USUÁRIO");

        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado. Solicite ao administrador que crie seu acesso.");
            return;
        }

        for (int tentativa = 1; tentativa <= MAX_TENTATIVAS_LOGIN; tentativa++) {
            System.out.print("E-mail: ");
            String email = scanner.nextLine().trim();
            System.out.print("Senha: ");
            String senha = scanner.nextLine().trim();

            Usuario encontrado = buscarUsuarioPorCredenciais(email, senha);

            if (encontrado != null) {
                System.out.println("Bem-vindo(a), " + encontrado.getNome() + "!");
                menuUsuario(encontrado);
                return;
            }

            int restantes = MAX_TENTATIVAS_LOGIN - tentativa;
            if (restantes > 0) {
                System.out.println("E-mail ou senha incorretos. " + restantes + " tentativa(s) restante(s).");
            } else {
                System.out.println("Número máximo de tentativas atingido. Voltando ao menu inicial.");
            }
        }
    }

    // Busca um usuário pelo e-mail e senha. Retorna null se não encontrado.
    private static Usuario buscarUsuarioPorCredenciais(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                return u;
            }
        }
        return null;
    }

    // =========================================================================
    // MENU DO ADMINISTRADOR
    // =========================================================================

    // Exibe o menu do admin e processa as opções escolhidas.
    private static void menuAdmin() {
        int opcao;

        do {
            mostrarMenuAdmin();
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    cadastrarUsuario();
                    break;
                case 2:
                    selecionarUsuarioParaAvaliar();
                    break;
                case 3:
                    mostrarCabecalho("SAINDO DO PAINEL ADMIN");
                    System.out.println("Sessão encerrada.");
                    break;
                default:
                    System.out.println("Opção inválida. Escolha entre 1 e 3.");
            }

        } while (opcao != 3);
    }

    // Exibe o cabeçalho do menu do administrador.
    private static void mostrarMenuAdmin() {
        mostrarCabecalho("PAINEL DO ADMINISTRADOR");
        System.out.println("1. Cadastrar novo usuário");
        System.out.println("2. Avaliar carros dos usuários");
        System.out.println("3. Sair");
        System.out.println("=".repeat(50));
    }

    // Cadastra um novo usuário no sistema.
    private static void cadastrarUsuario() {
        mostrarCabecalho("CADASTRO DE NOVO USUÁRIO");

        String nome  = lerTextoObrigatorio("Nome: ");
        String email = lerEmailValido("E-mail: ");
        String senha = lerTextoObrigatorio("Senha: ");

        usuarios.add(new Usuario(nome, email, senha));

        System.out.println("Usuário \"" + nome + "\" cadastrado com sucesso!");
        System.out.println("E-mail: " + email);
    }

    // Valida o e-mail: não pode ser vazio, deve conter @ e não pode já existir.
    private static String lerEmailValido(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String email = scanner.nextLine().trim();

            if (email.isEmpty()) {
                System.out.println("Erro: o e-mail não pode ficar vazio.");
                continue;
            }

            if (!email.contains("@")) {
                System.out.println("Erro: e-mail inválido (deve conter @).");
                continue;
            }

            // Verifica se o e-mail já está em uso.
            boolean emUso = false;
            for (Usuario u : usuarios) {
                if (u.getEmail().equalsIgnoreCase(email)) {
                    emUso = true;
                    break;
                }
            }

            if (emUso) {
                System.out.println("Erro: já existe um usuário cadastrado com esse e-mail.");
                continue;
            }

            return email;
        }
    }

    // Admin escolhe um usuário para visualizar e avaliar os carros.
    private static void selecionarUsuarioParaAvaliar() {
        mostrarCabecalho("SELECIONAR USUÁRIO PARA AVALIAR CARROS");

        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado no sistema.");
            return;
        }

        // Lista todos os usuários com a quantidade de carros cadastrados.
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            int qtdCarros = getVeiculosDoUsuario(u.getEmail()).size();
            System.out.printf("%d. %s (%s) — %d carro(s)%n",
                    i + 1, u.getNome(), u.getEmail(), qtdCarros);
        }

        System.out.println("-".repeat(50));
        System.out.println("0. Voltar");

        int escolha = lerInteiro("Selecione o número do usuário: ");

        if (escolha == 0) return;

        if (escolha < 1 || escolha > usuarios.size()) {
            System.out.println("Número inválido.");
            return;
        }

        menuAvaliacaoCarros(usuarios.get(escolha - 1));
    }

    // Exibe os carros de um usuário e permite ao admin selecionar um para avaliar.
    private static void menuAvaliacaoCarros(Usuario usuario) {
        while (true) {
            mostrarCabecalho("CARROS DE " + usuario.getNome().toUpperCase());

            ArrayList<Veiculo> carros = getVeiculosDoUsuario(usuario.getEmail());

            if (carros.isEmpty()) {
                System.out.println("Este usuário ainda não possui carros cadastrados.");
                return;
            }

            // Exibe lista de carros com status de avaliação e detalhes da avaliação se já avaliado.
            for (int i = 0; i < carros.size(); i++) {
                Veiculo v = carros.get(i);
                if (v.isAvaliado()) {
                    System.out.printf("%d. %s | %s | %d | [AVALIADO] | Valor Usuário: R$ %s | Valor Avaliado: R$ %s | Raridade: %s%n",
                            i + 1, v.getNome(), v.getMarca(), v.getAnoFabricacao(),
                            v.getValorFormatado(), v.getValorAvaliadoFormatado(), v.getRaridade());
                } else {
                    System.out.printf("%d. %s | %s | %d | [PENDENTE] | Valor Usuário: R$ %s%n",
                            i + 1, v.getNome(), v.getMarca(), v.getAnoFabricacao(),
                            v.getValorFormatado());
                }
            }

            System.out.println("-".repeat(50));
            System.out.println("0. Voltar");

            int escolha = lerInteiro("Selecione um carro para avaliar: ");

            if (escolha == 0) return;

            if (escolha < 1 || escolha > carros.size()) {
                System.out.println("Número inválido. Tente novamente.");
                continue;
            }

            avaliarVeiculo(carros.get(escolha - 1));
        }
    }

    // Admin define o valor avaliado e a raridade de um veículo.
    private static void avaliarVeiculo(Veiculo veiculo) {
        mostrarCabecalho("AVALIAÇÃO DO VEÍCULO");
        exibirDetalhesVeiculo(veiculo);

        System.out.println("--- Definir Avaliação ---");

        BigDecimal valorAvaliado = lerValorValido("Valor avaliado em R$ (use ponto, ex: 250000.00): ");
        String raridade = lerRaridade();

        // Grava a avaliação diretamente no objeto Veiculo.
        veiculo.setValorAvaliado(valorAvaliado);
        veiculo.setRaridade(raridade);

        mostrarCabecalho("AVALIAÇÃO REGISTRADA COM SUCESSO");
        System.out.println("Veículo   : " + veiculo.getNome());
        System.out.println("Valor     : R$ " + veiculo.getValorAvaliadoFormatado());
        System.out.println("Raridade  : " + raridade);
        System.out.println("-".repeat(50));
    }

    // Lê a raridade escolhida pelo admin (Comum, Raro ou Lendário).
    private static String lerRaridade() {
        while (true) {
            System.out.println("Nível de raridade:");
            System.out.println("  1. Comum");
            System.out.println("  2. Raro");
            System.out.println("  3. Lendário");
            int opcao = lerInteiro("Escolha o nível: ");

            switch (opcao) {
                case 1: return "Comum";
                case 2: return "Raro";
                case 3: return "Lendário";
                default: System.out.println("Opção inválida. Escolha 1, 2 ou 3.");
            }
        }
    }

    // =========================================================================
    // MENU DO USUÁRIO COMUM
    // =========================================================================

    // Exibe o menu do usuário e processa as opções escolhidas.
    private static void menuUsuario(Usuario usuario) {
        int opcao;

        do {
            mostrarMenuUsuario();
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    cadastrarVeiculo(usuario);
                    break;
                case 2:
                    listarVeiculos(usuario);
                    break;
                case 3:
                    menuFiltro(usuario);
                    break;
                case 4:
                    visualizarVeiculosAvaliados(usuario);
                    break;
                case 5:
                    mostrarCabecalho("SAINDO");
                    System.out.println("Sessão encerrada. Até mais, " + usuario.getNome() + "!");
                    break;
                default:
                    System.out.println("Opção inválida. Escolha uma opção entre 1 e 5.");
            }

        } while (opcao != 5);
    }

    // Exibe o cabeçalho do menu do usuário.
    private static void mostrarMenuUsuario() {
        mostrarCabecalho("SISTEMA DE CADASTRO DE VEÍCULOS");
        System.out.println("1. Cadastrar novo veículo");
        System.out.println("2. Listar meus veículos");
        System.out.println("3. Filtrar veículos");
        System.out.println("4. Ver veículos avaliados");
        System.out.println("5. Sair");
        System.out.println("=".repeat(50));
    }

    // Cadastra um novo veículo vinculado ao usuário logado.
    private static void cadastrarVeiculo(Usuario usuario) {
        mostrarCabecalho("CADASTRO DE NOVO VEÍCULO");

        String nome     = lerTextoObrigatorio("Digite o nome do carro: ");
        String marca    = lerTextoObrigatorio("Digite a marca: ");
        int ano         = lerAnoValido("Digite o ano de fabricação: ");
        BigDecimal valor = lerValorValido("Digite o valor do veículo (use ponto, ex: 45000.99): ");
        String detalhes = lerTextoOpcional("Digite os detalhes técnicos (ou Enter para pular): ");

        // O veículo é criado com o e-mail do usuário como identificador de dono.
        Veiculo veiculo = new Veiculo(nome, marca, ano, valor, detalhes, usuario.getEmail());
        veiculos.add(veiculo);

        mostrarCabecalho("VEÍCULO CADASTRADO COM SUCESSO");
        exibirDetalhesVeiculo(veiculo);
    }

    // Lista os veículos do usuário logado e permite editar ou deletar.
    private static void listarVeiculos(Usuario usuario) {
        while (true) {
            mostrarCabecalho("MEUS VEÍCULOS CADASTRADOS");

            ArrayList<Veiculo> meusCarros = getVeiculosDoUsuario(usuario.getEmail());

            if (meusCarros.isEmpty()) {
                System.out.println("Você ainda não possui veículos cadastrados.");
                return;
            }

            for (int i = 0; i < meusCarros.size(); i++) {
                Veiculo v = meusCarros.get(i);
                String statusAvaliacao = v.isAvaliado() ? " [AVALIADO]" : " [PENDENTE]";
                System.out.printf("%d. %s | %s | %d | R$ %s%s%n",
                        i + 1,
                        v.getNome(),
                        v.getMarca(),
                        v.getAnoFabricacao(),
                        v.getValorFormatado(),
                        statusAvaliacao);
            }

            System.out.println("-".repeat(50));
            System.out.println("0. Voltar ao menu principal");

            int escolha = lerInteiro("Selecione o número do veículo para gerenciar: ");

            if (escolha == 0) return;

            if (escolha < 1 || escolha > meusCarros.size()) {
                System.out.println("Erro: número de veículo inválido.");
                continue;
            }

            gerenciarVeiculo(meusCarros.get(escolha - 1));
        }
    }

    // Exibe apenas os veículos avaliados do usuário e seus detalhes.
    private static void visualizarVeiculosAvaliados(Usuario usuario) {
        while (true) {
            mostrarCabecalho("MEUS VEÍCULOS AVALIADOS");

            ArrayList<Veiculo> todosCarros = getVeiculosDoUsuario(usuario.getEmail());
            ArrayList<Veiculo> carrosAvaliados = new ArrayList<>();
            for (Veiculo v : todosCarros) {
                if (v.isAvaliado()) {
                    carrosAvaliados.add(v);
                }
            }

            if (carrosAvaliados.isEmpty()) {
                System.out.println("Nenhum veículo foi avaliado pelo administrador ainda.");
                System.out.println("Pressione Enter para voltar...");
                scanner.nextLine();
                return;
            }

            for (int i = 0; i < carrosAvaliados.size(); i++) {
                Veiculo v = carrosAvaliados.get(i);
                System.out.printf("%d. %s | %s | Ano: %d | R$ %s | Raridade: %s%n",
                        i + 1,
                        v.getNome(),
                        v.getMarca(),
                        v.getAnoFabricacao(),
                        v.getValorAvaliadoFormatado(),
                        v.getRaridade());
            }

            System.out.println("-".repeat(50));
            System.out.println("0. Voltar ao menu principal");

            int escolha = lerInteiro("Selecione o número do veículo para ver detalhes completos da avaliação: ");

            if (escolha == 0) return;

            if (escolha < 1 || escolha > carrosAvaliados.size()) {
                System.out.println("Erro: número de veículo inválido.");
                continue;
            }

            Veiculo selecionado = carrosAvaliados.get(escolha - 1);
            mostrarDetalhesAvaliacao(selecionado);
        }
    }

    // Exibe detalhes específicos da avaliação do veículo.
    private static void mostrarDetalhesAvaliacao(Veiculo veiculo) {
        mostrarCabecalho("DETALHES DA AVALIAÇÃO");
        System.out.println("Veículo         : " + veiculo.getNome() + " (" + veiculo.getMarca() + ")");
        System.out.println("Ano Fabricação  : " + veiculo.getAnoFabricacao());
        System.out.println("Valor Cadastrado: R$ " + veiculo.getValorFormatado());
        System.out.println("Detalhes        : " + veiculo.getDetalhesTecnicos());
        System.out.println("--------------------------------------------------");
        System.out.println("VALOR AVALIADO  : R$ " + veiculo.getValorAvaliadoFormatado());
        System.out.println("RARIDADE        : " + veiculo.getRaridade());
        System.out.println("--------------------------------------------------");
        System.out.println("Pressione Enter para voltar...");
        scanner.nextLine();
    }

    // Exibe o submenu de gerenciamento de um veículo específico.
    private static void gerenciarVeiculo(Veiculo veiculo) {
        while (true) {
            mostrarCabecalho("GERENCIAR VEÍCULO");
            exibirDetalhesVeiculo(veiculo);

            System.out.println("1. Deletar veículo");
            System.out.println("2. Editar veículo");
            System.out.println("3. Voltar para a listagem");
            System.out.println("-".repeat(50));

            int opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    deletarVeiculo(veiculo);
                    return; // Volta para a listagem atualizada.
                case 2:
                    editarVeiculo(veiculo);
                    return; // Volta para a listagem atualizada.
                case 3:
                    return;
                default:
                    System.out.println("Opção inválida. Escolha 1, 2 ou 3.");
            }
        }
    }

    // Remove um veículo da lista global após confirmação.
    private static void deletarVeiculo(Veiculo veiculo) {
        System.out.print("Tem certeza que deseja deletar \"" + veiculo.getNome() + "\"? (s/n): ");
        String confirmacao = scanner.nextLine().trim().toLowerCase();

        if (confirmacao.equals("s")) {
            veiculos.remove(veiculo);
            System.out.println("Veículo deletado com sucesso.");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    // Edita os dados básicos de um veículo.
    // Pressionar Enter mantém o valor atual do campo.
    private static void editarVeiculo(Veiculo veiculo) {
        mostrarCabecalho("EDIÇÃO DE VEÍCULO");
        System.out.println("Pressione Enter sem digitar nada para manter o valor atual.");
        System.out.println("-".repeat(50));

        String novoNome     = lerTextoEdicao("Nome do carro", veiculo.getNome());
        String novaMarca    = lerTextoEdicao("Marca", veiculo.getMarca());
        int novoAno         = lerAnoEdicao("Ano de fabricação", veiculo.getAnoFabricacao());
        BigDecimal novoValor = lerValorEdicao("Valor", veiculo.getValor());
        String novosDetalhes = lerTextoEdicao("Detalhes técnicos", veiculo.getDetalhesTecnicos());

        veiculo.setNome(novoNome);
        veiculo.setMarca(novaMarca);
        veiculo.setAnoFabricacao(novoAno);
        veiculo.setValor(novoValor);
        veiculo.setDetalhesTecnicos(novosDetalhes);

        System.out.println("Veículo editado com sucesso.");

        mostrarCabecalho("DADOS ATUALIZADOS");
        exibirDetalhesVeiculo(veiculo);
    }

    // Exibe todos os dados de um veículo, incluindo avaliação do admin se existir.
    private static void exibirDetalhesVeiculo(Veiculo veiculo) {
        System.out.println("Nome do carro   : " + veiculo.getNome());
        System.out.println("Marca           : " + veiculo.getMarca());
        System.out.println("Ano fabricação  : " + veiculo.getAnoFabricacao());
        System.out.println("Valor cadastrado: R$ " + veiculo.getValorFormatado());
        System.out.println("Detalhes        : " + veiculo.getDetalhesTecnicos());

        // Exibe os dados de avaliação do administrador, se já houver.
        if (veiculo.isAvaliado()) {
            System.out.println("--- Avaliação do Administrador ---");
            System.out.println("Valor avaliado  : R$ " + veiculo.getValorAvaliadoFormatado());
            System.out.println("Raridade        : " + veiculo.getRaridade());
        } else {
            System.out.println("Avaliação       : Pendente");
        }

        System.out.println("-".repeat(50));
    }

    // =========================================================================
    // FILTROS DE VEÍCULOS (menu do usuário)
    // =========================================================================

    // Exibe o submenu de filtros para os veículos do usuário logado.
    private static void menuFiltro(Usuario usuario) {
        ArrayList<Veiculo> meusCarros = getVeiculosDoUsuario(usuario.getEmail());

        if (meusCarros.isEmpty()) {
            mostrarCabecalho("FILTRO DE VEÍCULOS");
            System.out.println("Você não possui veículos cadastrados para filtrar.");
            return;
        }

        while (true) {
            mostrarCabecalho("FILTRO DE VEÍCULOS");
            System.out.println("1. Carro mais caro");
            System.out.println("2. Carro mais antigo");
            System.out.println("3. Voltar ao menu principal");
            System.out.println("=".repeat(50));

            int opcao = lerInteiro("Escolha uma opção: ");

            // Atualiza a lista antes de filtrar (pode ter sido editada).
            meusCarros = getVeiculosDoUsuario(usuario.getEmail());

            switch (opcao) {
                case 1:
                    filtrarCarroMaisCaro(meusCarros);
                    break;
                case 2:
                    filtrarCarroMaisAntigo(meusCarros);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Opção inválida. Escolha 1, 2 ou 3.");
            }
        }
    }

    // Encontra e exibe o carro de maior valor na lista fornecida.
    private static void filtrarCarroMaisCaro(ArrayList<Veiculo> lista) {
        Veiculo maisCaro = lista.get(0);

        for (Veiculo v : lista) {
            // compareTo retorna > 0 se o valor for maior que o do carro atual.
            if (v.getValor().compareTo(maisCaro.getValor()) > 0) {
                maisCaro = v;
            }
        }

        mostrarCabecalho("CARRO MAIS CARO CADASTRADO");
        exibirDetalhesVeiculo(maisCaro);
    }

    // Encontra e exibe o carro mais antigo (menor ano) na lista fornecida.
    private static void filtrarCarroMaisAntigo(ArrayList<Veiculo> lista) {
        Veiculo maisAntigo = lista.get(0);

        for (Veiculo v : lista) {
            if (v.getAnoFabricacao() < maisAntigo.getAnoFabricacao()) {
                maisAntigo = v;
            }
        }

        mostrarCabecalho("CARRO MAIS ANTIGO CADASTRADO");
        exibirDetalhesVeiculo(maisAntigo);
    }

    // =========================================================================
    // UTILITÁRIOS — LEITURA DE DADOS
    // =========================================================================

    // Lê um texto obrigatório (não pode estar vazio).
    private static String lerTextoObrigatorio(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine().trim();

            if (!texto.isEmpty()) return texto;

            System.out.println("Erro: este campo não pode ficar vazio.");
        }
    }

    // Lê um texto opcional (pode estar vazio; retorna "Não informado" nesse caso).
    private static String lerTextoOpcional(String mensagem) {
        System.out.print(mensagem);
        String texto = scanner.nextLine().trim();
        return texto.isEmpty() ? "Não informado" : texto;
    }

    // Lê um número inteiro, repetindo até receber entrada válida.
    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Erro: digite um número inteiro válido.");
            }
        }
    }

    // Valida o ano de fabricação (entre 1886 e o ano atual).
    private static int lerAnoValido(String mensagem) {
        int anoAtual = Year.now().getValue();

        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();

            try {
                int ano = Integer.parseInt(entrada);

                if (ano < 1886 || ano > anoAtual) {
                    System.out.println("Erro: o ano deve estar entre 1886 e " + anoAtual + ".");
                    continue;
                }

                return ano;
            } catch (NumberFormatException e) {
                System.out.println("Erro: digite um ano inteiro válido.");
            }
        }
    }

    // Valida o valor monetário (não aceita vírgula, aceita ponto como decimal).
    private static BigDecimal lerValorValido(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();

            if (entrada.contains(",")) {
                System.out.println("Erro: use ponto em vez de vírgula. Exemplo: 45000.99");
                continue;
            }

            if (!entrada.matches("\\d+(\\.\\d+)?")) {
                System.out.println("Erro: digit um valor numérico válido usando ponto. Exemplo: 45000.99");
                continue;
            }

            try {
                BigDecimal valor = new BigDecimal(entrada);

                if (valor.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Erro: o valor não pode ser negativo.");
                    continue;
                }

                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Erro: valor inválido.");
            }
        }
    }

    // Lê texto durante a edição. Enter mantém o valor atual.
    private static String lerTextoEdicao(String campo, String valorAtual) {
        System.out.print(campo + " atual [" + valorAtual + "] - novo valor: ");
        String entrada = scanner.nextLine().trim();
        return entrada.isEmpty() ? valorAtual : entrada;
    }

    // Lê o ano durante a edição. Enter mantém o valor atual.
    private static int lerAnoEdicao(String campo, int valorAtual) {
        int anoAtual = Year.now().getValue();

        while (true) {
            System.out.print(campo + " atual [" + valorAtual + "] - novo valor: ");
            String entrada = scanner.nextLine().trim();

            if (entrada.isEmpty()) return valorAtual;

            try {
                int ano = Integer.parseInt(entrada);

                if (ano < 1886 || ano > anoAtual) {
                    System.out.println("Erro: o ano deve estar entre 1886 e " + anoAtual + ".");
                    continue;
                }

                return ano;
            } catch (NumberFormatException e) {
                System.out.println("Erro: digite um ano inteiro válido.");
            }
        }
    }

    // Lê o valor monetário durante a edição. Enter mantém o valor atual.
    private static BigDecimal lerValorEdicao(String campo, BigDecimal valorAtual) {
        while (true) {
            System.out.print(campo + " atual [R$ " + valorAtual.toPlainString() + "] - novo valor: ");
            String entrada = scanner.nextLine().trim();

            if (entrada.isEmpty()) return valorAtual;

            if (entrada.contains(",")) {
                System.out.println("Erro: use ponto em vez de vírgula. Exemplo: 45000.99");
                continue;
            }

            if (!entrada.matches("\\d+(\\.\\d+)?")) {
                System.out.println("Erro: digite um valor numérico válido usando ponto.");
                continue;
            }

            try {
                BigDecimal valor = new BigDecimal(entrada);

                if (valor.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Erro: o valor não pode ser negativo.");
                    continue;
                }

                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Erro: valor inválido.");
            }
        }
    }

    // =========================================================================
    // UTILITÁRIOS — EXIBIÇÃO E FILTRAGEM
    // =========================================================================

    // Exibe um cabeçalho visual para organizar o terminal.
    private static void mostrarCabecalho(String titulo) {
        System.out.println();
        System.out.println("=".repeat(50));
        System.out.println(titulo);
        System.out.println("=".repeat(50));
    }

    // Retorna a lista de veículos pertencentes a um determinado usuário.
    private static ArrayList<Veiculo> getVeiculosDoUsuario(String emailUsuario) {
        ArrayList<Veiculo> resultado = new ArrayList<>();
        for (Veiculo v : veiculos) {
            if (v.getEmailUsuario().equalsIgnoreCase(emailUsuario)) {
                resultado.add(v);
            }
        }
        return resultado;
    }
}