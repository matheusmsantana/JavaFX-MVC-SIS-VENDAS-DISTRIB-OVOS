package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class FXMLVBoxMainController implements Initializable {
    //Atributos da  minha interface grafica layout
    @FXML
    private MenuItem menuItemCadastrosClientes;
    @FXML
    private MenuItem menuItemCadastrosProdutos;
    @FXML
    private MenuItem menuItemProcessosVendas;
    @FXML
    private MenuItem menuItemGraficosVendasPorMes;
    @FXML
    private MenuItem menuItemRelatoriosQuantidadeProdutosEstoque;
    @FXML
    private MenuItem menuItemRelatoriosVendaEntreDatas;
    @FXML
    private AnchorPane anchorPane;
    
    //Classe de inicialização
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }   
    
    //Metodo do meu menu que abre minha tela de cadastro de categorias dentro da minha tela principal
    @FXML
    public void handleMenuItemCadastrosCategorias() throws IOException {
        //Carregando a tela AnchorPane, no caso instanciando AnchorPane
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosCategorias.fxml"));
        //setando a instancia a na minha tela anchorPane
        anchorPane.getChildren().setAll(a);
    }
    
    //Metodo do meu menu que abre minha tela de cadastro de clietes dentro da minha tela principal
    @FXML
    public void handleMenuItemCadastrosClientes() throws IOException {
        //Carregando a tela AnchorPane, no caso instanciando AnchorPane
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosClientes.fxml"));
        //setando a instancia a na minha tela anchorPane
        anchorPane.getChildren().setAll(a);
    }
    
    //Metodo do meu menu que abre minha tela de cadastro de produtos dentro da minha tela principal
    @FXML
    public void handleMenuItemCadastrosProdutos() throws IOException {
        //Carregando a tela AnchorPane, no caso instanciando AnchorPane
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosProdutos.fxml"));
        //setando a instancia a na minha tela anchorPane
        anchorPane.getChildren().setAll(a);
    }
    
    
    //Evento do meu menu que abre minha tela de vendas dentro da minha tela principal
    @FXML
    public void handleMenuItemProcessosVendas () throws IOException {
    //Carregando a tela AnchorPane, no caso instanciando AnchorPane
    AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneProcessosVendas.fxml"));
    //setando a instancia a na minha tela anchorPane
    anchorPane.getChildren().setAll(a);
    
    }
    
    //Metodo do meu menu que abre minha tela de graficos de vendas dentro da minha tela principal
    @FXML
    public void handleMenuItemGraficosVendasPorMes () throws IOException {
    //Carregando a tela AnchorPane, no caso instanciando AnchorPane
    AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneGraficosVendasPorMes.fxml"));
    //setando a instancia a na minha tela anchorPane
    anchorPane.getChildren().setAll(a);
    
    }
    
    //Metodo do meu menu que abre minha tela de controle de estoque dentro da minha tela principal
    @FXML
    public void handleMenuItemRelatoriosAQuantidadeDeProdutos () throws IOException {
    //Carregando a tela AnchorPane, no caso instanciando AnchorPane
    AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneRelatoriosQuantidadeProdutos.fxml"));
    //setando a instancia a na minha tela anchorPane
    anchorPane.getChildren().setAll(a);
    
    }
    
    //
    //Metodo do meu menu que abre minha tela de controle de vendas entre datas
    @FXML
    public void handleMenuItemRelatoriosVendaEntreDatas () throws IOException {
    //Carregando a tela AnchorPane, no caso instanciando AnchorPane
    AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneRelatoriosTotalVendas.fxml"));
    //setando a instancia a na minha tela anchorPane
    anchorPane.getChildren().setAll(a);
    
    }
    
}
