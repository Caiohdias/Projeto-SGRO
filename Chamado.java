package sgro;

// ============================================================
//  Chamado.java
//  Conecta um MenuSupermercado a um MenuUsina.
//
//  Ciclo de vida:
//  "ABERTO" → "EM_ANDAMENTO" → "COLETADO" → "FINALIZADO"
// ============================================================

public class Chamado {

    public static final double KWH_POR_KG   = 0.5;
    public static final double PRECO_POR_KG = 0.30;

    private String           id;
    private MenuSupermercado supermercado;
    private Residuo          residuo;
    private String           tipoColeta;
    private String           status;
    private MenuUsina        usinaResponsavel;
    private double           pesoReal;
    private double           energiaGerada;
    private double           valorPago;

    public Chamado(String id, MenuSupermercado supermercado, Residuo residuo, String tipoColeta) {
        this.id               = id;
        this.supermercado     = supermercado;
        this.residuo          = residuo;
        this.tipoColeta       = tipoColeta;
        this.status           = "ABERTO";
        this.usinaResponsavel = null;
        this.pesoReal         = 0;
        this.energiaGerada    = 0;
        this.valorPago        = 0;
    }

    // UC07 - Usina aceita o chamado
    public boolean aceitar(MenuUsina usina) {
        if (!this.status.equals("ABERTO")) return false;
        this.usinaResponsavel = usina;
        this.status = "EM_ANDAMENTO";
        return true;
    }

    // UC08 - Usina confirma que coletou o resíduo fisicamente
    public boolean confirmarColeta() {
        if (!this.status.equals("EM_ANDAMENTO")) return false;
        this.status = "COLETADO";
        return true;
    }

    // UC09 - Registra o peso real, calcula energia e valor, e finaliza o chamado
    public boolean registrarPesoEFinalizar(double peso) {
        if (!this.status.equals("COLETADO")) return false;
        if (peso <= 0) return false;
        this.pesoReal      = peso;
        this.energiaGerada = peso * KWH_POR_KG;
        this.valorPago     = peso * PRECO_POR_KG;
        this.status        = "FINALIZADO";
        return true;
    }

    // UC08 exceção - Cancela e devolve o chamado para a lista de abertos
    public boolean cancelar() {
        if (this.status.equals("FINALIZADO") || this.status.equals("CANCELADO")) return false;
        this.usinaResponsavel = null;
        this.status = "ABERTO";
        return true;
    }

    // Exibe as informações do chamado formatadas no terminal
    public void exibir() {
        System.out.println("  | Chamado #" + id);
        System.out.println("  | Supermercado  : " + supermercado.getNome());
        System.out.println("  | Endereço      : " + supermercado.getEndereco());
        System.out.println("  | Resíduo       : " + residuo.getDescricao());
        System.out.println("  | Peso estimado : " + residuo.getPesoEstimado() + " kg");
        System.out.println("  | Tipo de coleta: " + tipoColeta);
        System.out.println("  | Status        : " + status);
        System.out.println("  | Registrado em : " + residuo.getDataRegistro());
        if (usinaResponsavel != null)
            System.out.println("  | Usina         : " + usinaResponsavel.getNome());
        if (pesoReal > 0) {
            System.out.println("  | Peso real     : " + pesoReal + " kg");
            System.out.printf( "  | Energia gerada: %.2f kWh%n", energiaGerada);
            System.out.printf( "  | Valor pago    : R$ %.2f%n", valorPago);
        }
        System.out.println("  +" + "-".repeat(42));
    }

    public String           getId()               { return id; }
    public MenuSupermercado getSupermercado()     { return supermercado; }
    public Residuo          getResiudo()          { return residuo; }
    public String           getTipoColeta()       { return tipoColeta; }
    public String           getStatus()           { return status; }
    public MenuUsina        getUsinaResponsavel() { return usinaResponsavel; }
    public double           getPesoReal()         { return pesoReal; }
    public double           getEnergiaGerada()    { return energiaGerada; }
    public double           getValorPago()        { return valorPago; }
}
