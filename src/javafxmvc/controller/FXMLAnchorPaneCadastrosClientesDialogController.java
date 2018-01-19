package javafxmvc.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.domain.Cliente;

public class FXMLAnchorPaneCadastrosClientesDialogController implements Initializable {
    //Atributos da minha interface grafica da tela de cadastro alteração e remoção de clientes
    @FXML
    private Label labelClienteNome;
    @FXML
    private Label labelClienteCPF;
    @FXML
    private Label labelClienteTelefone;
    @FXML
    private TextField textFieldClienteNome;
    @FXML
    private TextField textFieldClienteCPF;
    @FXML
    private TextField textFieldClienteTelefone;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;
    //Atributos que nao corresponde a componentes visuais
    
    //Atributo para estagio, no caso para verificar se minha tela esta setado 
    private Stage dialogStage;
    //Atributo para saber se o usuario cliclou no botao confirmar ou cancelar
    private boolean buttonConfirmarClicked = false;
    //Atributo para preenchimento de classe pra inserir no banco de dados
    private Cliente cliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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

    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        //recebendo dados setados pelo usuario para o banco de dados
        this.cliente = cliente;
        //pegando os dados das coluna e deixando pre selecionados nos campos de alteração
        this.textFieldClienteNome.setText(cliente.getNome());
        this.textFieldClienteCPF.setText(cliente.getCpf());
        this.textFieldClienteTelefone.setText(cliente.getTelefone());
    }
    
    //handleButtonConfirmar vai ser o metodo invocado quando o usuario clicar em confirmar
    @FXML
    public void handleButtonConfirmar() {

        if (validarEntradaDeDados()) {
            //Setando dados para o cliente de acordo com oque foi digitado pelo usuario
            cliente.setNome(textFieldClienteNome.getText());
            cliente.setCpf(textFieldClienteCPF.getText());
            cliente.setTelefone(textFieldClienteTelefone.getText());
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
        if (textFieldClienteNome.getText() == null || textFieldClienteNome.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }
        //validando campo de cpf se foi preenchido
        if (textFieldClienteCPF.getText() == null || textFieldClienteCPF.getText().length() == 0) {
            errorMessage += "CPF inválido!\n";
        }
        //validando campo de telefone se foi preenchido
        if (textFieldClienteTelefone.getText() == null || textFieldClienteTelefone.getText().length() == 0) {
            errorMessage += "Telefone inválido!\n";
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
