import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Veiculo> veiculos = new ArrayList<>();
    private static final ArrayList<Usuario> usuarios = new ArrayList<>();
    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_SENHA = "admin123";
    private static final int MAX_TENTATIVAS_LOGIN = 3;

    public static void main(String[] args) {
        int opcao;

        do {
            mostrarTelaInicial();
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    fazerLoginAdmin();
                    break;
                case 2:
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

    // Tela Inicial cabecalho
    private static void mostrarTelaInicial() {
        mostrarCabecalho("SISTEMA DE CADASTRO DE VEÍCULOS");
        System.out.println("1. Entrar como Administrador");
        System.out.println("2. Entrar como Usuário");
        System.out.println("3. Sair");
        System.out.println("=".repeat(50));
    }

    // Funcao de login admin sem uso de case
    private static void fazerLoginAdmin() {
        mostrarCabecalho("LOGIN - ADMINISTRADOR");

        for (int tentativa = 1; tentativa <= MAX_TENTATIVAS_LOGIN; tentativa++) {
            System.out.print("Login (ou digite 'voltar' para cancelar): ");
            String login = scanner.nextLine().trim();

            if (login.isEmpty() || login.equalsIgnoreCase("voltar")) {
                System.out.println("Login cancelado. Voltando ao menu inicial...");
                return;
            }

            System.out.print("Senha (ou digite 'voltar' para cancelar): ");
            String senha = scanner.nextLine().trim();

            if (senha.equalsIgnoreCase("voltar")) {
                System.out.println("Login cancelado. Voltando ao menu inicial...");
                return;
            }

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

    // Funcao de login usuario sem case
    private static void fazerLoginUsuario() {
        mostrarCabecalho("LOGIN - USUÁRIO");

        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado. Solicite ao administrador que crie seu acesso.");
            return;
        }

        for (int tentativa = 1; tentativa <= MAX_TENTATIVAS_LOGIN; tentativa++) {
            System.out.print("E-mail (ou digite 'voltar' para cancelar): ");
            String email = scanner.nextLine().trim();

            if (email.isEmpty() || email.equalsIgnoreCase("voltar")) {
                System.out.println("Login cancelado. Voltando ao menu inicial...");
                return;
            }

            System.out.print("Senha (ou digite 'voltar' para cancelar): ");
            String senha = scanner.nextLine().trim();

            if (senha.equalsIgnoreCase("voltar")) {
                System.out.println("Login cancelado. Voltando ao menu inicial...");
                return;
            }

            Usuario encontrado = buscarUsuarioPorCredenciais(email, senha);

            if (encontrado != null) {
                System.out.println("Bem-vindo(a), " + encontrado.getNome() + "!");

                if (encontrado.isPrimeiroLogin()) {
                    solicitarNovaSenhaPrimeiroLogin(encontrado);
                }

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

    // funcao buscar conta usuario
    private static Usuario buscarUsuarioPorCredenciais(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                return u;
            }
        }
        return null;
    }

    // funcao alteracao senha primeiro login
    private static void solicitarNovaSenhaPrimeiroLogin(Usuario usuario) {
        mostrarCabecalho("ALTERAÇÃO DE SENHA OBRIGATÓRIA - PRIMEIRO ACESSO");
        System.out.println("Por segurança, você deve alterar a senha provisória do seu primeiro acesso.");

        while (true) {
            String novaSenha = lerTextoObrigatorio("Digite sua nova senha: ");
            String confirmacao = lerTextoObrigatorio("Confirme sua nova senha: ");

            if (novaSenha.equals(confirmacao)) {
                usuario.setSenha(novaSenha);
                usuario.setPrimeiroLogin(false);
                System.out.println("Senha updated com sucesso!");
                break;
            } else {
                System.out.println("Erro: as senhas digitadas não coincidem. Tente novamente.");
            }
        }
    }

    // funcao redicioramento telas admin
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

    // Exibe o cabeçalho do menu do administrador
    private static void mostrarMenuAdmin() {
        mostrarCabecalho("PAINEL DO ADMINISTRADOR");
        System.out.println("1. Cadastrar novo usuário");
        System.out.println("2. Avaliar carros dos usuários");
        System.out.println("3. Sair");
        System.out.println("=".repeat(50));
    }

    // funcao cadastro usario do admin
    private static void cadastrarUsuario() {
        mostrarCabecalho("CADASTRO DE NOVO USUÁRIO");

        String nome = lerTextoObrigatorio("Nome: ");
        String email = lerEmailValido("E-mail: ");
        String senha = lerTextoObrigatorio("Senha: ");

        usuarios.add(new Usuario(nome, email, senha));

        System.out.println("Usuário \"" + nome + "\" cadastrado com sucesso!");
        System.out.println("E-mail: " + email);
    }

    // funcao validacao e mascara email
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

    // funcao busca usarios e seus carros ao admin para avaliacao
    private static void selecionarUsuarioParaAvaliar() {
        mostrarCabecalho("SELECIONAR USUÁRIO PARA AVALIAR CARROS");

        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado no sistema.");
            return;
        }

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            int qtdCarros = getVeiculosDoUsuario(u.getEmail()).size();
            System.out.printf("%d. %s (%s) - %d carro(s)%n",
                    i + 1, u.getNome(), u.getEmail(), qtdCarros);
        }

        System.out.println("-".repeat(50));
        System.out.println("0. Voltar");

        int escolha = lerInteiro("Selecione o número do usuário: ");

        if (escolha == 0)
            return;

        if (escolha < 1 || escolha > usuarios.size()) {
            System.out.println("Número inválido.");
            return;
        }

        menuAvaliacaoCarros(usuarios.get(escolha - 1));
    }

    // funcao montagem da exibicao dos usarios e seus carros ao admin para avalia
    private static void menuAvaliacaoCarros(Usuario usuario) {
        while (true) {
            mostrarCabecalho("CARROS DE " + usuario.getNome().toUpperCase());

            ArrayList<Veiculo> carros = getVeiculosDoUsuario(usuario.getEmail());

            if (carros.isEmpty()) {
                System.out.println("Este usuário ainda não possui carros cadastrados.");
                return;
            }

            for (int i = 0; i < carros.size(); i++) {
                Veiculo v = carros.get(i);
                if (v.isAvaliado()) {
                    System.out.printf(
                            "%d. %s | %s | %d | [AVALIADO] | Valor Usuário: R$ %s | Valor Avaliado: R$ %s | Raridade: %s%n",
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

            if (escolha == 0)
                return;

            if (escolha < 1 || escolha > carros.size()) {
                System.out.println("Número inválido. Tente novamente.");
                continue;
            }

            avaliarVeiculo(carros.get(escolha - 1));
        }
    }

    // funcao validacao da captura de valor do carro pelo admin
    private static void avaliarVeiculo(Veiculo veiculo) {
        mostrarCabecalho("AVALIAÇÃO DO VEÍCULO");
        exibirDetalhesVeiculo(veiculo);

        System.out.println("--- Definir Avaliação ---");

        BigDecimal valorAvaliado = lerValorValido("Valor avaliado em R$ (use ponto, ex: 250000.00): ");
        String raridade = lerRaridade();

        veiculo.setValorAvaliado(valorAvaliado);
        veiculo.setRaridade(raridade);

        mostrarCabecalho("AVALIAÇÃO REGISTRADA COM SUCESSO");
        System.out.println("Veículo   : " + veiculo.getNome());
        System.out.println("Valor     : R$ " + veiculo.getValorAvaliadoFormatado());
        System.out.println("Raridade  : " + raridade);
        System.out.println("-".repeat(50));
    }

    // funcao ler raridade
    private static String lerRaridade() {
        while (true) {
            System.out.println("Nível de raridade:");
            System.out.println("  1. Comum");
            System.out.println("  2. Raro");
            System.out.println("  3. Lendário");
            int opcao = lerInteiro("Escolha o nível: ");

            switch (opcao) {
                case 1:
                    return "Comum";
                case 2:
                    return "Raro";
                case 3:
                    return "Lendário";
                default:
                    System.out.println("Opção inválida. Escolha 1, 2 ou 3.");
            }
        }
    }

    // funcao redirecionamento menu usuario
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

    // funcao de exibicao do menu usario
    private static void mostrarMenuUsuario() {
        mostrarCabecalho("SISTEMA DE CADASTRO DE VEÍCULOS");
        System.out.println("1. Cadastrar novo veículo");
        System.out.println("2. Listar meus veículos");
        System.out.println("3. Filtrar veículos");
        System.out.println("4. Ver veículos avaliados");
        System.out.println("5. Sair");
        System.out.println("=".repeat(50));
    }

    // funcao cadastro de veiculo
    private static void cadastrarVeiculo(Usuario usuario) {
        mostrarCabecalho("CADASTRO DE NOVO VEÍCULO");

        String nome = lerTextoObrigatorio("Digite o nome do carro: ");
        String marca = lerTextoObrigatorio("Digite a marca: ");
        int ano = lerAnoValido("Digite o ano de fabricação: ");
        BigDecimal valor = lerValorValido("Digite o valor do veículo (use ponto, ex: 45000.99): ");
        String detalhes = lerTextoOpcional("Digite os detalhes técnicos (ou Enter para pular): ");

        Veiculo veiculo = new Veiculo(nome, marca, ano, valor, detalhes, usuario.getEmail());
        veiculos.add(veiculo);

        mostrarCabecalho("VEÍCULO CADASTRADO COM SUCESSO");
        exibirDetalhesVeiculo(veiculo);
    }

    // funcao busca e listagem de veiculos cadastrados
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

            if (escolha == 0)
                return;

            if (escolha < 1 || escolha > meusCarros.size()) {
                System.out.println("Erro: número de veículo inválido.");
                continue;
            }

            gerenciarVeiculo(meusCarros.get(escolha - 1));
        }
    }

    // funcao exibicao de veiculos cadastrados do usuario
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

            if (escolha == 0)
                return;

            if (escolha < 1 || escolha > carrosAvaliados.size()) {
                System.out.println("Erro: número de veículo inválido.");
                continue;
            }

            Veiculo selecionado = carrosAvaliados.get(escolha - 1);
            mostrarDetalhesAvaliacao(selecionado);
        }
    }

    // funcao exibicaodetalhes avaliacao veiculo
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

    // funcao exibicao menu de acoes de veiculos cadastrados usuarios
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
                    return;
                case 2:
                    editarVeiculo(veiculo);
                    return;
                case 3:
                    return;
                default:
                    System.out.println("Opção inválida. Escolha 1, 2 ou 3.");
            }
        }
    }

    // remove veiuclo da class
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

    // funcao leitura e gravacao de dados editados veiculo cadastrado
    private static void editarVeiculo(Veiculo veiculo) {
        mostrarCabecalho("EDIÇÃO DE VEÍCULO");
        System.out.println("Pressione Enter sem digitar nada para manter o valor atual.");
        System.out.println("-".repeat(50));

        String novoNome = lerTextoEdicao("Nome do carro", veiculo.getNome());
        String novaMarca = lerTextoEdicao("Marca", veiculo.getMarca());
        int novoAno = lerAnoEdicao("Ano de fabricação", veiculo.getAnoFabricacao());
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

    // funcao exibicao dados de veiculos cadastrados usuarios
    private static void exibirDetalhesVeiculo(Veiculo veiculo) {
        System.out.println("Nome do carro   : " + veiculo.getNome());
        System.out.println("Marca           : " + veiculo.getMarca());
        System.out.println("Ano fabricação  : " + veiculo.getAnoFabricacao());
        System.out.println("Valor cadastrado: R$ " + veiculo.getValorFormatado());
        System.out.println("Detalhes        : " + veiculo.getDetalhesTecnicos());

        if (veiculo.isAvaliado()) {
            System.out.println("--- Avaliação do Administrador ---");
            System.out.println("Valor avaliado  : R$ " + veiculo.getValorAvaliadoFormatado());
            System.out.println("Raridade        : " + veiculo.getRaridade());
        } else {
            System.out.println("Avaliação       : Pendente");
        }

        System.out.println("-".repeat(50));
    }

    // funcao exibicao menu de filtros de ordem carros cadastrados usuarios
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

    // funcao logica busca de carro mais caro da lista
    private static void filtrarCarroMaisCaro(ArrayList<Veiculo> lista) {
        Veiculo maisCaro = lista.get(0);

        for (Veiculo v : lista) {
            if (v.getValor().compareTo(maisCaro.getValor()) > 0) {
                maisCaro = v;
            }
        }

        mostrarCabecalho("CARRO MAIS CARO CADASTRADO");
        exibirDetalhesVeiculo(maisCaro);
    }

    // funcao logica busca de carro mais antigo da lista
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

    // funcao casos de obrigatoriedade de espaco nao em branco
    private static String lerTextoObrigatorio(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine().trim();

            if (!texto.isEmpty())
                return texto;

            System.out.println("Erro: este campo não pode ficar vazio.");
        }
    }

    // funcao descobrir se campo a ser exibido esta vazio ou nao
    private static String lerTextoOpcional(String mensagem) {
        System.out.print(mensagem);
        String texto = scanner.nextLine().trim();
        return texto.isEmpty() ? "Não informado" : texto;
    }

    // funcao loop ate ler numero inteiro valido
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

    // valida ano de fabricacap da lata
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

    // valida o valor reias
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

    // funcao busca de informacao de marca em caso de edicao
    private static String lerTextoEdicao(String campo, String valorAtual) {
        System.out.print(campo + " atual [" + valorAtual + "] - novo valor: ");
        String entrada = scanner.nextLine().trim();
        return entrada.isEmpty() ? valorAtual : entrada;
    }

    // funcao busca de informacao de ano em caso de edicao
    private static int lerAnoEdicao(String campo, int valorAtual) {
        int anoAtual = Year.now().getValue();

        while (true) {
            System.out.print(campo + " atual [" + valorAtual + "] - novo valor: ");
            String entrada = scanner.nextLine().trim();

            if (entrada.isEmpty())
                return valorAtual;

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

    // funcao busca de informacao de valor em caso de edicao
    private static BigDecimal lerValorEdicao(String campo, BigDecimal valorAtual) {
        while (true) {
            System.out.print(campo + " atual [R$ " + valorAtual.toPlainString() + "] - novo valor: ");
            String entrada = scanner.nextLine().trim();

            if (entrada.isEmpty())
                return valorAtual;

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

    // funcao UI (laterais) da tela
    private static void mostrarCabecalho(String titulo) {
        System.out.println();
        System.out.println("=".repeat(50));
        System.out.println(titulo);
        System.out.println("=".repeat(50));
    }

    // Retorna a lista de veículos por admin e usario
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