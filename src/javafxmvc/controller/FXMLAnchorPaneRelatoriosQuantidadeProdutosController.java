package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;
import javafxmvc.model.domain.Produto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class FXMLAnchorPaneRelatoriosQuantidadeProdutosController implements Initializable {

    //Atributos da minha interface grafica
    @FXML
    private TableView<Produto> tableViewProdutos;
    @FXML
    //private TableColumn<Produto, Integer> tableColumnCodigo;
    private TableColumn<Produto, String> tableColumnCodigo;
    @FXML
    private  TableColumn<Produto, String> tableColumnNome;
    @FXML
    private TableColumn<Produto, Double> tableColumnPreco;
    @FXML
    private TableColumn<Produto, Integer> tableColumnQuantidade;
    @FXML
    private TableColumn<Produto, Categoria> tableColumnCategoria;
    //
    @FXML
    private Button buttonImprimir;
    //
    private List <Produto> listProdutos;
    private ObservableList <Produto> observableListProdutos;  
    //Atributos para malipulação do banco de dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    
    //Metodo de inicialização
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       //Setando conexão do banco de dados na minha instancia da classe de produtos
       produtoDAO.setConnection(connection);
       //Chamando meu metodo para carregar minha tableview de produtos
       carregarTableViewProdutos();
       
    }    
    
    //Metodo para carregar minha table view de clientes
    public void carregarTableViewProdutos() {
        //Configurando as colunas
        //tableColumnCodigo vai exibir o codigo do produto
        tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<>("cdProduto"));
        //tableColumnNome vai exibir o nome do produto
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        //tableColumnPreco vai exibir preço do produto
        tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        //tableColumnQuantidade vai exibir quantidade do produto        
        tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        //tableColumnCategoria vai exibir categoria do produto        
        tableColumnCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));        
        //a minha ListProdutos vai receber o meu objeto produtoDAO com a função listar();
        listProdutos = produtoDAO.listar();
        //meu observableListProdutos esta recebendo minha listClientes
        observableListProdutos = FXCollections.observableArrayList(listProdutos);
        /*passando meu observableListProdutos para meu tableViewProdutos ou seja
        assim ele seta todos os meus produtos na minha tela tableViewProdutos*/
        tableViewProdutos.setItems(observableListProdutos);
    }
    
    /*Metodo do meu button Impromir relatorio esse metodo chama meu relatorio do
    jasperStudio em formatod de PDF de produtos em estoque */ 
    public void  handleImprimir() throws JRException{
        /*Passando a URL: passando local onde meu relatorio esta*/
        URL url = getClass().getResource("/javafxmvc/Relatorios/FXMVCRelatorioProduto.jasper");
        /*Objeto do tipo JasperReport: Serve para carregar essa url*/
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);
        
        
        /*JasperPrint: Trabalha com  a conexão do banco de dados
        no caso vão obter os valores exibitos no jasperReport*/
        
        /*o segundo parametro passado null: e porque no meu select nao estou trabalhando com
        filtro de dados no meu select*/
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);
                
        /*JasperViewer: serve para configurar a maneira de vizualização referente
        ao meu objeto jasperReport que foi definido acima*/
        
        /*O false que e passado no segundo parametro serve: não deixa fechar a aplicação principal,
        simplificando quando usuario fechar aplicação ele vai continuar sendo exibida*/
        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
        /*tornando visivel o relatorio gerado*/
        jasperViewer.setVisible(true);
    
    }
    
}
