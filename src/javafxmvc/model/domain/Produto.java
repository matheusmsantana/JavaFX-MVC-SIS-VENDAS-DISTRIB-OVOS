package javafxmvc.model.domain;

import java.io.Serializable;
import javafx.scene.control.TextField;

public class Produto implements Serializable {

    private String cdProduto;
    private String nome;
    private double preco;
    private int quantidade;
    private Categoria categoria;
    //Atributo do metodo que conta qnt de produtos de cada categoria
    private int qtdProduto;
    
    public Produto() {
    }

    public Produto(String cdProduto, String nome, double preco, int quantidade, int qtdProduto) {
        this.cdProduto = cdProduto;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.qtdProduto = qtdProduto;
    }
    
    //Metodos get e setter do meu metodo de quantos produtos tem por categoria
    public int getQtdProduto() {
        return qtdProduto;
    }

    public void setQtdProduto(int qtdProduto) {
        this.qtdProduto = qtdProduto;
    }
    
    public String getCdProduto() {
        return cdProduto;
    }

    public void setCdProduto(String cdProduto) {
        this.cdProduto = cdProduto;
    }    
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    //Metodo da minha casse Categoria me retorna nome da categoria
    @Override
    public String toString() {
        return this.nome;
    }
    
}
