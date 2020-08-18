package br.erp.rest;

import br.erp.bd.Conexao;
import br.erp.jdbc.JDBCUsuarioDAO;
import br.erp.modelo.Usuario;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Path("usuarios")

public class UsuariosREST extends UtilRest {
    @POST
    @Path("/cadastrar")
    @Consumes("application/*")
    public Response cadastrarUsuario(String user){
        Usuario usuario = new Gson().fromJson(user, Usuario.class);

        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCUsuarioDAO usuarioDAO = new JDBCUsuarioDAO(conexao);
        boolean isOk = usuarioDAO.cadastrar(usuario);
        if(isOk){
            return this.buildResponse("Usuários cadastrado com sucesso!");
        }
        else{
            return this.buildResponse("Erro no cadastro do usuário.");
        }


    }
    @GET
    @Path("/buscar")
    @Consumes("application/*")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarUsuario(@QueryParam("filtro") String filtro, @QueryParam("onlyActive") boolean onlyActive){

        List<Usuario> listUsers = new ArrayList<Usuario>();
        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCUsuarioDAO jdbcUsuarioDAO = new JDBCUsuarioDAO(conexao);
        listUsers = jdbcUsuarioDAO.buscar(filtro, onlyActive);
        conec.fecharConexao();

        return this.buildResponse(listUsers);

    }
    @POST
    @Path("/buscaid")
    public Response buscaIdUsuário(){

        int id;
        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCUsuarioDAO jdbcUsuarioDAO = new JDBCUsuarioDAO(conexao);
        id = jdbcUsuarioDAO.buscaid();
        conec.fecharConexao();
        id = id + 1;
        return this.buildResponse(id);
    }
    @GET
    @Path("/buscaPorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscaPorId(@QueryParam("id") int id){

        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCUsuarioDAO jdbcUsuarioDAO = new JDBCUsuarioDAO(conexao);
        Usuario user = jdbcUsuarioDAO.buscarPorId(id);
        conec.fecharConexao();
        return this.buildResponse(user);

    }
    @POST
    @Path("/atualizaUsuario")
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizaUsuario(String user){
        Usuario usuario = new Gson().fromJson(user, Usuario.class);
        System.out.println(usuario.getEmail());
        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCUsuarioDAO jdbcUsuarioDAO = new JDBCUsuarioDAO(conexao);
        boolean ok = jdbcUsuarioDAO.atualizaUsuario(usuario);
        if (ok){
            return this.buildResponse("Usuário atualizado com sucesso!");
        }
        else{
            return this.buildErrorResponse("Erro na atualização de usuário, contate o administrador!");
        }
    }
    @GET
    @Path("/deletaUsuario")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletaUsuarios(@QueryParam("id") int id){
        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCUsuarioDAO jdbcUsuarioDAO = new JDBCUsuarioDAO(conexao);
        boolean ok = jdbcUsuarioDAO.deletaUsuario(id);
        if (ok){
            return this.buildResponse("Usuário inativado com sucesso!");
        }
        else{
            return this.buildErrorResponse("Erro em inativar o usuário, contate o administrador!");
        }
    }
    @GET
    @Path("/ativaUsuario")
    @Produces(MediaType.APPLICATION_JSON)
    public Response ativaUsuarios(@QueryParam("id") int id){
        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCUsuarioDAO jdbcUsuarioDAO = new JDBCUsuarioDAO(conexao);
        boolean ok = jdbcUsuarioDAO.ativaUsuario(id);
        if (ok){
            return this.buildResponse("Usuário ativado com sucesso!");
        }
        else{
            return this.buildErrorResponse("Erro em ativar o usuário, contate o administrador!");
        }
    }
    public Usuario getUserForSession(String login){

        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCUsuarioDAO jdbcUsuarioDAO = new JDBCUsuarioDAO(conexao);

        Usuario usuario = jdbcUsuarioDAO.getUserForSession(login);
        return usuario;
    }
    @Path("/CheckEmail")
    @POST
    @Consumes("Application/*")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateEmail(String email){

        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCUsuarioDAO jdbcUsuarioDAO = new JDBCUsuarioDAO(conexao);

        boolean testRegex = email.matches("\\S+@\\S+\\.\\S+");

        if(!testRegex){
            return this.buildResponse("Formato de e-mail inválido.");
        }

        boolean ok = jdbcUsuarioDAO.checkEmail(email);
        if (ok){
            return this.buildResponse("Ok!");
        }
        else{
            return this.buildResponse("E-mail já existente!");
        }
    }
}
