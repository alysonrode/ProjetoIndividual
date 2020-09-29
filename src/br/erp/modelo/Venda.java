package br.erp.modelo;

import java.util.ArrayList;
import java.util.List;

public class Venda {
    private ArrayList<Produto> listProduts;

    public ArrayList<Produto> getListProduts() {
        return listProduts;
    }

    public void setListProduts(ArrayList<Produto> listProduts) {
        this.listProduts = listProduts;
    }
}
