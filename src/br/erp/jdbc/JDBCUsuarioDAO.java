package br.erp.jdbc;

import br.erp.login.PasswordUtils;
import br.erp.login.ValidaUsuario;
import br.erp.modelo.Usuario;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUsuarioDAO {
    private Connection conexao;

    public JDBCUsuarioDAO(Connection conexao){
        this.conexao = conexao;
    }

    public boolean  cadastrar(Usuario usuario){
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
            p.setDate(4, null);
            if(!usuario.getDataNasc().equals("")) {
                p.setString(4, usuario.getDataNasc());
            }
            p.setInt(5, admin);

            PasswordUtils pu = new PasswordUtils();
            String senha = pu.convertPassword(usuario.getSenha());
            senha += geraHash(buscaid()+1);
            p.setString(6, senha);

            p.execute();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Usuario> buscar(String busca, boolean onlyActive){
        List<Usuario> listUsuarios = new ArrayList<>();
        Usuario usuario;

        String sql = "select * from Usuario ";

        if (!busca.equals("") || onlyActive){
            sql += "where";
        }
        if (!busca.equals("")){
            sql += " primeiroNome like '" +busca + "' or idUsuario like '" + busca + "' or ultimoNome like '" +busca + "'";
            if(onlyActive){
                sql += " and";
            }
        }
        if (onlyActive) {
            sql += " inativado = 0 ";
        }
        sql += "order by primeiroNome asc;";
        try {
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                usuario = new Usuario();
                usuario.setFirstName(rs.getString("primeiroNome"));
                usuario.setLastName(rs.getString("ultimoNome"));
                usuario.setEmail("");
                if(!rs.getString("email").equals("")){
                    usuario.setEmail(rs.getString("email"));
                }
                usuario.setDataNasc("");
                if(rs.getString("dataNasc") != null){
                    usuario.setDataNasc(rs.getString("dataNasc"));
                }
                usuario.setAdministrador(false);
                if(rs.getInt("nivel") == 1) {
                    usuario.setAdministrador(true);
                }
                usuario.setId(rs.getInt("idUsuario"));
                usuario.setInativado(false);
                if(rs.getInt("inativado") == 1){
                    usuario.setInativado(true);
                }
                listUsuarios.add(usuario);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listUsuarios;
    }

    public int buscaid(){
        int id = 0;
        String sql = "select Max(idUsuario) as id from Usuario;";
        try{
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                id = rs.getInt("id");
            }
            }
        catch(Exception e){
            e.printStackTrace();
        }

        return id;
    }

    public Usuario buscarPorId(int id){

        String comando = "select * from Usuario where idUsuario = ?";
        PreparedStatement p;

        Usuario usuario = null;

        try{
            p = conexao.prepareStatement(comando);
            p.setInt(1, id);

            ResultSet rs = p.executeQuery();
            while (rs.next()){
                usuario = new Usuario();

                boolean admin = false;
                if(rs.getInt("nivel") == 1){
                    admin = true;
                }
                usuario.setAdministrador(admin);

                String email = "";
                if(rs.getString("email") != null){
                    email = rs.getString("email");
                }
                usuario.setEmail(email);

                String dataNasc = "";
                if(rs.getString("dataNasc") != null){
                    dataNasc = rs.getString("dataNasc");
                }
                usuario.setDataNasc(dataNasc);
                usuario.setId(id);
                usuario.setFirstName(rs.getString("primeiroNome"));
                usuario.setLastName(rs.getString("ultimoNome"));

            }
            return usuario;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;

    }

    public boolean atualizaUsuario(Usuario user){
        String novaSenha = "";
        byte[] hash;
        String sql = "update Usuario set primeiroNome = ?, ultimoNome = ?, email = ?, dataNasc = ?, nivel = ?";

            if(!user.getSenha().equals("")){
                PasswordUtils pu = new PasswordUtils();
                novaSenha = pu.convertPassword(user.getSenha());
                hash = geraHash(user.getId());
                if(hash == null){
                    return false;
                }
                novaSenha += hash;
                sql += ", senha = ?";
            }

        sql += " where idUsuario = ?;";
        PreparedStatement p;

        try{
            p = conexao.prepareStatement(sql);
            p.setString(1, user.getFirstName());
            p.setString(2, user.getLastName());
            p.setString(3, user.getEmail());
            p.setString(4, user.getDataNasc());
            if(user.isAdministrador()) {
                p.setInt(5, 1);
            }
            else{
                p.setInt(5, 0);
            }
            //Colocar a criptografia da senha antes disso.
            if(!user.getSenha().equals("")) {
                p.setString(6, novaSenha);
                p.setInt(7, user.getId());
            }
            else{
                p.setInt(6, user.getId());
            }
            p.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    return true;
    }

    public boolean deletaUsuario(int id){
        String sql = "update Usuario set inativado = 1 where idUsuario = ?;";
        PreparedStatement p;
            try{
                p = conexao.prepareStatement(sql);
                p.setInt(1, id);
                p.execute();
            }
            catch (Exception e){
                e.printStackTrace();
                return false;
            }
        return true;
    }

    public boolean ativaUsuario(int id){
        String sql = "update Usuario set inativado = 0 where idUsuario = ?;";
        PreparedStatement p;
        try{
            p = conexao.prepareStatement(sql);
            p.setInt(1, id);
            p.execute();
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public byte[] geraHash(int id){
        PasswordUtils pu = new PasswordUtils();
        byte[] hash = pu.geraSalt();
        String sql = "select * from Usuario_hash where idUsuario = "+id+";";

            try {
                Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if(rs.next()){
                    sql = "update Usuario_hash set hash = '" + hash +"' where idUsuario = "+id +";";
                }
                else{
                    sql = "insert into Usuario_hash (idUsuario, hash) values ("+ id +","+"'"+ hash+"'" +");";
                }
                stmt.execute(sql);

        }catch(Exception e ){
            e.printStackTrace();
            return null;
        }
        return hash;
    }
    public Usuario getUserForSession(String login){
        boolean testaemail = login.matches("\\S+@\\S+\\.\\S+");
        String sql = "select * from Usuario where ";
        Usuario usuario = new Usuario();
        if (testaemail){
            sql += "Email = '"+ login + "';";
        }
        else{
            sql += "idUsuario = " + login + ";";
        }
        try {
            Statement stmt = this.conexao.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {

                boolean admin = false;
                if(rs.getInt("nivel") == 1){
                    admin = true;
                }
                usuario.setAdministrador(admin);

                String email = "";
                if(rs.getString("email") != null){
                    email = rs.getString("email");
                }
                usuario.setEmail(email);

                String dataNasc = "";
                if(rs.getString("dataNasc") != null){
                    dataNasc = rs.getString("dataNasc");
                }
                usuario.setDataNasc(dataNasc);
                usuario.setId(rs.getInt("idUsuario"));
                usuario.setFirstName(rs.getString("primeiroNome"));
                usuario.setLastName(rs.getString("ultimoNome"));
            }
        }catch(Exception e){
            e.printStackTrace();
            return usuario;
        }
    return usuario;
    }
}
