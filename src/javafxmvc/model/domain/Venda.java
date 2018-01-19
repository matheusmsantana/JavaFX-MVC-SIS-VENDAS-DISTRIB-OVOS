package javafxmvc.model.domain;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class Venda implements Serializable {

    private int cdVenda;
    private LocalDate data;
    private double valor;
    private boolean pago;
    private List<ItemDeVenda> itensDeVenda;
    private Cliente cliente;
    //
    private LocalDate data1;
    private LocalDate data2;
    //
    private Date data1Text;
    private Date data2Text;
    
    public Venda() {
    }

    /*public Venda(int cdvenda, LocalDate data, double valor, boolean pago) {
        this.cdVenda = cdvenda;
        this.data = data;
        this.valor = valor;
        this.pago = pago;
    }*/
    
        public Venda(int cdvenda, LocalDate data, double valor, boolean pago, LocalDate data1, LocalDate data2, Date data1Text, Date data2Text) {
        this.cdVenda = cdvenda;
        this.data = data;
        this.valor = valor;
        this.pago = pago;
        this.data1 = data1;
        this.data2 = data2;
        this.data1Text = data1Text;
        this.data2Text = data2Text;
    }

    public int getCdVenda() {
        return cdVenda;
    }

    public void setCdVenda(int cdVenda) {
        this.cdVenda = cdVenda;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
    
    /*
    public LocalDate getData1() {
        return data1;
    }

    public void setData1(LocalDate data1) {
        this.data1 = data1;
    }
    //
    public LocalDate getData2() {
        return data2;
    }

    public void setData2(LocalDate data2) {
        this.data2 = data1;
    }
    */
    
    public Date getData1() {
        return data1Text;
    }

    public void setData1(Date data1Text) {
        this.data1Text = data1Text;
    }
    //
    public Date getData2() {
        return data2Text;
    }

    public void setData2(Date data1Text) {
        this.data2Text = data1Text;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public boolean getPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public List<ItemDeVenda> getItensDeVenda() {
        return itensDeVenda;
    }

    public void setItensDeVenda(List<ItemDeVenda> itensDeVenda) {
        this.itensDeVenda = itensDeVenda;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
}
