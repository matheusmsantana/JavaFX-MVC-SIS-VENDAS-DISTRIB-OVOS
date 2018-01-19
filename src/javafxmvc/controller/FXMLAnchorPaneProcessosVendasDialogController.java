package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafxmvc.model.dao.ClienteDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.dao.VendaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Cliente;
import javafxmvc.model.domain.ItemDeVenda;
import javafxmvc.model.domain.Produto;
import javafxmvc.model.domain.Venda;

public class FXMLAnchorPaneProcessosVendasDialogController implements Initializable {
    
    //Atributos da interface grafica
    @FXML
    private ComboBox comboBoxVendaCliente;
    @FXML
    private DatePicker datePickerVendaData;
    @FXML
    private CheckBox checkBoxVendaPago;
    @FXML
    //private ComboBox comboBoxVendaProduto;
    private TextField textFieldVendaProduto;
    @FXML
    private TextField textFieldVendaQtd;
    //private TextField textFieldItemDeVendaQtd;
    @FXML
    private Button buttonAdd;
    //private Button buttonAdicionar;
    @FXML
    private TableView<ItemDeVenda> tableViewVendaItens;
    @FXML
    private TableColumn<ItemDeVenda, Produto> tableColumnValor;
    @FXML
    private TableColumn<ItemDeVenda, Integer> tableColumnQuantidade;
    @FXML
    private TableColumn<ItemDeVenda, Double> tableColumnProduto;
    @FXML 
    private TextField textFieldVendaValor;
    //
    @FXML
    private TextField textFieldVendaDinheiro;
    @FXML
    private TextField textFieldVendaTroco;
    @FXML
    private Button buttonCancelar;
    @FXML
    private Button buttonCalcular;
    @FXML
    private Button ButtonConfirmar;
    //Atributos do meu metodo de troco
    private double Calculo;
    private double Dinheiro;
    
    Cliente cliente;
    //Getters e Setter de metodo de troco
    private double getCauculo(){
        return Calculo;
    }
    
    public void setCalcuto(double Calculo) {
        this.Calculo = Calculo;
    }
    
    
    public double getDinheiro(){
        return Dinheiro;
    }
    
    public void setDinheiro(double Dinheiro) {
        this.Dinheiro = Dinheiro;
    }
    
    
    /*Atributos de lista para listar listClientes*/
    private List<Cliente> listClientes;
    /*Atributos de lista para listar listProdutos*/
    private List<Produto> listProdutos;
    
    
    //observablelist de clientes
    private ObservableList<Cliente> observableListClientes;
    //observablelist de produtos
    //private ObservableList<Produto> observableListVendas;
    //observablelist de itens de venda
    private ObservableList<ItemDeVenda> observableListDeItensDeVenda;
    
    
    //Atributos para manipulação do banco de dados
    //Fazendo acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    /*Esse metodo esta indo ao banco de dados e se conectando com meu banco de dados*/
    private final Connection connection = database.conectar();
    /*Instanciando a classe ClienteDAO que contem metodos de sql*/
    private final ClienteDAO clienteDAO = new ClienteDAO();
    /*Instanciando a classe ClienteDAO que contem metodos de sql*/
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    //Atributo para estagio, no caso para verificar se minha tela esta setado 
    private Stage dialogStage;
    //Atributo para saber se o usuario cliclou no botao confirmar ou cancelar
    private boolean buttonConfirmarClicked = false;
    //Atributo para preenchimento de classe pra inserir no banco de dados
    private Venda venda;
    
    //Classe de inicialização
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*setando connection que foi aberta no meu atributo clienteDAO*/
        clienteDAO.setConnection(connection);
        /*setando connection que foi aberta no meu atributo produtoDAO*/
        produtoDAO.setConnection(connection);
        //chamando meus metodos pra carregar o combo de cliente e produto
        carregarComboBoxClientes();
        //carregarcomboBoxVendaProduto();
        /*configurando as colunas da TableViewVendas indicando os atributos de select
        da minha classe ProdutoDAO a serem ixibidos
        */
        tableColumnProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }
      
       //Metodo para carregar meu comboBox de clientes no caso estou populando ele
       public void carregarComboBoxClientes() {
        /*Minha listClientes esta recebendo uma isntancia da minha classe ClienteDAO
        com metodo listar*/    
        listClientes = clienteDAO.listar();
        //Meu observableListClientes esta recebendo minha list
        //observableListClientes = FXCollections.observableArrayList(listClientes.set(0, cliente));
        observableListClientes = FXCollections.observableArrayList(listClientes);
        //Setando meu observableListVendas no meu comboBoxVendaCliente
        //comboBoxVendaCliente.setItems(observableListClientes);
        comboBoxVendaCliente.setItems(observableListClientes);
        //comboBoxVendaCliente.;
        
        //chinelada = comboBoxVendaCliente.setSelectionModel().;
    }
       
    
    //Metodo para carregar meu comboBox de produtos no caso estou populando ele
    /*public void carregarcomboBoxVendaProduto(){
       listProdutos =  produtoDAO.listar();
       observableListVendas = FXCollections.observableArrayList(listProdutos);
       comboBoxVendaProduto.setItems(observableListVendas);
    }*/
    
    //Metodos Gets e Setters
    public Stage getDialogStage(){
        return dialogStage;
    }
    
    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }
    
    public boolean isbuttonConfirmarClicked(){
       return buttonConfirmarClicked; 
    }
    
    //public void setbuttonConfirmarClicked(boolean buttonConfirmarClicked){
      //  this.buttonConfirmarClicked = buttonConfirmarClicked;
    //}
    
    public Venda getVenda(){
        return venda;
    }
    
    public void setVenda(Venda venda){
        //recebendo dados setados pelo usuario para o banco de dados
        this.venda = venda;  
        //pegando os dados das coluna e deixando pre selecionados nos campos de alteração
        //this.comboBoxVendaCliente.setItems(observableListClientes);
        //this.textFieldItemDeVendaQtd.setText(String.format("%d", venda.()));
        //this.comboBoxVendaProduto.setItems(observableListVendas);
        this.textFieldVendaValor.setText(String.format("%.2f", venda.getValor()));
        //this.tableViewVendaItens.setT
        //this.tableColumnProduto.setCellValueFactory(new PropertyValueFactory<>("nome"));
        //this.checkBoxVendaPago.setCacheShape(venda.getPago());
        //this.datePickerVendaData.setValue(venda.getValue());       
        //this.textFieldClienteCPF.setText(venda.getCpf());
    }
    
    /*Metodo de COMBOBOX acionado ao clicar no button adicionar*/
    /*@FXML
    public void handleButtonAdicionar() {
        Produto produto;
        ItemDeVenda itemDeVenda = new ItemDeVenda();
        //Caso o produto nao seja nulo 
        if (comboBoxVendaProduto.getSelectionModel().getSelectedItem() != null) {
            //variavel produto recebe esse produto
            produto = (Produto) comboBoxVendaProduto.getSelectionModel().getSelectedItem();
            //verificando se a qtd de produtos que estao sendo comprados e maior q a do banco de dados
            if (produto.getQuantidade() >= Integer.parseInt(textFieldItemDeVendaQtd.getText())) {
                //setando produto valor
                itemDeVenda.setProduto((Produto) comboBoxVendaProduto.getSelectionModel().getSelectedItem());
                //setando quantidade de produtos
                itemDeVenda.setQuantidade(Integer.parseInt(textFieldItemDeVendaQtd.getText()));
                //setando valor dos produtos
                itemDeVenda.setValor(itemDeVenda.getProduto().getPreco() * itemDeVenda.getQuantidade());
                //setando esse item de venda no arrayList de itemDeVenda
                venda.getItensDeVenda().add(itemDeVenda);
                //atualizando o valor do item de venda
                venda.setValor(venda.getValor() + itemDeVenda.getValor());
                //passando meu arrayList para minha observableListDeItensDeVenda
                observableListDeItensDeVenda = FXCollections.observableArrayList(venda.getItensDeVenda());
                //setando observableListDeItensDeVenda na minha tableViewVendaItens
                tableViewVendaItens.setItems(observableListDeItensDeVenda);
                //mostrando na textFieldVendaValor o valor da venda
                textFieldVendaValor.setText(String.format("%.2f", venda.getValor()));
            } else {
                //mostrando mensagem de erro caso qtd de produtos selecinados forem maiores do q a do banco
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Problemas na escolha do produto!");
                alert.setContentText("Não existe a quantidade de produtos disponíveis no estoque!");
                alert.show();
            }
        }
    }*/
    
    
    /*Metodo CÓDIGO DE BARRAS acionado ao clicar no button adicionar*/
    @FXML
    public void handleButtonAdicionar() {
        Produto produto = new Produto();
        ItemDeVenda itemDeVenda = new ItemDeVenda();
        //setando codigo do produto
        produto.setCdProduto(textFieldVendaProduto.getText());
        //Caso o produto nao seja nulo 
        if (textFieldVendaProduto.getText()!= null) {
            //objeto produto recebe produto setado pelo vendedor
            produto = produtoDAO.buscar(produto);
            //verificando se a qtd de produtos que estao sendo comprados e maior q a do banco de dados
            if (produto.getQuantidade() >= Integer.parseInt(textFieldVendaQtd.getText())) {
                //setando produto valor
                itemDeVenda.setProduto(produto);
                //setando quantidade de produtos
                itemDeVenda.setQuantidade(Integer.parseInt(textFieldVendaQtd.getText()));
                //setando valor dos produtos de acordo com a quantidade 
                itemDeVenda.setValor(itemDeVenda.getProduto().getPreco() * itemDeVenda.getQuantidade());
                //setando esse item de venda no arrayList de itemDeVenda
                venda.getItensDeVenda().add(itemDeVenda);
                //atualizando o valor do item de venda
                venda.setValor(venda.getValor() + itemDeVenda.getValor());
                //passando meu arrayList para minha observableListDeItensDeVenda
                observableListDeItensDeVenda = FXCollections.observableArrayList(venda.getItensDeVenda());
                //setando observableListDeItensDeVenda na minha tableViewVendaItens
                tableViewVendaItens.setItems(observableListDeItensDeVenda);
                //mostrando na textFieldVendaValor o valor da venda
                textFieldVendaValor.setText(String.format("%.2f", venda.getValor()));
                
                //this.textFieldVendaDinheiro.getText(String.format("%.2f", args)) - venda.getValor();
                //this.textFieldProdutoNome.setText(produto.getNome());
                
                //textFieldVendaDinheiro.setText(String.format("%.2f", Dinheiro));
                //Calculo = Dinheiro - venda.getValor();
                //textFieldVendaTroco.setText(String.format("%.2f", Calculo));
                textFieldVendaProduto.setText("");
            } else {
                //mostrando mensagem de erro caso qtd de produtos selecinados forem maiores do q a do banco
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Problemas na escolha do produto!");
                alert.setContentText("Não existe a quantidade de produtos disponíveis no estoque!");
                alert.show();
            }
        }
    }
    
    //Método para Calcular troco do Cliente    
    public void handleButtonCalcular(){
        Dinheiro = (Double.parseDouble(textFieldVendaDinheiro.getText().replace(",", ".")));
        Calculo = (Dinheiro - venda.getValor());
        textFieldVendaTroco.setText(String.format("%.2f", Calculo));
    }
    
    //Quando o usuario clicar no button confirmar ele vai chamar esta função
    @FXML
    public void handleButtonConfirmar(){
        //se a minha validação de venda for true
        if(validarEntradaDeDados()){
            //eu seto minha venda com as outras informações que ainda faltam
            //setando cliente de acordo com oque foi escolhido
            venda.setCliente((Cliente) comboBoxVendaCliente.getSelectionModel().getSelectedItem());
            //setando meu check box de acordo com oque foi pago
            venda.setPago(checkBoxVendaPago.isSelected());
            //setando a data de acordo com oque foi escolhido
            venda.setData(datePickerVendaData.getValue());
            //confirmando que o usuario aperto botao confirmar
            buttonConfirmarClicked = true;
            //fechando janela
            dialogStage.close();
        }
    }
    
    //handleButtonCancelar vai ser o metodo acionado quando o cliente clicar em cancelar 
    @FXML 
    public void handleButtonCancelar(){
        //Fechando a janela 
        dialogStage.close();
    }
    
    @FXML
    private boolean validarEntradaDeDados(){
        String errorMessage = "";
           if (comboBoxVendaCliente.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Cliente Invalido!\n";
        } 
           if(datePickerVendaData.getValue() == null){
            errorMessage += "Data Invalida! \n";
        }
           if(observableListDeItensDeVenda == null){
               errorMessage += "Itens de Venda invalidos! \n";
        }
           if(errorMessage.length() == 0){
               return true; 
           } else {
               
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error no cadastro");
               alert.setHeaderText("Campos inválidos por favor corrija...");
               alert.setContentText(errorMessage);
               alert.show();
               return false;
           }
    }
            
}