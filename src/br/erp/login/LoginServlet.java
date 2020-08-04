package br.erp.login;

import br.erp.bd.Conexao;
import br.erp.jdbc.JDBCUsuarioDAO;
import br.erp.modelo.Usuario;
import br.erp.rest.UsuariosREST;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try{

            HttpSession session = ((HttpServletRequest) request).getSession();
            session.setMaxInactiveInterval(300);
            String usuario = null;
            if (session != null ){
                usuario = (String) session.getAttribute("login");
            }
                PasswordUtils pu = new PasswordUtils();
                String senha = pu.convertPassword(request.getParameter("senha"));
                UsuariosREST usuariosREST = new UsuariosREST();
                Usuario objUsuario = usuariosREST.getUserForSession(request.getParameter("login"));
                Conexao conec = new Conexao();
                Connection conexao = conec.abrirConexao();

                ValidaUsuario vd = new ValidaUsuario(conexao);
                boolean validado = vd.validaUsuario(request.getParameter("login"), senha);

            if (validado) {
                HttpSession sessao = request.getSession();
                sessao.setAttribute("login", request.getParameter("login"));
                sessao.setAttribute("id", objUsuario.getId());
                sessao.setAttribute("admin", objUsuario.isAdministrador());
                sessao.setAttribute("name", objUsuario.getFirstName());
                response.sendRedirect("/ERP/home");
            } else {
                response.sendRedirect("/ERP");
                session.setAttribute("msg", "Usuário ou senha incorretos.");

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}

