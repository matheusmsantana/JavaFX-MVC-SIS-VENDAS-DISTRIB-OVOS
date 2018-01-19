package javafxmvc.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxmvc.model.domain.Cliente;
import javafxmvc.model.domain.ItemDeVenda;
import javafxmvc.model.domain.Venda;

/*Classe para inserir alterar e remover venda no banco de dados*/
public class VendaDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Venda venda) {
        String sql = "INSERT INTO vendas(data, valor, pago, cdCliente) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(venda.getData()));
            stmt.setDouble(2, venda.getValor());
            stmt.setBoolean(3, venda.getPago());
            stmt.setInt(4, venda.getCliente().getCdCliente());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Venda venda) {
        String sql = "UPDATE clientes SET data=?, valor=?, pago=?, cdCliente=? WHERE cdVenda=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(venda.getData()));
            stmt.setDouble(2, venda.getValor());
            stmt.setBoolean(3, venda.getPago());
            stmt.setInt(4, venda.getCliente().getCdCliente());
            stmt.setInt(5, venda.getCdVenda());
            stmt.execute();
           //System.out.println("");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Venda venda) {
        String sql = "DELETE FROM vendas WHERE cdVenda=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, venda.getCdVenda());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Venda> listar() {
        String sql = "SELECT * FROM vendas";
        List<Venda> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Venda venda = new Venda();
                Cliente cliente = new Cliente();
                List<ItemDeVenda> itensDeVenda = new ArrayList();

                venda.setCdVenda(resultado.getInt("cdVenda"));
                venda.setData(resultado.getDate("data").toLocalDate());
                venda.setValor(resultado.getDouble("valor"));
                venda.setPago(resultado.getBoolean("pago"));
                cliente.setCdCliente(resultado.getInt("cdCliente"));

                //Obtendo os dados completos do Cliente associado à Venda
                ClienteDAO clienteDAO = new ClienteDAO();
                clienteDAO.setConnection(connection);
                cliente = clienteDAO.buscar(cliente);

                //Obtendo os dados completos dos Itens de Venda associados à Venda
                ItemDeVendaDAO itemDeVendaDAO = new ItemDeVendaDAO();
                itemDeVendaDAO.setConnection(connection);
                itensDeVenda = itemDeVendaDAO.listarPorVenda(venda);

                venda.setCliente(cliente);
                venda.setItensDeVenda(itensDeVenda);
                retorno.add(venda);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Venda buscar(Venda venda) {
        String sql = "SELECT * FROM vendas WHERE cdVenda=?";
        Venda retorno = new Venda();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, venda.getCdVenda());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Cliente cliente = new Cliente();
                venda.setCdVenda(resultado.getInt("cdVenda"));
                venda.setData(resultado.getDate("data").toLocalDate());
                venda.setValor(resultado.getDouble("valor"));
                venda.setPago(resultado.getBoolean("pago"));
                cliente.setCdCliente(resultado.getInt("cdCliente"));
                venda.setCliente(cliente);
                retorno = venda;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
       //QUEM SABE FUNCIONA E AI EU FICO FELIZ
       public Venda BuscarValorVendas(Venda venda) {
        String sql = "SELECT SUM(valor) FROM vendas WHERE data BETWEEN ? AND ?;";
        Venda retorno = new Venda();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            /*stmt.setDate(1, venda.getData1());
            stmt.setDate(2, venda.getData2());*/
            stmt.setDate(1, venda.getData1());
            stmt.setDate(2, venda.getData2());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                //Cliente cliente = new Cliente();
                //venda.setData1(resultado.getDate("data1").toLocalDate());
                //venda.setData2(resultado.getDate("data2").toLocalDate());
                venda.setValor(resultado.getDouble("SUM"));
                //venda.setCliente(cliente);
                retorno = venda;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    
    public Venda buscarUltimaVenda() {
        /*String sql esta recebendo o maior codigo da venda: entao isso quer dizer que vai retornar 
        a ultima venda pois ela tem o maior codigo*/
        String sql = "SELECT max(cdVenda) FROM vendas";
        Venda retorno = new Venda();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                retorno.setCdVenda(resultado.getInt("max"));
                return retorno;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    

    public Map<Integer, ArrayList> listarQuantidadeVendasPorMes() {
        /*Select count(cdVenda) serve contar os codigos de venda no caso contar quantas vendas foram executadas*/
        /*extract(yerar from data) as ano: esta retornando a quantidade de vendas no ano*/
        /*extract(month from data) as mes from vendas: esta retornando a quantidade de vendas no mês*/
        /*group by ano, mes order by ano, mes: agrupando por ano order by por mês*/
        
        /*esse metodo me traz a quantidade de vendas do mês em tal ano*/
        String sql = "select count(cdVenda), extract(year from data) as ano, extract(month from data) as mes from vendas group by ano, mes order by ano, mes";
        Map<Integer, ArrayList> retorno = new HashMap();
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                ArrayList linha = new ArrayList();
                if (!retorno.containsKey(resultado.getInt("ano")))
                {
                    linha.add(resultado.getInt("mes"));
                    linha.add(resultado.getInt("count"));
                    retorno.put(resultado.getInt("ano"), linha);
                }else{
                    ArrayList linhaNova = retorno.get(resultado.getInt("ano"));
                    linhaNova.add(resultado.getInt("mes"));
                    linhaNova.add(resultado.getInt("count"));
                }
            }
            return retorno;
        } catch (SQLException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
