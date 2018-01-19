package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxmvc.model.dao.ClienteDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Cliente;

public class FXMLAnchorPaneCadastrosClientesController implements Initializable {
    
    //Atributos da minha interface grafica tela de clientes
    //<Cliente>: serve para indicar que esse atributo e da classe cliente
    @FXML
    private TableView<Cliente> tableViewClientes;
    @FXML
    private TableColumn<Cliente, String> tableColumnClienteNome;
    @FXML
    private TableColumn<Cliente, String> tableColumnClienteCPF;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
    @FXML
    private Label labelClienteCodigo;
    @FXML
    private Label labelClienteNome;
    @FXML
    private Label labelClienteCPF;
    @FXML
    private Label labelClienteTelefone;
    
    /*o Atributo do tipo List serve para que quando minha classe for ao banco
    de dados me traga uma lista de clientes*/
    private List<Cliente> listClientes;
    /*ObservableList é uma lista observavel, ela serve para que quando eu
    adicionar clientes que sao clientes pertencentes a um ObservableList
    em um componente grafico, qualquer alteração que for feita no componente
    grafico ira refletir na ObservableList*/
    private ObservableList<Cliente> observableListClientes;

    //Fazendo acesso ao banco de dados
    //Atributos para manipulação do banco de dados
    
    /*solicitando a minha fabrica de banco de dados pra que me forneça
    uma instancia de uma classe postgresql, ou seja falando que meu banco vai ser um postgresql*/
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    /*Esse metodo esta indo ao banco de dados e se conectando com meu banco de dados*/
    private final Connection connection = database.conectar();
    /*Instanciando a classe ClienteDAO que contem metodos de sql*/
    private final ClienteDAO clienteDAO = new ClienteDAO();
    
    //Classe de inicialização
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Setando a conecxão com banco de dados atravez da minha instancia ClienteDAO
        clienteDAO.setConnection(connection);
        //Chamando meu metodo para carregar minha tableview de clientes
        carregarTableViewCliente();
        //Setando que seta propriedades do cliente como null 
        selecionarItemTableViewClientes(null);
        /*Listen acionado diante  de qualquer alteração na seleçao de itens do tableView,
        ou seja ele vai observar qualquer evento do mause ou teclado e passavar via paramento 
        para a minha função selecionarItemTableViewClientes*/
        tableViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewClientes(newValue));

    }
    
    //Metodo para carregar minha table view de clientes
    public void carregarTableViewCliente() {
        //Configurando as colunas
        //tableColumnClienteNome vai exibir o nome do cliente
        tableColumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        //tableColumnClienteCPF vai exibir o cpf do cliente
        tableColumnClienteCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        
        //a minha listClientes vai receber o meu objeto clienteDAO com a função listar();
        listClientes = clienteDAO.listar();
        
        //meu observableListClientes esta recebendo minha listClientes
        observableListClientes = FXCollections.observableArrayList(listClientes);
        /*passando meu observableListClientes para meu tableViewClientes ou seja
        assim ele seta todos os meus clientes na minha tela tableViewClientes*/
        tableViewClientes.setItems(observableListClientes);
    }
    
    //função para setar oque foi selecionado e observado pelo meu listen na minha label
    public void selecionarItemTableViewClientes(Cliente cliente){
        if (cliente != null) {
            //convertendo oque foi selecionado pelo usuario para minha label
            labelClienteCodigo.setText(String.valueOf(cliente.getCdCliente()));
            //passando para a minha label oque foi selecionado pelo usuario
            labelClienteNome.setText(cliente.getNome());
            //passando para a minha label oque foi selecionado pelo usuario
            labelClienteCPF.setText(cliente.getCpf());
            //passando para a minha label oque foi selecionado pelo usuario
            labelClienteTelefone.setText(cliente.getTelefone());
        } else {
            labelClienteCodigo.setText("");
            labelClienteNome.setText("");
            labelClienteCPF.setText("");
            labelClienteTelefone.setText("");
        }

    }
    
    //Metodo do meu button de cadastro de cliente 
    @FXML
    public void handleButtonInserir() throws IOException {
        //Instanciando um novo cliente
        Cliente cliente = new Cliente();
        //Chamando minha função que abre minha tela de inserção de dados do cliente
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosClientesDialog(cliente);
        //Validação de confirmação se for verdadeiro 
        if (buttonConfirmarClicked) {
            /*chamo minha função de insert clienteDAO que 
            esta no pacote model.dao e faço o insert*/
            clienteDAO.inserir(cliente);
            /*carregando a tabela de clientes para que ela 
            seja atualizada com novo cliente inserido*/
            carregarTableViewCliente();
        }
    }
    //Metodo do meu button janela de alteração de ciente 
    @FXML
    public void handleButtonAlterar() throws IOException {
        //Pegando cliente que foi setado pelo usuario 
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            //Chamando minha função que abre minha tela de alteração de dados do cliente
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosClientesDialog(cliente);
            //Validação de confirmação se for verdadeiro 
            if (buttonConfirmarClicked) {
                /*chamo minha função de update clienteDAO que 
                esta no pacote model.dao e faço o insert*/
                clienteDAO.alterar(cliente);
                /*carregando a tabela de clientes para que ela 
                seja atualizada com update do cliente*/
                carregarTableViewCliente();
            }
            //Caso usuario nao tenha setado um cliente ele jera essa mensagem de erro
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente na Tabela!");
            alert.show();
        }
    }
    
    //Metodo para o meu botão de remoção de cliente
    @FXML
    public void handleButtonRemover() throws IOException {
        //Pegando cliente que foi setado pelo usuario
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            /*chamo minha função de delete clienteDAO que 
            esta no pacote model.dao e faço o insert*/
            clienteDAO.remover(cliente);
            /*carregando a tabela de clientes para que ela 
            seja atualizada com update do cliente*/
            carregarTableViewCliente();
            //Caso usuario nao sete um cliente para exclusao e click no button remover ele gera
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente na Tabela!");
            alert.show();
        }
    }
    
    //Metodo que  mostra a janela de inserção ou alteraçao dos dados
    public boolean showFXMLAnchorPaneCadastrosClientesDialog(Cliente cliente) throws IOException {
        //criando um atributo do tipo FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        //Carregando a janela que tem os bunttons de alteraçao de criaçao e remoção de cliente
        loader.setLocation(FXMLAnchorPaneCadastrosClientesDialogController.class.getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosClientesDialog.fxml"));
        //criando um atributo para guardar em cache minha janela que esta carregada
        AnchorPane page = (AnchorPane) loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        //Setando um titulo para minha janela
        dialogStage.setTitle("Cadastro de Clientes");
        //instanciando minha cena com a minha janela carregada
        Scene scene = new Scene(page);
        //Setando no meu estagio a minha cena 
        dialogStage.setScene(scene);

        // Setando o cliente no Controller.
        /*setando o cliente que foi selecionado pelo usuario 
        ou setando uma nova instancia de um cliente*/
        FXMLAnchorPaneCadastrosClientesDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();

    }

}
