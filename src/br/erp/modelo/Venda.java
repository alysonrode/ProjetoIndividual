package br.erp.modelo;

import java.util.ArrayList;
import java.util.List;

public class Venda {

    private int idVenda;

    private double valorTotal;

    private String dataVenda;

    private List<Produto> listProduts;

    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(String dataVenda) {
        this.dataVenda = dataVenda;
    }
    public List<Produto> getListProduts() {
        return listProduts;
    }

    public void setListProduts(List<Produto> listProduts) {
        this.listProduts = listProduts;
    }
}
