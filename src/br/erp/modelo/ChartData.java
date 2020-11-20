package br.erp.modelo;

import java.sql.Date;
import java.util.List;

public class ChartData {

    private List<Integer> quantidades;
    private List<String> datas;

    public List<Integer> getQuantidades() {
        return quantidades;
    }

    public void setQuantidades(List<Integer> quantidades) {
        this.quantidades = quantidades;
    }

    public List<String> getDatas() {
        return datas;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

}
