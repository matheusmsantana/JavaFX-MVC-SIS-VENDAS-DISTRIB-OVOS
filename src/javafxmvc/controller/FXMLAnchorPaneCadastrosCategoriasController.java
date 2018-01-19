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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxmvc.model.dao.CategoriaDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;
import javafxmvc.model.domain.Produto;

public class FXMLAnchorPaneCadastrosCategoriasController implements Initializable {
    //Atributos da interface grafica
    @FXML
    private TableView<Categoria> tableViewCategoria;
    @FXML
    private TableColumn<Categoria, Integer> tableColumnCategoriaCodigo;
    @FXML 
    private TableColumn<Categoria, String> tableColumnCategoriaNome;
    @FXML
    private Label labelCategoriaCodigo;
    @FXML 
    private Label labelCategoriaNome;
    
    //Atributos do metodo que conta quantos produtos tem em cada categoria
    /*@FXML
    private TextField textFieldCategoriaCodigo;
    @FXML 
    private Button buttonPesquisarQtdProdutos;
    @FXML
    private Label labelCategoriaQtdProduto;*/

    
    /*o Atributo do tipo List: serve para transferir a consulta do banco para ela 
    Essa list e do tipo Categoria*/
    private List<Categoria> listCategorias;
    /*ObservableList é uma lista observavel, ela serve para que quando eu
    adicionar categorias que sao categorias pertencentes a um ObservableList
    em um componente grafico, qualquer alteração que for feita no componente
    grafico ira refletir na ObservableList*/
    private ObservableList<Categoria> observableListCategorias;
    
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
        //Setando a conecxão com banco de dados atravez da minha instancia categoriaDAO
        categoriaDAO.setConnection(connection);
        //Chamando meu metodo para carregar minha tableViewCategoria de categorias
        carregarTableViewCategorias();
        /*Metodo que seta as propriedades das minhas labels de produtos como null
        para que nada seja setado ate eu setar*/
        selecionarItemTableViewCategorias(null);
        
        /*Listen acionado diante  de qualquer alteração na seleçao de itens do tableView,
        ou seja ele vai observar qualquer evento do mause ou teclado e passavar via paramento 
        para a minha função selecionarItemTableViewCategorias*/
        tableViewCategoria.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> selecionarItemTableViewCategorias(newValue));
        
    }    
    
    //função para setar oque foi selecionado e observado pelo meu listen na minha label
    public void selecionarItemTableViewCategorias(Categoria categoria){
            if (categoria != null) {
            //convertendo oque foi selecionado pelo usuario para as minhas labels
            labelCategoriaCodigo.setText(String.valueOf(categoria.getCdCategoria()));
            labelCategoriaNome.setText(String.valueOf(categoria.getDescricao()));
        } else {
            labelCategoriaCodigo.setText("");
            labelCategoriaNome.setText("");
        }
            
    }
    
    //Metodo para carregar minha table view de categorias
    public void carregarTableViewCategorias() {
        //Configurando as colunas
        //tableColumnCategoriaCodigo vai exibir o codigo da categoria
        tableColumnCategoriaCodigo.setCellValueFactory(new PropertyValueFactory<>("cdCategoria"));
        //tableColumnCategoriaNome vai exibir nome da categoria
        tableColumnCategoriaNome.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        //a minha listProdutos vai receber o meu objeto produtoDAO com a função listar();
        listCategorias = categoriaDAO.listar();
        
        //meu observableListProdutos esta recebendo minha listProdutos
        observableListCategorias = FXCollections.observableArrayList(listCategorias);
        /*passando meu observableListProdutos para meu tableViewClientes ou seja
        assim ele seta todos os meus produtos na minha tela tableViewProdutos*/
        tableViewCategoria.setItems(observableListCategorias); 
    }
    
    
   //Metodo do meu buttonInsirir para insirir uma categoria. 
    @FXML
    public void handleButtonInserir() throws IOException {
        //Instanciando um novo produto
        Categoria categoria = new Categoria();
        //Chamando minha função que abre minha tela de inserção de dados da categoria
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosCategoriasDialog(categoria);
        //Validação de confirmação se for verdadeiro 
        if (buttonConfirmarClicked) {
            /*chamo minha função de insert produtoDAO que 
            esta no pacote model.dao e faço o insert da categoria*/
            categoriaDAO.inserir(categoria);
            /*carregando a tabela de produtos para que ela 
            seja atualizada com nova categoria inserida*/
            carregarTableViewCategorias();
        }
    }
    
    //Metodo do meu button de alteração para alterar uma categoria ja inserido no banco de dados
    public void handleButtonAlterar() throws IOException{
        //Pegando a categoria que foi setada pelo usuario
        Categoria categoria = tableViewCategoria.getSelectionModel().getSelectedItem();
        //se a categoria não for nulla
        if (categoria != null){
        //Chamando minha função que abre minha tela de alteração de dados da categoria
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosCategoriasDialog(categoria);
            //Validação de confirmação se for verdadeiro
            if (buttonConfirmarClicked) {
                /*chamo minha função de update categoriaDAO que 
                esta no pacote model.dao e faço o update*/
                categoriaDAO.alterar(categoria);
                /*carregando a tabela de categorias para que ela 
                seja atualizada com update da categoria*/
                carregarTableViewCategorias();
            }
        //Caso usuario nao tenha setado uma categoria ele jera essa mensagem de erro
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma categoria na Tabela!");
            alert.show();
        }
        
    }
    
    //Metodo para o meu botão de remoção da categoria
    @FXML
    public void handleButtonRemover() throws IOException {
        //Pegando a categoria que foi setado pelo usuario
        Categoria categoria = tableViewCategoria.getSelectionModel().getSelectedItem();
        if (categoria != null) {
            /*chamo minha função de delete produtoDAO que 
            esta no pacote model.dao e faço o delete*/
            categoriaDAO.remover(categoria);
            /*carregando a tabela de categorias para que ela 
            seja atualizada com update do produto*/
            carregarTableViewCategorias();
            //Caso usuario nao sete uma categoria para exclusao e click no button remover ele gera
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma categoria na Tabela!");
            alert.show();
        }
    }
    
    //Metodo que  mostra a janela de inserção ou alteraçao dos dados
    public boolean showFXMLAnchorPaneCadastrosCategoriasDialog(Categoria categoria) throws IOException {
        //criando um atributo do tipo FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        //Carregando a janela que tem os bunttons de confirmaçao de criaçao e remoção de produtos 
        loader.setLocation(FXMLAnchorPaneCadastrosCategoriasDialogController.class.getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosCategoriasDialog.fxml"));
        //criando um atributo para guardar em cache minha janela que esta carregada
        AnchorPane page = (AnchorPane) loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        //Setando um titulo para minha janela
        dialogStage.setTitle("Registro de Categorias");
        //instanciando minha cena com a minha janela carregada
        Scene scene = new Scene(page);
        //Setando no meu estagio a minha cena 
        dialogStage.setScene(scene);

        // Setando o produto no Controller.
        /*setando o produto que foi selecionado pelo usuario 
        ou setando uma nova instancia de um produto*/
        FXMLAnchorPaneCadastrosCategoriasDialogController controller = loader.getController();
        //setando minha dialogStage no meu controller
        controller.setDialogStage(dialogStage);
        controller.setCategoria(categoria);
        //controller.set(categoria);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();
        //retornando estado do meu button confirmar
        return controller.isButtonConfirmarClicked();

    }
    
    /*@FXML
    public void handleButtonPesquisar(Produto produto){
        textFieldCategoriaCodigo.getText();
        //Produto produto = textFieldCategoriaCodigo.getSelectedText();
        if(textFieldCategoriaCodigo != null){
            labelCategoriaQtdProduto.setText(String.format("%d", produto.getQtdProduto()));
        } 
    }*/
    
}
