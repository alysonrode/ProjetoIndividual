package br.erp.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ValidaUsuario {
    private Connection conexao;
    ValidaUsuario(Connection conexao){
        this.conexao = conexao;
    }

    public boolean validaUsuario(String user, String senha) {

        boolean emailTest = user.matches("\\S+@\\S+\\.\\S+");
        String sql = "select * from Usuario where ";

        sql += emailTest ? "email = ?;" : "idUsuario = ?;";


        PreparedStatement p;

        try{
            p = this.conexao.prepareStatement(sql);
            if (emailTest) {
                p.setString(1, user);
            } else {
                p.setInt(1, Integer.parseInt(user));
            }
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                int id = rs.getInt("idUsuario");
                boolean senhaOk = validaSenha(id, senha);
                if(senhaOk)
                    return true;
            }
            return false;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean validaSenha(int id, String senha){

        String sql = "select * from Usuario_hash where idUsuario = ?;";
        String sql2 = "select * from Usuario where idUsuario = ?;";
        String hash = "";
        String senhaBanco = "";
        PreparedStatement p;

        try {
            p = this.conexao.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                hash = rs.getString("hash");
            }

            p = this.conexao.prepareStatement(sql2);
            p.setInt(1, id);
            ResultSet rs2 = p.executeQuery();
            if(rs2.next()){
                senhaBanco = rs2.getString("senha");
            }

            String senhaNova = senha + hash;
            System.out.println(senhaNova + " - " + senhaBanco);

            if(senhaBanco.equals(senhaNova)){
                return true;
            }

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return false;
    }

}
