package sgro;

// ============================================================
//  Usuario.java
//  Classe PAI de MenuSupermercado e MenuUsina.
//  Centraliza os atributos e métodos comuns a todo usuário.
// ============================================================

public class Usuario {

    private String id;
    private String nome;
    private String senha;
    private String endereco;

    public Usuario(String id, String nome, String senha, String endereco) {
        this.id       = id;
        this.nome     = nome;
        this.senha    = senha;
        this.endereco = endereco;
    }

    // Verifica se a senha informada está correta (usado no login)
    public boolean senhaCorreta(String senhaDigitada) {
        return this.senha.equals(senhaDigitada);
    }

    public String getId()       { return id; }
    public String getNome()     { return nome; }
    public String getEndereco() { return endereco; }
}
