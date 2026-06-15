package sgro;

// ============================================================
//  Main.java
//  Ponto de entrada do sistema SGRO.
//  Sem a classe Banco: as listas de usuários e chamados ficam
//  aqui, assim como a lógica de login.
// ============================================================

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner            scanner  = new Scanner(System.in);
        ArrayList<Chamado> chamados = new ArrayList<>();

        // Contadores compartilhados de IDs: [0] = residuos, [1] = chamados
        int[] contadores = {1, 1};

        // ── Cadastra supermercados ──
        ArrayList<MenuSupermercado> supermercados = new ArrayList<>();
        supermercados.add(new MenuSupermercado("SUP01", "Delta",    "1234", "Rua das Flores, 100",     chamados, scanner, contadores));
        supermercados.add(new MenuSupermercado("SUP02", "Copacabana", "5678", "Av. das Palmeiras, 500",  chamados, scanner, contadores));
        supermercados.add(new MenuSupermercado("SUP03", "Kibarato",  "abcd", "Rod. SP-127, km 5",       chamados, scanner, contadores));

        // ── Cadastra usinas ──
        ArrayList<MenuUsina> usinas = new ArrayList<>();
        usinas.add(new MenuUsina("USI01", "BioEnergia Paulista",    "pass1", "Estrada Rural, km 12",         chamados, scanner));
        usinas.add(new MenuUsina("USI02", "EcoUsina Energia", "pass2", "Rod. dos Bandeirantes, km 30", chamados, scanner));

        // ── Tela inicial ──
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║       SGRO - Gerenciamento de Residuos       ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║  USUARIOS DE TESTE                           ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║  Supermercados:                              ║");
        System.out.println("║    ID: SUP01  |  Senha: 1234                 ║");
        System.out.println("║    ID: SUP02  |  Senha: 5678                 ║");
        System.out.println("║    ID: SUP03  |  Senha: abcd                 ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║  Usinas:                                     ║");
        System.out.println("║    ID: USI01  |  Senha: pass1                ║");
        System.out.println("║    ID: USI02  |  Senha: pass2                ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        // ── Loop principal de login ──
        boolean rodando = true;

        while (rodando) {
            System.out.println("\n╔══════════════════════════════════════════════╗");
            System.out.println("║                    LOGIN                     ║");
            System.out.println("╚══════════════════════════════════════════════╝");
            System.out.println("  (Digite 'sair' no ID para encerrar o sistema)");
            System.out.print("  ID    : ");
            String id = scanner.nextLine().trim();

            if (id.equalsIgnoreCase("sair")) {
                System.out.println("\n  Encerrando o sistema. Ate logo!");
                rodando = false;
                continue;
            }

            System.out.print("  Senha : ");
            String senha = scanner.nextLine().trim();

            // Tenta encontrar nas listas e validar a senha
            MenuSupermercado sup   = buscarSupermercado(supermercados, id, senha);
            MenuUsina        usina = buscarUsina(usinas, id, senha);

            if (sup != null) {
                System.out.println("\n  [OK] Bem-vindo(a), " + sup.getNome() + "!");
                sup.exibirMenu();

            } else if (usina != null) {
                System.out.println("\n  [OK] Bem-vindo(a), " + usina.getNome() + "!");
                usina.exibirMenu();

            } else {
                System.out.println("\n  [!]  ID ou senha incorretos. Tente novamente.");
            }
        }

        scanner.close();
    }

    // Busca supermercado pelo ID e valida a senha
    private static MenuSupermercado buscarSupermercado(ArrayList<MenuSupermercado> lista,
                                                       String id, String senha) {
        for (MenuSupermercado s : lista)
            if (s.getId().equals(id) && s.senhaCorreta(senha)) return s;
        return null;
    }

    // Busca usina pelo ID e valida a senha
    private static MenuUsina buscarUsina(ArrayList<MenuUsina> lista,
                                         String id, String senha) {
        for (MenuUsina u : lista)
            if (u.getId().equals(id) && u.senhaCorreta(senha)) return u;
        return null;
    }
}
