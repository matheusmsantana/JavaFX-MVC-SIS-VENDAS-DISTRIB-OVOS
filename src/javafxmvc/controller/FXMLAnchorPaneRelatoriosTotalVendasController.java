package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafxmvc.model.dao.ItemDeVendaDAO;
import javafxmvc.model.dao.VendaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.ItemDeVenda;
import javafxmvc.model.domain.Venda;

/*@author Matheus Marques*/
public class FXMLAnchorPaneRelatoriosTotalVendasController implements Initializable {
    
    //Atributos da Interface Grafica
    @FXML
    DatePicker datePickerRelatorioVendaData1;
    @FXML
    DatePicker datePickerRelatorioVendaData2;
    @FXML
    TextField textFieldRelatorioVendaData1; 
    @FXML
    TextField textFieldRelatorioVendaData2;
    @FXML
    Button buttonPesquisar;
    @FXML
    TextField textFieldRelatorioResultado;
    
    //Atributos para manipulação do banco de dados
    //Fazendo acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    /*Esse metodo esta indo ao banco de dados e se conectando com meu banco de dados*/
    private final Connection connection = database.conectar();
    /*Instanciando a classe ClienteDAO que contem metodos de sql*/
    private final ItemDeVendaDAO itemDeVendaDAO = new ItemDeVendaDAO();
    private final VendaDAO vendaDAO = new VendaDAO();
    //
    private Venda venda;
    
    private String dataInicial1 = "";
    private String dataInicial2 = "";
    private String dataFinal1 = "";
    private String dataFinal2 = "";
    String array1[] = new String[3];
    String array2[] = new String[3];
    
    //Método de inicialização
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*setando connection que foi aberta no meu atributo ItemDeVendaDAO*/
        itemDeVendaDAO.setConnection(connection);
        vendaDAO.setConnection(connection);
        
    }    
    
    /*public Venda getVenda(){
        return venda;
    }
    
    public void setVenda(Venda venda){
        //recebendo dados setados pelo usuario para o banco de dados
        this.venda = venda;  
        //
        this.textFieldRelatorioResultado.setText(String.format("%.2f", venda.getValor()));
    }*/
    
    
    /*Metodo para trazer valor total vendido entre dutas datas*/
    //DATAPICKER
    /*@FXML
    public void handleButtonPesquisar() {
        //Produto produto = new Produto();
        //ItemDeVenda itemDeVenda = new ItemDeVenda();
        Venda venda = new Venda();
        //setando codigo do produto
        //produto.setCdProduto(textFieldVendaProduto.getText());
        //vendaDAO
        dataInicial1 = (String.format(datePickerRelatorioVendaData1.getValue()));
        dataInicial2 = (String.format(datePickerRelatorioVendaData2.getValue()));
            
        array1 = dataInicial1.split("/");
        dataFinal1 = array1[2]+"-"+array1[1]+"-"+array1[0];
        
        array2 = dataInicial2.split("/");
        dataFinal2 = array2[2]+"-"+array2[1]+"-"+array2[0];
        
        //venda.setData1(datePickerRelatorioVendaData1.getValue());
        //venda.setData2(datePickerRelatorioVendaData2.getValue());
        
        //Caso o produto nao seja nulo 
        if (datePickerRelatorioVendaData1.getValue()!= null) {
            if (datePickerRelatorioVendaData2.getValue() != null){  
            //objeto venda recebe data setado pelo usuario
            //produto = produtoDAO.buscar(produto);
            venda = vendaDAO.BuscarValorVendas(venda);

            textFieldRelatorioResultado.setText(String.format("%.2f", venda.getValor()));
     
            } else {
                //mostrando mensagem de erro caso qtd de produtos selecinados forem maiores do q a do banco
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Problemas na Busca de Vendas!");
                alert.setContentText("Prenencha os calendarios!");
                alert.show();
            }
        }
    }*/
    
    
    
    /*Metodo para trazer valor total vendido entre dutas datas*/
    @FXML
    public void handleButtonPesquisar() {
        //Produto produto = new Produto();
        //ItemDeVenda itemDeVenda = new ItemDeVenda();
        Venda venda = new Venda();
        //setando codigo do produto
        //produto.setCdProduto(textFieldVendaProduto.getText());
        //vendaDAO
        
        //dataInicial1 = (String.format(datePickerRelatorioVendaData1.toString()));
        dataInicial1 = (String.format(textFieldRelatorioVendaData1.getText()));
        dataInicial2 = (String.format(textFieldRelatorioVendaData2.getText()));
        
        array1 = dataInicial1.split("/");
        dataFinal1 = array1[2]+"-"+array1[1]+"-"+array1[0];
        
        array2 = dataInicial2.split("/");
        dataFinal2 = array2[2]+"-"+array2[1]+"-"+array2[0];
        
        System.out.println(dataFinal2);
        System.out.println(dataFinal1);
        venda.setData1(Date.valueOf(dataFinal1));
        venda.setData2(Date.valueOf(dataFinal2));
        
        //Caso o data nao seja nula
        if (dataFinal1 != null) {
            if (dataFinal2 != null){  
            //objeto venda recebe resultado da query 
            //produto = produtoDAO.buscar(produto);
            venda = vendaDAO.BuscarValorVendas(venda);

            textFieldRelatorioResultado.setText(String.format("%.2f", venda.getValor()));
     
            } else {
                //mostrando mensagem de erro caso qtd de produtos selecinados forem maiores do q a do banco
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Problemas na Busca de Vendas!");
                alert.setContentText("Prenencha os calendarios!");
                alert.show();
            }
        }
    }
    
}
