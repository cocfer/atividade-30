package br.com.starlog.model;

/** Produto do almoxarifado; toda escrita passa pelas mesmas regras. */
public class Produto {
    private Long id;
    private final String nome;
    private double preco;
    private int estoque;

    public Produto(Long id, String nome, double preco, int estoque) {
        String limpo = (nome == null) ? "" : nome.trim();
        if (limpo.length() < 3) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres.");
        }
        this.id = id;
        this.nome = limpo;
        this.preco = validarPreco(preco);
        this.estoque = validarEstoque(estoque);
    }

    public Produto(String nome, double preco, int estoque) {
        this(null, nome, preco, estoque);
    }

    private static double validarPreco(double preco) {
        if (preco > 0.0) {
            return preco;
        }
        throw new IllegalArgumentException("Preço deve ser maior que zero.");
    }

    private static int validarEstoque(int estoque) {
        if (estoque >= 0) {
            return estoque;
        }
        throw new IllegalArgumentException("Estoque não pode ser negativo.");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = validarPreco(preco);
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = validarEstoque(estoque);
    }

    @Override
    public String toString() {
        return "Produto[" + id + "] " + nome + " | R$ " + String.format("%.2f", preco) + " | estoque: " + estoque;
    }
}
