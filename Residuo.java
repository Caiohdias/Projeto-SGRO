package sgro;

// ============================================================
//  Residuo.java
//  Representa um lote de resíduos registrado pelo supermercado.
// ============================================================

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Residuo {

    private String id;
    private double pesoEstimado;
    private String descricao;
    private String dataRegistro;

    public Residuo(String id, double pesoEstimado, String descricao) {
        this.id           = id;
        this.pesoEstimado = pesoEstimado;
        this.descricao    = descricao;

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.dataRegistro = LocalDateTime.now().format(formato);
    }

    public String getId()           { return id; }
    public double getPesoEstimado() { return pesoEstimado; }
    public String getDescricao()    { return descricao; }
    public String getDataRegistro() { return dataRegistro; }
}
