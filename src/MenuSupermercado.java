package sgro;

// ============================================================
//  MenuSupermercado.java
//  Representa o supermercado E contém seu menu.
//  Herda os dados cadastrais de Usuario.
//  Sem a classe Banco: recebe a lista de chamados e os
//  contadores de ID diretamente pelo construtor.
// ============================================================

import java.util.ArrayList;
import java.util.Scanner;

public class MenuSupermercado extends Usuario {

    private ArrayList<Chamado> chamados;
    private Scanner            scanner;
    private int[]              contadores; // [0] = resíduos, [1] = chamados

    public MenuSupermercado(String id, String nome, String senha, String endereco,
                            ArrayList<Chamado> chamados, Scanner scanner, int[] contadores) {
        super(id, nome, senha, endereco);
        this.chamados   = chamados;
        this.scanner    = scanner;
        this.contadores = contadores;
    }

    // ----------------------------------------------------------
    //  MENU PRINCIPAL
    // ----------------------------------------------------------

    public void exibirMenu() {
        boolean rodando = true;

        while (rodando) {
            cabecalho("SUPERMERCADO  >>  " + getNome());
            System.out.println("  [ 1 ]  Registrar Residuo");
            System.out.println("  [ 2 ]  Ver Historico de Chamadas");
            System.out.println("  [ 0 ]  Sair");
            rodape();

            switch (lerOpcao()) {
                case "1"  -> registrarResiduo();
                case "2"  -> verHistorico();
                case "0"  -> rodando = false;
                default   -> aviso("Opcao invalida. Tente novamente.");
            }
        }
    }

    // ----------------------------------------------------------
    //  UC02 - Registrar Residuo
    // ----------------------------------------------------------

    private void registrarResiduo() {
        cabecalho("REGISTRAR RESIDUO");

        double peso = lerPeso();

        System.out.print("  Descricao (ex: frutas vencidas): ");
        String descricao = scanner.nextLine().trim();
        if (descricao.isEmpty()) descricao = "Residuos organicos";

        String tipoColeta = lerTipoColeta();

        // Gera IDs usando os contadores compartilhados
        String idResiduo = "RES" + contadores[0]++;
        String idChamado = "CH"  + contadores[1]++;

        Residuo residuo = new Residuo(idResiduo, peso, descricao);
        Chamado chamado = new Chamado(idChamado, this, residuo, tipoColeta);

        // Adiciona direto na lista compartilhada
        chamados.add(chamado);

        double valorEstimado = peso * Chamado.PRECO_POR_KG;
        separador();
        sucesso("Residuo registrado com sucesso!");
        System.out.printf("  Chamado gerado    : #%s%n", idChamado);
        System.out.printf("  Valor estimado    : R$ %.2f  (%.2f kg x R$ %.2f/kg)%n",
                valorEstimado, peso, Chamado.PRECO_POR_KG);
        System.out.println("  As usinas ja podem visualizar este chamado.");
        pausar();
    }

    // ----------------------------------------------------------
    //  UC03 - Ver Historico de Chamadas
    // ----------------------------------------------------------

    private void verHistorico() {
        cabecalho("HISTORICO DE CHAMADAS  >>  " + getNome());

        // Filtra os chamados que pertencem a este supermercado
        ArrayList<Chamado> historico = new ArrayList<>();
        for (Chamado c : chamados)
            if (c.getSupermercado().getId().equals(this.getId())) historico.add(c);

        if (historico.isEmpty()) {
            aviso("Nenhum chamado registrado ate o momento.");
        } else {
            System.out.println("  Total: " + historico.size() + " chamado(s)\n");
            for (Chamado c : historico) c.exibir();
        }
        pausar();
    }

    // ----------------------------------------------------------
    //  Métodos auxiliares de leitura
    // ----------------------------------------------------------

    private double lerPeso() {
        double peso = 0;
        while (peso <= 0) {
            System.out.print("  Peso estimado dos residuos (kg): ");
            try {
                peso = Double.parseDouble(scanner.nextLine().replace(",", "."));
                if (peso <= 0) aviso("O peso precisa ser maior que zero.");
            } catch (NumberFormatException e) {
                aviso("Digite apenas numeros.");
            }
        }
        return peso;
    }

    private String lerTipoColeta() {
        separador();
        while (true) {
            System.out.println("  Tipo de coleta:");
            System.out.println("  [ 1 ]  Manual     (chamado imediato)");
            System.out.println("  [ 2 ]  Automatica (agendada para sexta-feira)");
            System.out.print("  Escolha: ");
            String opcao = scanner.nextLine().trim();
            if (opcao.equals("1")) {
                return "MANUAL";
            } else if (opcao.equals("2")) {
                System.out.println("  Coleta automatica agendada para sexta-feira.");
                return "AUTOMATICA";
            } else {
                aviso("Opcao invalida. Digite 1 para Manual ou 2 para Automatica.");
                separador();
            }
        }
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
