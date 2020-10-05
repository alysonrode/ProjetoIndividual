package br.erp.rest;

import br.erp.bd.Conexao;
import br.erp.jdbc.JDBCUsuarioDAO;
import br.erp.jdbc.JDBCVendasDAO;
import br.erp.modelo.Produto;
import br.erp.modelo.Usuario;
import br.erp.modelo.Venda;
import com.google.gson.Gson;
import com.mysql.cj.x.protobuf.MysqlxExpr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.spi.AbstractResourceBundleProvider;

@Path("vendas")
public class VendasREST extends UtilRest {

    @Context
    HttpServletRequest request;

    @Path("/cadastrar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response cadastrarVenda(String JSONParam){

        ArrayList<String> lista = prepareList(JSONParam);
        List<Produto> produtos = prepareObjects(lista);

        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCVendasDAO vendasDAO = new JDBCVendasDAO(conexao);

        for(Produto produto : produtos){
            produto.setValor(vendasDAO.getValorProduto(produto));
        }

        JDBCUsuarioDAO jdbcUsuarioDAO = new JDBCUsuarioDAO(conexao);
        HttpSession session = ((HttpServletRequest) request).getSession();
        Usuario user = jdbcUsuarioDAO.buscarPorId(Integer.parseInt(session.getAttribute("id").toString().replaceAll("\"","")));

        boolean ok = vendasDAO.cadastraVenda(preparaVenda(produtos, user));

        if(ok){
            return this.buildResponse("Venda realizada com sucesso!");
        }
        else{
            return this.buildErrorResponse("Erro no cadastro de venda");
        }
    }

    public Venda preparaVenda(List<Produto> produtos, Usuario user){

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dataAtual = simpleDateFormat.format(date);

        Venda venda = new Venda();
        venda.setListProduts(produtos);
        venda.setValorTotal(calculaValorTotal(produtos));
        venda.setDataVenda(dataAtual);
        venda.setUsuario(user);

        return venda;


    }
    public double calculaValorTotal(List<Produto> produtos){

        double valorTotal = 0;
        for(Produto produto : produtos){
            valorTotal = valorTotal + (produto.getQuantidade() * produto.getValor());
        }
        return valorTotal;

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
            i++;
            produto.setId(Integer.parseInt(lista.get(i).replaceAll("idProduto:","")));

            produtos.add(produto);
        }
        return produtos;
    }
    
    
}
