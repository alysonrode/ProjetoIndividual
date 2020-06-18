package br.erp.jdbc;

import br.erp.modelo.Usuario;

import javax.naming.ldap.ExtendedRequest;
import javax.ws.rs.GET;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JDBCUsuarioDAO {
    private Connection conexao;

    public JDBCUsuarioDAO(Connection conexao){
        this.conexao = conexao;
    }

    public boolean cadastrar(Usuario usuario){
        String sql = "insert into Usuario (primeiroNome, ultimoNome, email, dataNasc, nivel, senha) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement p;

        try{
            int admin = 0;
            if(usuario.isAdministrador()){
                admin = 1;
            }
            p = this.conexao.prepareStatement(sql);
            p.setString(1, usuario.getFirstName());
            p.setString(2, usuario.getLastName());
            p.setString(3, usuario.getEmail());
            p.setString(4, usuario.getDataNasc());
            p.setInt(5, admin);
            p.setString(6, usuario.getSenha());

            p.execute();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Usuario> buscar(String busca){
        List<Usuario> listUsuarios = new ArrayList<>();
        Usuario usuario;
        String sql = "select * from Usuario";
        if (!busca.equals("")){
            sql += " where primeiroNome like '" +busca + "' or idUsuario like '" + busca + "' or ultimoNome like '" +busca + "'";
        }
        sql += " order by primeiroNome asc;";
        try {
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(sql);
            System.out.println(rs.next());
            while(rs.next() == true){
                usuario = new Usuario();
                usuario.setFirstName(rs.getString("primeiroNome"));
                usuario.setLastName(rs.getString("ultimoNome"));
                usuario.setEmail("");
                System.out.println("AAA:" + usuario.getFirstName());
                if(rs.getString("email") != null){
                    usuario.setEmail(rs.getString("email"));
                }
                usuario.setDataNasc("");
                if(rs.getString("email") != null){
                    usuario.setDataNasc(rs.getString("dataNasc"));
                }
                usuario.setAdministrador(false);
                if(rs.getInt("nivel") == 1) {
                    usuario.setAdministrador(true);
                }
                listUsuarios.add(usuario);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listUsuarios;
    }

}
