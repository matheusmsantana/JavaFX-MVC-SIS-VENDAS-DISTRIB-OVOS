package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
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
import javafxmvc.model.dao.ItemDeVendaDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.dao.VendaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.ItemDeVenda;
import javafxmvc.model.domain.Produto;
import javafxmvc.model.domain.Venda;

public class FXMLAnchorPaneProcessosVendasController implements Initializable {
    
    //Atributos da Interface Grafica
    /*<Venda> quer dizer que esse atributo e da minha classe VendaDAO que esta 
    no pacote model.dao*/
    @FXML 
    private TableView<Venda> tableViewVendas;
    @FXML 
    private TableColumn<Venda, Integer> tableColumnVendaCodigo;
    @FXML
    private TableColumn<Venda, LocalDate> tableColumnVendaData;
    @FXML
    TableColumn <Venda, Venda> tableColumnVendaCliente;
    @FXML
    Button buttonInserir;
    @FXML
    //Button buttonAlterar;
    //@FXML
    Button buttonRemover;
    @FXML
    Label labelVendaCodigo;
    @FXML 
    Label labelVendaData;
    @FXML 
    Label labelVendaValor;
    @FXML 
    Label labelVendaPago;
    @FXML
    Label labelVendaCliente;
    
    /*o Atributo do tipo List serve para que quando minha classe for ao banco
    de dados me traga uma lista de clientes*/
    private List<Venda> listVendas;
    /*ObservableList é uma lista observavel, ela serve para que quando eu
    adicionar clientes que sao clientes pertencentes a um ObservableList
    em um componente grafico, qualquer alteração que for feita no componente
    grafico ira refletir na ObservableList*/
    private ObservableList<Venda> observableListVendas;

    //Fazendo acesso ao banco de dados
    //Atributos para manipulação do banco de dados
    
    /*solicitando a minha fabrica de banco de dados pra que me forneça
    uma instancia de uma classe postgresql, ou seja falando que meu banco vai ser um postgresql*/
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    /*Esse metodo esta indo ao banco de dados e se conectando com meu banco de dados*/
    private final Connection connection = database.conectar();
    /*Instanciando a classe VendaDAO, ItemDeVendaDAO, ProdutoDAO que contem metodos de sql*/
    private final VendaDAO vendaDAO = new VendaDAO();
    private final ItemDeVendaDAO itemDeVendaDAO = new ItemDeVendaDAO();
    private final ProdutoDAO produtoDao = new ProdutoDAO();
    
    
    //Metodo de inicialização
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       //listando todas as vendas que estao inseridas no banco de dados
       //setando a conexão do meu objeto vendaDAO com a minha conexão com banco de dados
       vendaDAO.setConnection(connection);
       //Chamando meu metodo para carregar minha tableViewVendas de vendas
       carregarTableViewVendas();
       //Metodo que seta as propriedades das minhas labels da venda como null 
       selecionarItemTableViewVendas(null);
       
        /*Listen acionado diante  de qualquer alteração na seleçao de itens do tableView,
        ou seja ele vai observar qualquer evento do mause ou teclado e passavar via paramento 
        para a minha função selecionarItemTableViewClientes*/
        tableViewVendas.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> selecionarItemTableViewVendas(newValue));
    }    
    
    //função para setar oque foi selecionado e observado pelo meu listen na minha label
    public void selecionarItemTableViewVendas(Venda venda){
            if (venda != null) {
            //convertendo oque foi selecionado pelo usuario para minha label
            labelVendaCodigo.setText(String.valueOf(venda.getCdVenda()));
            labelVendaData.setText(String.valueOf(venda.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            labelVendaValor.setText(String.format("%.2f", venda.getValor()));
            labelVendaPago.setText(String.valueOf(venda.getPago()));
            labelVendaCliente.setText(venda.getCliente().toString());
        } else {
            labelVendaCodigo.setText("");
            labelVendaData.setText("");
            labelVendaValor.setText("");
            labelVendaPago.setText("");
            labelVendaCliente.setText("");
        }
            
    }
    
    //Metodo para carregar minha table view de clientes
    public void carregarTableViewVendas(){
        //Configurando as colunas
        //tableColumnVendaCodigo vai exibir o  codigo da venda
        tableColumnVendaCodigo.setCellValueFactory(new PropertyValueFactory<>("cdVenda"));
        //tableColumnVendaData vai exibir o data da venda
        tableColumnVendaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        //tableColumnVendaCliente vai exibir o vendedor da venda
        tableColumnVendaCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        
        //a minha listVendas vai receber o meu objeto clienteDAO com a função listar();
        listVendas = vendaDAO.listar();
        
        //meu observableListClientes esta recebendo minha listClientes
        observableListVendas = FXCollections.observableArrayList(listVendas);
        /*passando meu observableListClientes para meu tableViewClientes ou seja
        assim ele seta todos os meus clientes na minha tela tableViewClientes*/
        tableViewVendas.setItems(observableListVendas);   
    }
    
    //Metodo do meu buttonInserir para venda de produto. 
    @FXML
    public void handleButtonInserir() throws IOException, SQLException {
        //Instanciando venda nova
        Venda venda = new Venda();
        //meu List<ItemDeVenda> recebe arraylist
        List<ItemDeVenda> listItensDeVenda = new ArrayList<>();
        //setando List<ItemDeVenda> na minha venda
        venda.setItensDeVenda(listItensDeVenda);
        //verificano se meu buttonconfirmar foi clicado 
        boolean buttonConfirmarClicked = showFXMLAnchorPaneProcessosVendasDialog(venda);
        //se foi clicado 
        if (buttonConfirmarClicked) {
            try {
                /*setando autocommit como false para que nao insira nd ate eu chamar 
                meu metodo connection.commit()*/
                connection.setAutoCommit(false);
                //setando conexão com minha instancia vendaDAO
                vendaDAO.setConnection(connection);
                //pré inserindo os dados da minha venda da minha isntancia.
                vendaDAO.inserir(venda);
                //setando conexao com minha instancia de itemDeVendaDAO
                itemDeVendaDAO.setConnection(connection);
                //setando conexao com minha instancia de produtoDao
                produtoDao.setConnection(connection);
                //
                for (ItemDeVenda listItemDeVenda : venda.getItensDeVenda()) {
                    //
                    Produto produto = listItemDeVenda.getProduto();
                    listItemDeVenda.setVenda(vendaDAO.buscarUltimaVenda());
                    itemDeVendaDAO.inserir(listItemDeVenda);
                    produto.setQuantidade(produto.getQuantidade() - listItemDeVenda.getQuantidade());
                    produtoDao.alterar(produto);
                }
                //efetuando as alterações e inserções no banco de dados
                connection.commit();
                //carregando a tabela novamente
                carregarTableViewVendas();
            } catch (SQLException ex) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    java.util.logging.Logger.getLogger(FXMLAnchorPaneProcessosVendasController.class.getName()).log(Level.SEVERE, null, ex1);
                }
                java.util.logging.Logger.getLogger(FXMLAnchorPaneProcessosVendasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    //Metodo do meu button de alteração para alterar uma venda ja registrada no banco de dados
    /*public void handleButtonAlterar() throws IOException, SQLException{
        //Pegando a venda que foi setada pelo usuario 
        Venda venda = tableViewVendas.getSelectionModel().getSelectedItem();
        //se a venda não for nulla
        if (venda != null){
        //
        boolean buttonConfirmarClicked = showFXMLAnchorPaneProcessosVendasDialog(venda);
        //setando autocommit como false para que nao insira nd ate eu chamar 
        //meu metodo connection.commit()
        connection.setAutoCommit(false);
        //setando conexão com minha instancia vendaDAO
        vendaDAO.setConnection(connection);
        ////setando conexão com minha instancia itemDeVendaDAO
        itemDeVendaDAO.setConnection(connection);
        //setando conexão com minha instancia produtoDao
        produtoDao.setConnection(connection);
        //Obtendo todos os itens da venda. no caso ai enquanto eu tiver itens de venda faça
        for(ItemDeVenda listItemDeVenda : venda.getItensDeVenda()){
        //listando esses produtos e jogando na minha intancia de produto
        Produto produto = listItemDeVenda.getProduto();
        //setando na minha instancia de produto a quantidade de produtos
        //produto.setQuantidade(produto.getQuantidade() -+ listItemDeVenda.getQuantidade());
        if(produto.getQuantidade()> listItemDeVenda.getQuantidade()){
            produto.setQuantidade(produto.getQuantidade() - listItemDeVenda.getQuantidade());
            //Fazendo a devolução dos meus da venda que vai ser cancelada para o banco de dados
            produtoDao.alterar(produto);
            //removendo todos os itens de venda
            vendaDAO.inserir(venda);
            connection.commit();
        }else{
            produto.setQuantidade(produto.getQuantidade() + listItemDeVenda.getQuantidade());
            //Fazendo a devolução dos meus da venda que vai ser cancelada para o banco de dados
            produtoDao.alterar(produto);
            itemDeVendaDAO.remover(listItemDeVenda);
            connection.commit();
        }
        }
        //fazendo connection.commit()
        //connection.commit();
        //atualizando minha tableViewVendas
        carregarTableViewVendas();
        }else{
            //Exibindo mensagem de erro se minha venda for nulla
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma venda na Tabela");
            alert.show();
        }
    
    }*/
    
    //Metodo do meu button de remoção para remover uma venda ja registrada no banco de dados  
    @FXML
    public void handleButtonRemover() throws IOException, SQLException {
        //Pegando a venda que foi setada pelo usuario 
        Venda venda = tableViewVendas.getSelectionModel().getSelectedItem();
        //se a venda não for nulla
        if (venda != null){
        /*setando autocommit como false para que nao remova nd ate eu chamar 
        meu metodo connection.commit()*/
        connection.setAutoCommit(false);
        //setando conexão com minha instancia vendaDAO
        vendaDAO.setConnection(connection);
        ////setando conexão com minha instancia itemDeVendaDAO
        itemDeVendaDAO.setConnection(connection);
        //setando conexão com minha instancia produtoDao
        produtoDao.setConnection(connection);
        /*Obtendo todos os itens da venda. no caso ai enquanto eu tiver itens de venda faça*/ 
        for(ItemDeVenda listItemDeVenda : venda.getItensDeVenda()){
        //listando esses produtos e jogando na minha intancia de produto
        Produto produto = listItemDeVenda.getProduto();
        //setando na minha instancia de produto a quantidade de produtos
        produto.setQuantidade(produto.getQuantidade() + listItemDeVenda.getQuantidade());
        /*Fazendo a devolução dos meus da venda que vai ser cancelada para o banco de dados*/
        produtoDao.alterar(produto);
        //removendo todos os itens de venda
        itemDeVendaDAO.remover(listItemDeVenda);
        }
        //deletando a venda completa
        vendaDAO.remover(venda);
        //fazendo connection.commit()
        connection.commit();
        //atualizando minha tableViewVendas
        carregarTableViewVendas();
        }else{
            //Exibindo mensagem de erro se minha venda for nulla
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma venda na Tabela");
            alert.show();
        }
    }
    
    //Metodo que  mostra a janela de inserção ou alteraçao dos dados
    public boolean showFXMLAnchorPaneProcessosVendasDialog(Venda venda) throws IOException {
        //criando um atributo do tipo FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        //Carregando a janela que tem os bunttons de confirmaçao de criaçao e remoção de produtos 
        loader.setLocation(FXMLAnchorPaneCadastrosClientesDialogController.class.getResource("/javafxmvc/view/FXMLAnchorPaneProcessosVendasDialog.fxml"));
        //criando um atributo para guardar em cache minha janela que esta carregada
        AnchorPane page = (AnchorPane) loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        //Setando um titulo para minha janela
        dialogStage.setTitle("Registro de Vendas");
        //instanciando minha cena com a minha janela carregada
        Scene scene = new Scene(page);
        //Setando no meu estagio a minha cena 
        dialogStage.setScene(scene);

        // Setando a venda no Controller.
        /*setando a venda que foi selecionado pelo usuario 
        ou setando uma nova instancia de uma venda*/
        FXMLAnchorPaneProcessosVendasDialogController controller = loader.getController();
        //setando minha dialogStage no meu controller
        controller.setDialogStage(dialogStage);
        controller.setVenda(venda);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();
        //retornando estado do meu button confirmar
        return controller.isbuttonConfirmarClicked();

    }
    
}