package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafxmvc.model.dao.VendaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;

public class FXMLAnchorPaneGraficosVendasPorMesController implements Initializable {
    
    //Atributos da interface grafica 
    @FXML
    private BarChart<String, Integer> barChart;
    @FXML 
    private CategoryAxis categoryAxis;
    @FXML
    private NumberAxis numberAxis; 
    
    /*observableListMeses serve para definir esses meses por padrao*/
    private ObservableList<String> observableListMeses = FXCollections.observableArrayList();
    
    /*Atributos para conectar no banco de dados*/
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    /*instanciando a classe VendaDAO para o objeto VendaDao*/
    private final VendaDAO VendaDao = new VendaDAO();
    
    //Metodo de inicialização
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    //Obtem an  array com nome  dos meses em ingles
    String[] arrayMeses = {"Jan","Fev","Mar","Abr","Mai","Jun","Jul","Ago","Set","Out","Nov","Dez"};
    //Converte o array em uma lista  e adiciona na ObservableListMeses
    observableListMeses.addAll(arrayMeses);
    /*Assosia os nomes do mês como categorias para o eixo horizontal, no caso
    meu atributo grafico categoryAxis*/
    categoryAxis.setCategories(observableListMeses);
    //setando conexão na minha classe VendaDao, para poder trazer minhas vendas
    VendaDao.setConnection(connection);
    //Listando minha quantidade de vendas por mês atravez do MAP
    //dados são do tipo Map  chave do tipo "INTEGER" e o valor é um ARRAYLIST
    //CHAVE E O ANO E O VALOR E OS MESES COM SUAS QUANTIDADES DE VENDA
    Map<Integer, ArrayList> dados = VendaDao.listarQuantidadeVendasPorMes();
    
    /*A sintax que esta no for e uma sintax que vai varrer todo o MAP
    pegando todos os dados do  map dados.entrySet()
    e vou atribuir o primeiro dado em dadosItem vou fazer isto
    para tados os elementos armazenados do meu map*/
    
    //EXEMPLIFICANDO enquanto eu ainda tiver anos com vendas ele fazer o loop 
    for(Map.Entry<Integer, ArrayList> dadosItem : dados.entrySet()){
        //criando uma nova serie de dados para cada elemento do map
        //exemplo minha primeira venda foi em 2017 ele cria uma serie para 2017
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        //setando minha serie de dados com nome do ano que esta sendo percorrido
        series.setName(dadosItem.getKey().toString());
        //for para varrer todos os messes da minha serie no caso 2017
        for(int i=0;i<dadosItem.getValue().size();i = i+2){
            //incluindo os dados na minha serie de dados
            String mes;
            Integer quantidade;
            mes = retornaNomeMes ((int) dadosItem.getValue().get(i));
            quantidade = (Integer) dadosItem.getValue().get(i+1);
            series.getData().add(new XYChart.Data<>(mes,quantidade));
            }
            /*Adicionando meses e vendas da minha serie autual que e 2017
            no meu elemento grafico barChart*/
            barChart.getData().add(series);
            
        }
    
    }
    
    //metodo para retornar o nome do mês ao invez de retornar o num do mes
    public String retornaNomeMes(int mes){
        switch (mes){
            case(1):
                    return "Jan";
            case(2):
                    return "Fev";
            case(3):
                    return "Mar";
            case(4):
                    return "Abr";
            case(5):
                    return "Mai";
            case(6):
                    return "Jun";
            case(7):
                    return "Jul";
            case(8):
                    return "Agos";
            case(9):
                    return "Set";
            case(10):
                    return "Out";
            case(11):
                    return "Nov";
            case(12):
                    return "Dez";
        }
        return "Caralho";
    }
    
}
