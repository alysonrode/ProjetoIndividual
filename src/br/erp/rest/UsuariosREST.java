package br.erp.rest;

import br.erp.bd.Conexao;
import br.erp.jdbc.JDBCUsuarioDAO;
import br.erp.modelo.Usuario;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarUsuario(@QueryParam("valorBusca") String busca){

        List<Usuario> listUsers = new ArrayList<Usuario>();
        Conexao conec = new Conexao();
        Connection conexao = conec.abrirConexao();
        JDBCUsuarioDAO jdbcUsuarioDAO = new JDBCUsuarioDAO(conexao);
        jdbcUsuarioDAO.buscar(busca);
        conec.fecharConexao();

        return this.buildResponse(listUsers);

    }
}
