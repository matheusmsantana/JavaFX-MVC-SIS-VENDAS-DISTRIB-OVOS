package javafxmvc.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextField;
import javafxmvc.model.domain.Categoria;
import javafxmvc.model.domain.Produto;

/*Classe para fazer alterações e querys na tabela produtos*/
public class ProdutoDAO {
    /*criando atributo private e utilizando metodo conexão com banco atravez 
    da minha interface de banco de dados database.java */
    private Connection connection;
    /*pegando a conexão e retornando ela */
    public Connection getConnection() {
        return connection;
    }
    /*setando a conexão*/
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    /*metodo para fazer inserção de produtos no banco de dados*/
    public boolean inserir(Produto produto) {
        String sql = "INSERT INTO produtos(nome, preco, quantidade, cdCategoria, cdProduto) VALUES(?,?,?,?,?)";
        /*String sql = "INSERT INTO produtos(nome, preco, quantidade, cdCategoria) VALUES(?,?,?,?)";*/
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setInt(4, produto.getCategoria().getCdCategoria());
            stmt.setString(5, produto.getCdProduto());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    /*metodo para alterar produtos*/
    public boolean alterar(Produto produto) {
        String sql = "UPDATE produtos SET nome=?, preco=?, quantidade=?, cdCategoria=? WHERE cdProduto=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setInt(4, produto.getCategoria().getCdCategoria());
            stmt.setString(5, produto.getCdProduto());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    /*metodo para deletar produtos*/
    public boolean remover(Produto produto) {
        String sql = "DELETE FROM produtos WHERE cdProduto=?"/*"DELETE FROM produtos WHERE cdCliente=?"*/;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, produto.getCdProduto());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    /*metodo para listar todos os produtos da tabela produtos*/
    public List<Produto> listar() {
        String sql = "SELECT * FROM produtos";
        List<Produto> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Produto produto = new Produto();
                Categoria categoria = new Categoria();
                produto.setCdProduto(resultado.getString("cdProduto"));
                produto.setNome(resultado.getString("nome"));
                produto.setPreco(resultado.getDouble("preco"));
                produto.setQuantidade(resultado.getInt("quantidade"));
                categoria.setCdCategoria(resultado.getInt("cdCategoria"));
                
                //Obtendo os dados completos da Categoria associada ao Produto
                CategoriaDAO categoriaDAO = new CategoriaDAO();
                categoriaDAO.setConnection(connection);
                categoria = categoriaDAO.buscar(categoria);
                
                produto.setCategoria(categoria);
                retorno.add(produto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    /*metodo para listar produtos por categoria*/
    public List<Produto> listarPorCategoria(Categoria categoria) {
        String sql = "SELECT * FROM produtos WHERE cdCategoria=?";
        List<Produto> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, categoria.getCdCategoria());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Produto produto = new Produto();
                produto.setCdProduto(resultado.getString("cdProduto"));
                produto.setNome(resultado.getString("nome"));
                produto.setPreco(resultado.getDouble("preco"));
                produto.setQuantidade(resultado.getInt("quantidade"));
                categoria.setCdCategoria(resultado.getInt("cdCategoria"));
                produto.setCategoria(categoria);
                retorno.add(produto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    /*metodo para listar um produto*/
    public Produto buscar(Produto produto) {
        String sql = "SELECT * FROM produtos WHERE cdProduto=?";
        Produto retorno = new Produto();
        Categoria categoria = new Categoria();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, produto.getCdProduto());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setCdProduto(resultado.getString("cdProduto"));
                retorno.setNome(resultado.getString("nome"));
                retorno.setPreco(resultado.getDouble("preco"));
                retorno.setQuantidade(resultado.getInt("quantidade"));
                categoria.setCdCategoria(resultado.getInt("cdCategoria"));
                retorno.setCategoria(categoria);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    

        //Metodo para contar quantos produtos tem em cada categoria
       public Produto Contar(Categoria categoria) {
        String sql = "SELECT COUNT(cdProduto) FROM produtos WHERE cdCategoria=? GROUP BY cdCategoria";
        Produto retorno = new Produto();
        //Categoria categoria = new Categoria();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, categoria.getCdCategoria());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                //int qtdProduto = resultado.getInt("cdProduto");
                retorno.setQtdProduto(resultado.getInt("qtdProduto"));
                retorno.setCategoria(categoria);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
 
}
