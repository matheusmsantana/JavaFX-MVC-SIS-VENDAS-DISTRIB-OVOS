
package javafxmvc.model.database;

import java.sql.Connection;
/*Na interface é define um contrato dizendo que qualquer
classe que a implementar vai ter que escrever os métodos definidos.*/

/*Quando você tem uma classe que implementa a interface, 
você é obrigado a escrever o método que está definido na
interface dentro da classe, mesmo que dentro deste método não haja código nenhum.
Se você fizer um extends não é obrigado a escrever o método na 
classe, a não ser que a superclasse seja abstrata.*/

/*metodo para fazer conexão e desconexão com banco de dados*/
public interface Database {
    
    public Connection conectar();
    public void desconectar(Connection conn);
}
