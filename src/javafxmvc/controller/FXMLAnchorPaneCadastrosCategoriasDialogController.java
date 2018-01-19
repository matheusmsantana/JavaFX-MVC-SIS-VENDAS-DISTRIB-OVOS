package javafxmvc.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.domain.Categoria;

public class FXMLAnchorPaneCadastrosCategoriasDialogController implements Initializable {
    //Atributos da minha interface grafica da tela de cadastro alteração e remoção de clientes
    @FXML
    private TextField textFieldCategoriaNome;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;
    
    //Atributo para estagio, no caso para verificar se minha tela esta setado 
    private Stage dialogStage;
    //Atributo para saber se o usuario cliclou no botao confirmar ou cancelar
    private boolean buttonConfirmarClicked = false;
    //Atributo para preenchimento de classe pra inserir no banco de dados
    private Categoria categoria;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    //Metodos Get e Set
    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    public Categoria getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        //recebendo dados setados pelo usuario para o banco de dados
        this.categoria = categoria;
        //pegando os dados das coluna e deixando pre selecionados nos campos de alteração
        this.textFieldCategoriaNome.setText(categoria.getDescricao());
    }
    
    //handleButtonConfirmar vai ser o metodo invocado quando o usuario clicar em confirmar
    @FXML
    public void handleButtonConfirmar() {

        if (validarEntradaDeDados()) {
            //Setando dados para o cliente de acordo com oque foi digitado pelo usuario
            categoria.setDescricao(textFieldCategoriaNome.getText());
            //Passando para o meu atributo buttonConfirmarClicked true
            buttonConfirmarClicked = true;
            //Fecho a janela
            dialogStage.close();
        }

    }
    
    //handleButtonCancelar vai ser o metodo invocado quando o usuario clicar em cancelar
    @FXML
    public void handleButtonCancelar() {
        //fechando a janela sem setar nada
        dialogStage.close();
    }
    
    
        //Validar entrada de dados para o cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        //validando campo de nome se foi preenchido
        if (textFieldCategoriaNome.getText() == null || textFieldCategoriaNome.getText().length() == 0) {
            errorMessage += "Nome da categoria inválido!\n";
        }
        
        //Caso minha variavel de validação nao tenha caracteres return true se nao return false
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Mostrando a mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor, corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
    
}
