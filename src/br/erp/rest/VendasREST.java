package br.erp.rest;

import br.erp.modelo.Produto;
import br.erp.modelo.Venda;
import com.google.gson.Gson;
import com.mysql.cj.x.protobuf.MysqlxExpr;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.spi.AbstractResourceBundleProvider;

@Path("vendas")
public class VendasREST extends UtilRest {
    @Path("/cadastrar")
    @POST
    @Produces("*/*")
    public void cadastrarVenda(String JSONParam){

        ArrayList<String> lista = prepareList(JSONParam);
        List<Produto> produtos = prepareObjects(lista);
    }
    
    public ArrayList<String> prepareList(String JSONParam){

        //REPLACES
        JSONParam = JSONParam.replaceAll("\\[", "");
        JSONParam = JSONParam.replaceAll("\\{", "");
        JSONParam = JSONParam.replaceAll("]", "");
        JSONParam = JSONParam.replaceAll("}", "");
        JSONParam = JSONParam.replaceAll("\"", "");

        //SPLIT NA STRING
        String[] str = JSONParam.split(",");

        //PREPARO DA LISTA
        List<String> stringList = new ArrayList<String>();
        stringList = Arrays.asList(str);

        //RETORNO
        return new ArrayList<String>(stringList);
    }

    public List<Produto> prepareObjects(ArrayList<String> lista){

        List<Produto> produtos = new ArrayList<Produto>();

        for(int i = 0; i <lista.size(); i++){
            Produto produto = new Produto();

            produto.setNome(lista.get(i).replace("produto:", ""));
            i++;
            produto.setQuantidade(Integer.parseInt(lista.get(i).replaceAll("quantidade:", "")));

            produtos.add(produto);
        }
        return produtos;
    }
    
    
}
