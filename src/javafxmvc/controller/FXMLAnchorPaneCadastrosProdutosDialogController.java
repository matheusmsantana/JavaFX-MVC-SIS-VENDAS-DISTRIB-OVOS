package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.dao.CategoriaDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;
import javafxmvc.model.domain.Produto;

public class FXMLAnchorPaneCadastrosProdutosDialogController implements Initializable {

    //Atributos da minha interface grafica da tela de cadastro alteração e remoção de produtos
    @FXML
    private ComboBox comboBoxProdutoCategoria;
    @FXML
    private TextField textFieldProdutoCodigo;
    @FXML
    private TextField textFieldProdutoNome;
    @FXML
    private TextField textFieldProdutoQuantidade;
    @FXML
    private TextField textFieldProdutoPreco;
    @FXML
    private Button buttonCancelar;
    @FXML
    private Button buttonConfirmar;
    /*Atributos de lista para listar Categoria dos produtos*/
    private List<Categoria>  listCategoriaProduto;
    /*Atributos de lista para listar listCategoriaProduto*/
    private ObservableList<Categoria> observableListCategoriaProduto;
   
    //Atributos para manipulação do banco de dados
    //Fazendo acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    /*Esse metodo esta indo ao banco de dados e se conectando com meu banco de dados*/
    private final Connection connection = database.conectar();
    /*Instanciando a classe ClienteDAO que contem metodos de sql*/
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    /*Instanciando a classe ClienteDAO que contem metodos de sql*/
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    
    //Atributo para estagio, no caso para verificar se minha tela esta setado 
    private Stage dialogStage;
    //Atributo para saber se o usuario cliclou no botao confirmar ou cancelar
    private boolean buttonConfirmarClicked = false;
    //Atributo para preenchimento de classe pra inserir no banco de dados
    private Produto produto;
    
    //Metodo de inicialização
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*setando connection que foi aberta no meu atributo produtoDAO*/
        produtoDAO.setConnection(connection);
        /*setando connection que foi aberta no meu atributo categoriaDAO*/
        categoriaDAO.setConnection(connection);
        /*Chamando meu metodo para popular comboBoxProdutoCategoria*/
        carregarComboBoxCategoriaProdutos();
    }    
    
       //Metodo para carregar meu comboBox de clientes no caso estou populando ele
       public void carregarComboBoxCategoriaProdutos() {
        /*Minha listCategoriaProduto esta recebendo uma isntancia da minha classe categoriaDAO
        com metodo listar*/    
        listCategoriaProduto = categoriaDAO.listar();
        //Meu observableListClientes esta recebendo minha list
        observableListCategoriaProduto = FXCollections.observableArrayList(listCategoriaProduto);
        //Setando meu observableListCategoriaProduto no meu comboBoxProdutoCategoria
        comboBoxProdutoCategoria.setItems(observableListCategoriaProduto);
    }
       
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
    
    public Produto getProduto(){
        return produto;
    }
    
    public void setProduto(Produto produto){
        //recebendo dados setados pelo usuario para o banco de dados
        this.produto = produto;  
        //pegando os dados das coluna e deixando pre selecionados nos campos de alteração
        this.comboBoxProdutoCategoria.setItems(observableListCategoriaProduto);
        //this.textFieldProdutoCodigo.setText(String.format("%d", produto.getCdProduto()));
        this.textFieldProdutoCodigo.setText(produto.getCdProduto());
        this.textFieldProdutoNome.setText(produto.getNome());
        this.textFieldProdutoQuantidade.setText(String.format("%d", produto.getQuantidade()));
        this.textFieldProdutoPreco.setText(String.format("%.2f", produto.getPreco()));
    }
    
    
     /*handleButtonConfirmar: Metodo acionado ao clicar no button confirmar*/
     //Quando o usuario clicar no button confirmar ele vai chamar esta função
    @FXML
    public void handleButtonConfirmar(){
        //se a minha validação de cadastro de produto for true
        if(validarEntradaDeDados()){
            //eu seto minha meu produto com as outras informações que ainda faltam
            //setando categoria do produto de acordo com oque foi escolhido
            produto.setCategoria((Categoria) comboBoxProdutoCategoria.getSelectionModel().getSelectedItem());
            //setando codigo do produto de acordo com oque foi escolhido
            produto.setCdProduto(textFieldProdutoCodigo.getText());
            //produto.setCdProduto(Long.parseLong(textFieldProdutoCodigo.getText()));
            //Setando nome do produto de acordo com oque foi digitado pelo usuario
            produto.setNome(textFieldProdutoNome.getText());
            //Convertendo textFieldProdutoQuantidade para Int e depois setando na minha instancia
            produto.setQuantidade(Integer.parseInt(textFieldProdutoQuantidade.getText()));
            //Convertendo textFieldProdutoPreco para DOUBLE e depois setando na minha instancia
            //produto.setPreco(Double.parseDouble(textFieldProdutoPreco.getText()));
            produto.setPreco(Double.parseDouble(textFieldProdutoPreco.getText().replace(",",".")));
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
    
    //Metodo para validar se os campos foram digitados
     @FXML
    private boolean validarEntradaDeDados(){
        String errorMessage = "";
            if (comboBoxProdutoCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Categoria Invalida!\n";
        } 
            if(textFieldProdutoCodigo == null){
               errorMessage += "Código do produto invalido! \n";
        }
            
            if(textFieldProdutoNome == null){
               errorMessage += "Nome do produto invalido! \n";
        }   
           
            if(textFieldProdutoQuantidade == null){
               errorMessage += "Quantidade de produto invalida! \n";
        }
           
            if(textFieldProdutoPreco == null){
               errorMessage += "Preço do produto invalido! \n";
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