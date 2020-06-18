package br.erp.bd;

import java.sql.Connection;

public class Conexao {

    private Connection conexao;
    public Connection abrirConexao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Diz ao objeto conexão qual é o banco de dados que ele deve se conectar
            conexao = java.sql.DriverManager.getConnection("jdbc:mysql://localhost/ERP?" +
                    "user=erp&password=as&useTimezone=true&serverTimezone=UTC");

        }catch(Exception e) {
            e.printStackTrace();
        }
        return conexao;
    }
    public void fecharConexao() {
        try {
            //Fecha conexão
            conexao.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
