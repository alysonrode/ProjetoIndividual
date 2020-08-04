package br.erp.filter;;

import br.erp.modelo.Usuario;
import br.erp.rest.UsuariosREST;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.http.HttpResponse;

@WebFilter()
public class FiltroConexao implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException{
        String context = request.getServletContext().getContextPath();

        try{
            HttpSession session = ((HttpServletRequest) request).getSession();
            String usuario = null;
            if (session != null ){
                usuario = (String) session.getAttribute("login");
                UsuariosREST usrRST = new UsuariosREST();
            }
            if (usuario == null){
                session.setAttribute("msg", "Você não está logado no sistema");
                ((HttpServletResponse) response).sendRedirect(context);
            }
            else{
                chain.doFilter(request, response);
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
