package com.example.rango.lugar;

public class Lugar {
    private String nome;
    private String categoria;
    private double preco;
    private String observacao;
    private int votos;

    public Lugar(String nome, String categoria, double preco, String observacao, int votos) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.observacao = observacao;
        this.votos = votos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getVotos() {
        return votos;
    }

    public void setVotos(int votos) {
        this.votos = votos;
    }
}
