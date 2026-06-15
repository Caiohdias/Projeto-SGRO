package sgro;

// ============================================================
//  MenuUsina.java
//  Representa a usina E contém seu menu.
//  Herda os dados cadastrais de Usuario.
//  Sem a classe Banco: recebe a lista de chamados diretamente.
//  Os métodos de busca e filtro ficam aqui dentro.
//
//  Fluxo da opção 4:
//  Confirmar coleta → se sim, já pede o peso → calcula e finaliza
// ============================================================

import java.util.ArrayList;
import java.util.Scanner;

public class MenuUsina extends Usuario {

    private ArrayList<Chamado> chamados;
    private Scanner            scanner;

    public MenuUsina(String id, String nome, String senha, String endereco,
                     ArrayList<Chamado> chamados, Scanner scanner) {
        super(id, nome, senha, endereco);
        this.chamados = chamados;
        this.scanner  = scanner;
    }

    // ----------------------------------------------------------
    //  MENU PRINCIPAL
    // ----------------------------------------------------------

    public void exibirMenu() {
        boolean rodando = true;

        while (rodando) {
            cabecalho("USINA  >>  " + getNome());
            System.out.println("  [ 1 ]  Ver Chamados Disponiveis");
            System.out.println("  [ 2 ]  Aceitar um Chamado");
            System.out.println("  [ 3 ]  Ver Meus Chamados em Andamento");
            System.out.println("  [ 4 ]  Confirmar Coleta e Registrar Peso");
            System.out.println("  [ 0 ]  Sair");
            rodape();

            switch (lerOpcao()) {
                case "1"  -> verChamadosDisponiveis();
                case "2"  -> aceitarChamado();
                case "3"  -> verChamadosEmAndamento();
                case "4"  -> confirmarColetaERegistrarPeso();
                case "0"  -> rodando = false;
                default   -> aviso("Opcao invalida. Tente novamente.");
            }
        }
    }

    // ----------------------------------------------------------
    //  UC06 - Ver Chamados Disponíveis
    // ----------------------------------------------------------

    private void verChamadosDisponiveis() {
        cabecalho("CHAMADOS DISPONIVEIS");

        ArrayList<Chamado> abertos = getChamadosAbertos();

        if (abertos.isEmpty()) {
            aviso("Nenhum chamado disponivel no momento.");
        } else {
            System.out.println("  Total: " + abertos.size() + " chamado(s) aberto(s)\n");
            for (Chamado c : abertos) c.exibir();
        }
        pausar();
    }

    // ----------------------------------------------------------
    //  UC07 - Aceitar um Chamado
    // ----------------------------------------------------------

    private void aceitarChamado() {
        cabecalho("ACEITAR CHAMADO");

        ArrayList<Chamado> abertos = getChamadosAbertos();

        if (abertos.isEmpty()) {
            aviso("Nenhum chamado disponivel no momento.");
            pausar();
            return;
        }

        System.out.println("  Chamados disponiveis:\n");
        for (Chamado c : abertos) c.exibir();

        System.out.print("  Digite o ID do chamado (ou 0 para cancelar): ");
        String idDigitado = scanner.nextLine().trim();
        if (idDigitado.equals("0")) return;

        Chamado chamado = buscarChamado(idDigitado);
        if (chamado == null) {
            aviso("Chamado nao encontrado.");
            pausar();
            return;
        }

        separador();
        System.out.printf("  Custo estimado de compra: R$ %.2f%n",
                chamado.getResiudo().getPesoEstimado() * Chamado.PRECO_POR_KG);
        System.out.printf("  Energia estimada a gerar: %.2f kWh%n",
                chamado.getResiudo().getPesoEstimado() * Chamado.KWH_POR_KG);
        separador();

        System.out.print("  Deseja aceitar este chamado? (s/n): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("s")) {
            aviso("Operacao cancelada.");
            pausar();
            return;
        }

        if (chamado.aceitar(this)) {
            sucesso("Chamado #" + idDigitado + " aceito com sucesso!");
            System.out.println("  Supermercado: " + chamado.getSupermercado().getNome());
        } else {
            aviso("Este chamado nao esta mais disponivel.");
        }
        pausar();
    }

    // ----------------------------------------------------------
    //  Ver Chamados em Andamento
    // ----------------------------------------------------------

    private void verChamadosEmAndamento() {
        cabecalho("MEUS CHAMADOS EM ANDAMENTO");

        ArrayList<Chamado> lista = getChamadosDaUsina();

        if (lista.isEmpty()) {
            aviso("Nenhum chamado em andamento.");
        } else {
            for (Chamado c : lista) c.exibir();
        }
        pausar();
    }

    // ----------------------------------------------------------
    //  UC08 + UC09 - Confirmar Coleta e Registrar Peso
    //
    //  Fluxo:
    //  1. Lista os chamados EM_ANDAMENTO
    //  2. Usuário escolhe o chamado
    //  3. Confirma que a coleta foi feita
    //     - Se NÃO: oferece cancelar ou manter em andamento
    //     - Se SIM: já pede o peso → calcula e finaliza
    // ----------------------------------------------------------

    private void confirmarColetaERegistrarPeso() {
        cabecalho("CONFIRMAR COLETA E REGISTRAR PESO");

        // Filtra só os chamados EM_ANDAMENTO desta usina
        ArrayList<Chamado> emAndamento = new ArrayList<>();
        for (Chamado c : getChamadosDaUsina())
            if (c.getStatus().equals("EM_ANDAMENTO")) emAndamento.add(c);

        if (emAndamento.isEmpty()) {
            aviso("Nenhuma coleta aguardando confirmacao.");
            pausar();
            return;
        }

        System.out.println("  Chamados aguardando confirmacao:\n");
        for (Chamado c : emAndamento) c.exibir();

        System.out.print("  Digite o ID do chamado (ou 0 para cancelar): ");
        String idDigitado = scanner.nextLine().trim();
        if (idDigitado.equals("0")) return;

        Chamado chamado = buscarChamado(idDigitado);
        if (chamado == null) {
            aviso("Chamado nao encontrado.");
            pausar();
            return;
        }

        // --- Passo 1: confirmar se a coleta foi feita ---
        separador();
        System.out.print("  A coleta foi realizada? (s/n): ");

        if (!scanner.nextLine().trim().equalsIgnoreCase("s")) {
            // Exceção: problema na coleta
            System.out.print("  Deseja cancelar e devolver o chamado para a lista? (s/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                chamado.cancelar();
                sucesso("Chamado devolvido para a lista de disponiveis.");
            } else {
                aviso("Chamado permanece em andamento.");
            }
            pausar();
            return;
        }

        // Coleta confirmada
        chamado.confirmarColeta();
        sucesso("Coleta confirmada!");

        // --- Passo 2: já pede o peso real ---
        separador();
        System.out.println("  Agora informe o peso real coletado:");
        double peso = lerPeso();

        chamado.registrarPesoEFinalizar(peso);

        separador();
        System.out.println("  RESUMO DA COLETA");
        separador();
        System.out.printf("  Supermercado  : %s%n",      chamado.getSupermercado().getNome());
        System.out.printf("  Peso coletado : %.2f kg%n",  chamado.getPesoReal());
        System.out.printf("  Energia gerada: %.2f kWh%n", chamado.getEnergiaGerada());
        separador();
        System.out.printf("  VALOR A PAGAR : R$ %.2f%n",  chamado.getValorPago());
        separador();

        System.out.print("  Confirmar pagamento de R$ " + String.format("%.2f", chamado.getValorPago()) + " ao supermercado? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
            sucesso("Pagamento confirmado! Chamado finalizado com sucesso.");
        } else {
            aviso("Pagamento nao confirmado. O chamado foi finalizado mas o pagamento esta pendente.");
        }
        separador();
        pausar();
    }

    // ----------------------------------------------------------
    //  Métodos auxiliares de busca (antes ficavam no Banco)
    // ----------------------------------------------------------

    // Retorna só os chamados com status ABERTO
    private ArrayList<Chamado> getChamadosAbertos() {
        ArrayList<Chamado> lista = new ArrayList<>();
        for (Chamado c : chamados)
            if (c.getStatus().equals("ABERTO")) lista.add(c);
        return lista;
    }

    // Retorna os chamados ativos desta usina
    private ArrayList<Chamado> getChamadosDaUsina() {
        ArrayList<Chamado> lista = new ArrayList<>();
        for (Chamado c : chamados) {
            boolean ativo     = !c.getStatus().equals("FINALIZADO")
                             && !c.getStatus().equals("CANCELADO");
            boolean vinculado = this.equals(c.getUsinaResponsavel());
            if (ativo && vinculado) lista.add(c);
        }
        return lista;
    }

    // Busca um chamado pelo ID
    private Chamado buscarChamado(String id) {
        for (Chamado c : chamados)
            if (c.getId().equals(id)) return c;
        return null;
    }

    // ----------------------------------------------------------
    //  Métodos auxiliares de leitura
    // ----------------------------------------------------------

    private double lerPeso() {
        double peso = 0;
        while (peso <= 0) {
            System.out.print("  Peso real coletado (kg): ");
            try {
                peso = Double.parseDouble(scanner.nextLine().replace(",", "."));
                if (peso <= 0) aviso("O peso precisa ser maior que zero.");
            } catch (NumberFormatException e) {
                aviso("Digite apenas numeros.");
            }
        }
        return peso;
    }

    private String lerOpcao() {
        System.out.print("  Escolha: ");
        return scanner.nextLine().trim();
    }

    private void pausar() {
        System.out.print("\n  Pressione ENTER para voltar...");
        scanner.nextLine();
    }

    // ----------------------------------------------------------
    //  Formatação visual
    // ----------------------------------------------------------

    private void cabecalho(String titulo) {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.printf( "║  %-44s║%n", titulo);
        System.out.println("╠══════════════════════════════════════════════╣");
    }

    private void rodape() {
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    private void separador() {
        System.out.println("  ──────────────────────────────────────────────");
    }

    private void sucesso(String msg) { System.out.println("  [OK] " + msg); }
    private void aviso(String msg)   { System.out.println("  [!]  " + msg); }
}
