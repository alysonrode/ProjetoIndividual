package br.erp.rest;

import br.erp.bd.Conexao;
import br.erp.jdbc.JDBCUsuarioDAO;
import br.erp.jdbc.JDBCVendasDAO;
import br.erp.modelo.ChartData;
import br.erp.modelo.Produto;
import br.erp.modelo.Usuario;
import br.erp.modelo.Venda;
import com.google.gson.Gson;
import com.mysql.cj.x.protobuf.MysqlxExpr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
            produto.setQuantidade(Integer.parseInt(lista.get(i).replaceAll("quantidade:", "")));
            i++;
            produto.setId(Integer.parseInt(lista.get(i).replaceAll("idProduto:","")));

            produtos.add(produto);
        }
        return produtos;
    }
    @Path("/buscar")
    @GET
    @Consumes("Application/*")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscar(@QueryParam("busca") String busca, @QueryParam("minValue") int minValue, @QueryParam("maxValue") int maxValue){

        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCVendasDAO vendasDAO = new JDBCVendasDAO(conexao);

        List<Venda> vendas = vendasDAO.search(busca, minValue, maxValue);

        conec.fecharConexao();

        return this.buildResponse(vendas);

    }

    @Path("/selecionar")
    @POST
    @Consumes("Application/*")
    @Produces(MediaType.APPLICATION_JSON)
    public Response selecionar (String idParam){

        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCVendasDAO vendasDAO = new JDBCVendasDAO(conexao);

        List<Venda> vendas = vendasDAO.search(idParam, -1,-1);

        Venda venda = vendas.get(0);
        venda.setListProduts(vendasDAO.montaProdutos(venda.getIdVenda()));

        conec.fecharConexao();
        if(venda != null){
            return this.buildResponse(venda);
        }
        else{
            return this.buildErrorResponse("Erro na seleção da venda!");
        }
    }
    @Path("/update")
    @POST
    @Consumes("Application/*")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(String jsonParam){

        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCVendasDAO vendasDAO = new JDBCVendasDAO(conexao);

        ArrayList<String> stringList = prepareList(jsonParam);
        Venda venda = new Venda();

        venda.setIdVenda(Integer.parseInt(stringList.get(0)));
        stringList.remove(0);
        List<Produto> produtoList = new ArrayList<>();
        for (Produto produto : prepareObjects(stringList)){
            produto.setValor(vendasDAO.getValorProduto(produto));
            produtoList.add(produto);
        }
        venda.setListProduts(produtoList);
        venda.setValorTotal(calculaValorTotal(venda.getListProduts()));

        boolean ok = vendasDAO.updateVenda(venda);

        conec.fecharConexao();
        if(ok) {
            return this.buildResponse("Produto atualizado com sucesso!");
        }
        else{
            return this.buildErrorResponse("Erro na atualização do produto!");
        }
    }
    @Path("/deletar")
    @GET
    @Consumes("Application/*")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletar(@QueryParam("id") int id){
        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCVendasDAO vendasDAO = new JDBCVendasDAO(conexao);

        boolean ok = vendasDAO.deletar(id);
        conec.fecharConexao();

        if(ok){
            return this.buildResponse("Venda deletada com sucesso");
        }else{
            return this.buildErrorResponse("Erro ao deletar venda, contate o administrador!");
        }


    }
    @Path("/getDataToChat")
    @GET
    @Consumes("Application/*")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataToChat(@QueryParam("from") String from, @QueryParam("to") String to){
        if(from.equals("") && to.equals("")){
            from = getFirstDayOfMonth();
            to = getActualDate();
        }
        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCVendasDAO vendasDAO = new JDBCVendasDAO(conexao);
        ChartData chartData = vendasDAO.getDateToChat(from, to);
        conec.fecharConexao();

        return this.buildResponse(chartData);
    }

    @Path("/getDataToPDF")
    @GET
    @Consumes("Application/*")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataToPDF(@QueryParam("from") String from, @QueryParam("to") String to){
        if(from.equals("") && to.equals("")){
            from = getFirstDayOfMonth();
            to = getActualDate();
        }
        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCVendasDAO vendasDAO = new JDBCVendasDAO(conexao);
        List<Venda> vendas = vendasDAO.getDateToPDF(from, to);
        conec.fecharConexao();

        return this.buildResponse(vendas);
    }

    public String getFirstDayOfMonth(){
        Date date = new Date();
        date.setDate(1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
    public String getActualDate(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

}
