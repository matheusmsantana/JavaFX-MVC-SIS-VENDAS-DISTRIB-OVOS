package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
import javafxmvc.model.dao.CategoriaDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Produto;

public class FXMLAnchorPaneCadastrosProdutosController implements Initializable {
    //Atributos da interface grafica
    @FXML
    private TableView<Produto> tableViewProdutos;
    @FXML
    private TableColumn<Produto, String> TableColumnProdutosCodigo;
    @FXML
    private TableColumn<Produto, String> TableColumnProdutosNome;
    @FXML 
    private TableColumn<Produto, Double> TableColumnProdutosPreco;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
    @FXML
    private Label LabelCadastroProdutosCodigo;
    @FXML
    private Label LabelCadastroProdutosNome;
    @FXML
    private  Label LabelCadastroProdutosPreco;
    
    /*o Atributo do tipo List: serve para transferir a consulta do banco para ela 
    Essa list e do tipo Produto*/
    private List<Produto> listProdutos;
    /*ObservableList é uma lista observavel, ela serve para que quando eu
    adicionar produtos que sao produtos pertencentes a um ObservableList
    em um componente grafico, qualquer alteração que for feita no componente
    grafico ira refletir na ObservableList*/
    private ObservableList<Produto> observableListProdutos;
    
    //Atributos de conexão com banco de dados
    /*solicitando a minha fabrica de banco de dados "Database", pra que me forneça 
    uma instancia de uma classe postgresql, ou seja falando que meu banco vai ser um postgresql*/
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    /*Esse atributo esta recebendo um metodo que se conecta com o o banco de dados*/
    private final Connection connection = database.conectar();
    /*Instanciando a classe produtoDAO que contem metodos de sql*/
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    /*Instanciando a classe categoriaDAO que contem metodos de sql*/
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    
    //Metodo de inicialização
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Setando a conecxão com banco de dados atravez da minha instancia produtoDAO
        produtoDAO.setConnection(connection);
        //Chamando meu metodo para carregar minha tableViewProdutos de produtos
        carregarTableViewProdutos();
        /*Metodo que seta as propriedades das minhas labels de produtos como null
        para que nada seja setado ate eu setar*/
        selecionarItemTableViewProdutos(null);
        
        /*Listen acionado diante  de qualquer alteração na seleçao de itens do tableView,
        ou seja ele vai observar qualquer evento do mause ou teclado e passavar via paramento 
        para a minha função selecionarItemTableViewProdutos*/
        tableViewProdutos.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> selecionarItemTableViewProdutos(newValue));
    }    
  
    
    //função para setar oque foi selecionado e observado pelo meu listen na minha label
    public void selecionarItemTableViewProdutos(Produto produto){
            if (produto != null) {
            //convertendo oque foi selecionado pelo usuario para as minhas labels
            LabelCadastroProdutosCodigo.setText(String.valueOf(produto.getCdProduto()));
            LabelCadastroProdutosNome.setText(String.valueOf(produto.getNome()));
            LabelCadastroProdutosPreco.setText(String.format("%.2f", produto.getPreco()));
        } else {
            LabelCadastroProdutosCodigo.setText("");
            LabelCadastroProdutosNome.setText("");
            LabelCadastroProdutosPreco.setText("");
        }
            
    }
    
    //Metodo para carregar minha table view de produtos
    public void carregarTableViewProdutos() {
        //Configurando as colunas
        //TableColumnProdutosCodigo vai exibir o codigo do produto
        TableColumnProdutosCodigo.setCellValueFactory(new PropertyValueFactory<>("cdProduto"));
        //TableColumnProdutosNome vai exibir nome do produto
        TableColumnProdutosNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        //TableColumnProdutosPreco vai exirbir o preço do produto
        TableColumnProdutosPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        
        //a minha listProdutos vai receber o meu objeto produtoDAO com a função listar();
        listProdutos = produtoDAO.listar();
        
        //meu observableListProdutos esta recebendo minha listProdutos
        observableListProdutos = FXCollections.observableArrayList(listProdutos);
        /*passando meu observableListProdutos para meu tableViewClientes ou seja
        assim ele seta todos os meus produtos na minha tela tableViewProdutos*/
        tableViewProdutos.setItems(observableListProdutos); 
    }
    
    
    //Metodo do meu buttonInsirir para insirir um produto. 
    @FXML
    public void handleButtonInserir() throws IOException {
        //Instanciando um novo produto
        Produto produto = new Produto();
        //Chamando minha função que abre minha tela de inserção de dados do produto
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosProdutosDialog(produto);
        //Validação de confirmação se for verdadeiro 
        if (buttonConfirmarClicked) {
            /*chamo minha função de insert produtoDAO que 
            esta no pacote model.dao e faço o insert do produto*/
            produtoDAO.inserir(produto);
            /*carregando a tabela de produtos para que ela 
            seja atualizada com novo produto inserido*/
            carregarTableViewProdutos();
        }
    }
    
    
    //Metodo do meu button de alteração para alterar um produto ja inserido no banco de dados
    public void handleButtonAlterar() throws IOException{
        //Pegando o produto que foi setada pelo usuario
        Produto produto = tableViewProdutos.getSelectionModel().getSelectedItem();
        //se o produto não for nullo
        if (produto != null){
        //Chamando minha função que abre minha tela de alteração de dados do produto
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosProdutosDialog(produto);
            //Validação de confirmação se for verdadeiro
            if (buttonConfirmarClicked) {
                /*chamo minha função de update produtoDAO que 
                esta no pacote model.dao e faço o update*/
                produtoDAO.alterar(produto);
                /*carregando a tabela de produtos para que ela 
                seja atualizada com update do produto*/
                carregarTableViewProdutos();
            }
        //Caso usuario nao tenha setado um produto ele jera essa mensagem de erro
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um produto na Tabela!");
            alert.show();
        }
        
    }
    
    
    //Metodo para o meu botão de remoção de produto
    @FXML
    public void handleButtonRemover() throws IOException {
        //Pegando o produto que foi setado pelo usuario
        Produto produto = tableViewProdutos.getSelectionModel().getSelectedItem();
        if (produto != null) {
            /*chamo minha função de delete produtoDAO que 
            esta no pacote model.dao e faço o delete*/
            produtoDAO.remover(produto);
            /*carregando a tabela de produtos para que ela 
            seja atualizada com update do produto*/
            carregarTableViewProdutos();
            //Caso usuario nao sete um produto para exclusao e click no button remover ele gera
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um produto na Tabela!");
            alert.show();
        }
    }
    
    
    //Metodo que  mostra a janela de inserção ou alteraçao dos dados
    public boolean showFXMLAnchorPaneCadastrosProdutosDialog(Produto produto) throws IOException {
        //criando um atributo do tipo FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        //Carregando a janela que tem os bunttons de confirmaçao de criaçao e remoção de produtos 
        loader.setLocation(FXMLAnchorPaneCadastrosProdutosDialogController.class.getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosProdutosDialog.fxml"));
        //criando um atributo para guardar em cache minha janela que esta carregada
        AnchorPane page = (AnchorPane) loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        //Setando um titulo para minha janela
        dialogStage.setTitle("Registro de Produtos");
        //instanciando minha cena com a minha janela carregada
        Scene scene = new Scene(page);
        //Setando no meu estagio a minha cena 
        dialogStage.setScene(scene);

        // Setando o produto no Controller.
        /*setando o produto que foi selecionado pelo usuario 
        ou setando uma nova instancia de um produto*/
        FXMLAnchorPaneCadastrosProdutosDialogController controller = loader.getController();
        //setando minha dialogStage no meu controller
        controller.setDialogStage(dialogStage);
        controller.setProduto(produto);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();
        //retornando estado do meu button confirmar
        return controller.isbuttonConfirmarClicked();

    }
    
}