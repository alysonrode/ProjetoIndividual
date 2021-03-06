package br.erp.rest;

import br.erp.bd.Conexao;
import br.erp.jdbc.JDBCProdutoDAO;
import br.erp.jdbc.JDBCUsuarioDAO;
import br.erp.modelo.Produto;
import br.erp.modelo.Usuario;
import com.google.gson.Gson;
import com.mysql.cj.x.protobuf.MysqlxExpr;

import javax.persistence.criteria.CriteriaBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("produtos")
public class ProdutosREST extends UtilRest {
    @Path("/cadastrar")
    @POST
    @Consumes("*/*")
    @Produces("*/*")
    public Response cadastrarProduto(String produtoParam){
        Produto produto = new Gson().fromJson(produtoParam, Produto.class);
        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCProdutoDAO jdbcProdutoDAO = new JDBCProdutoDAO(conexao);
        boolean integridade = jdbcProdutoDAO.verificaIntegridade(produto);
        if(!integridade){
            return this.buildResponse("Produto já existe!");
        }
        boolean cadastrado = jdbcProdutoDAO.cadastrar(produto);
        if(cadastrado)
            return this.buildResponse("Produto cadastrado com sucesso!");
        else
            return this.buildErrorResponse("Erro no cadastro do produto");
    }
    @Path("/buscaid")
    @POST
    @Produces("*/*")
    public Response buscaId(){
        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCProdutoDAO jdbcProdutoDAO = new JDBCProdutoDAO(conexao);
        int id = jdbcProdutoDAO.buscaid();
        id++;
        conec.fecharConexao();
        return this.buildResponse(id);
    }
    @Path("/buscar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("*/*")
    public Response buscar(@QueryParam("busca") String busca){

        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCProdutoDAO jdbcUsuarioDAO = new JDBCProdutoDAO(conexao);
        List<Produto> produtoList;

        produtoList = jdbcUsuarioDAO.buscar(busca);
        conec.fecharConexao();
        return this.buildResponse(produtoList);


    }
    @Path("/getProductById")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("Application/*")
    public Response getProductById(@QueryParam("id") int id){

        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCProdutoDAO jdbcUsuarioDAO = new JDBCProdutoDAO(conexao);

        Produto product = jdbcUsuarioDAO.getProductById(id);

        conec.fecharConexao();

        return this.buildResponse(product);
    }
    @Path("/pegaQuantidade")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("Application/*")
    public Response pegaQuantidade(String listaIdsParam){

        List<Integer> listIds = preparaLista(listaIdsParam);
        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCProdutoDAO jdbcProdutoDAO = new JDBCProdutoDAO(conexao);

        List<Integer> quantidades = jdbcProdutoDAO.pegaQuantidades(listIds);
        conec.fecharConexao();
        return this.buildResponse(quantidades);
    }
    public List<Integer> preparaLista(String listJSON){

        listJSON = listJSON.replaceAll("\\[", "");
        listJSON = listJSON.replaceAll("\\]", "");
        List<Integer> listIds = new ArrayList<>();
        List<String> stringList = new ArrayList<String>();
        String[] strings = listJSON.split(",");

        stringList = Arrays.asList(strings);
        for (String s : stringList) {
            listIds.add(Integer.parseInt(s.replaceAll("\"","")));
        }

        return listIds;
    }
    @Path("/updateProduct")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("Application/*")
    public Response updateProduct(String produtoParam){

        Produto produto = new Gson().fromJson(produtoParam, Produto.class);
        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCProdutoDAO jdbcProdutoDAO = new JDBCProdutoDAO(conexao);

        boolean integridade = jdbcProdutoDAO.verificaIntegridade(produto);

        if(!integridade){
            conec.fecharConexao();
            return this.buildErrorResponse("Já existe um produto igual.");
        }

        boolean atualizado = jdbcProdutoDAO.updateProduct(produto);
        conec.fecharConexao();
        if(atualizado){
            return this.buildResponse("Produto atualizado!");
        }else{
            return this.buildErrorResponse("Erro ao atulizar produto!");
        }
    }
    @Path("/deleteProduct")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("Application/*")
    public Response deleteProcuct(@QueryParam("id") int id){

        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCProdutoDAO jdbcProdutoDAO = new JDBCProdutoDAO(conexao);

        boolean integridade =  jdbcProdutoDAO.integridade(id);
        if(integridade){
            boolean deletado = jdbcProdutoDAO.deleteProduct(id);
            if (deletado){
                return this.buildResponse("Produto deletado com sucesso.");
            }
            else{
                return this.buildErrorResponse("Erro ao deletar produto!");
            }
        }
        else{
            return this.buildErrorResponse("O produto está vinculado à uma ou mais vendas!");
        }
    }
}
