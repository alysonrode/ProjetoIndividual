package br.erp.jdbc;

import br.erp.modelo.Produto;
import br.erp.modelo.Usuario;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import com.mysql.cj.xdevapi.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JDBCProdutoDAO {

    private Connection conexao;

    public JDBCProdutoDAO(Connection conexao){
        this.conexao = conexao;
    }

    public int buscaid(){
        int id = 0;
        int id2 = 0;
        String sql = "select Max(idProduto) as id from Produto;";
        String sql2 = "select Max(idProduto) as id from produtos_deletados;";
        try{
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                id = rs.getInt("id");
            }
            rs = stmt.executeQuery(sql2);
            if(rs.next()){
                id2 = rs.getInt("id");
            }
            if(id2 > id){
                id = id2;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return id;
    }

    public boolean verificaIntegridade(Produto produto){

        String sql = "select * from Produto where nome = ? and marca = ? and idProduto != ?;";
        PreparedStatement p;
        try{
            p = this.conexao.prepareStatement(sql);
            p.setString(1,produto.getNome());
            p.setString(2, produto.getMarca());
            p.setInt(3, produto.getId());
            ResultSet rs = p.executeQuery();

            if(rs.next()){
                return false;
            }
            else{
                return true;
            }
        }
        catch (Exception e){
            return false;
        }

    }
    public boolean cadastrar(Produto produto){
        String sql = "insert into Produto (nome, marca, quantidade, valor) values (?,?,?,?);";
        PreparedStatement p;
        try{
            p = conexao.prepareStatement(sql);
            p.setString(1, produto.getNome());
            p.setString(2, produto.getMarca());
            p.setInt(3, produto.getQuantidade());
            p.setDouble(4, produto.getValor());
            p.execute();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public List<Produto> buscar(String busca){
        String sql = "select * from Produto";
        if(!busca.equals("")){
            sql += " where nome like '%" + busca + "%' or marca like '%" + busca + "%' or idProduto = '" + busca + "'";
        }

        sql += ";";

        List<Produto> produtoList = new ArrayList<>();
        Produto produto;
        try {
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                produto = new Produto();
                produto.setId(rs.getInt("idProduto"));
                produto.setMarca(rs.getString("marca"));
                produto.setNome(rs.getString("nome"));
                produto.setQuantidade(rs.getInt("quantidade"));
                produto.setValor(rs.getDouble("valor"));

                produtoList.add(produto);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return produtoList;
    }

    public Produto getProductById(int id){
        String sql = "select * from Produto where idProduto = ?;";
        PreparedStatement p;
        Produto product = new Produto();
        try{
            p = this.conexao.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                product.setQuantidade(rs.getInt("quantidade"));
                product.setNome(rs.getString("nome"));
                product.setMarca(rs.getString("marca"));
                product.setValor(rs.getDouble("valor"));
                product.setId(id);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return product;
    }

    public boolean updateProduct(Produto produto){
        String sql = "update Produto set nome = ?, marca =?, quantidade = ?, valor = ? where idProduto = ?;";
        PreparedStatement p;

        try{
            p = this.conexao.prepareStatement(sql);
            p.setString(1, produto.getNome());
            p.setString(2, produto.getMarca());
            p.setInt(3, produto.getQuantidade());
            p.setDouble(4, produto.getValor());
            p.setInt(5, produto.getId());


            p.execute();

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
    public boolean deleteProduct(int id){
        String sql = "delete from Produto where idProduto = ?";
        PreparedStatement p;
        boolean inserted = insertProductDeleted(id);
        if(inserted) {
            try {
                p = this.conexao.prepareStatement(sql);
                p.setInt(1, id);
                p.execute();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    public boolean insertProductDeleted(int id){
        String sql1 = "SELECT * FROM Produto where idProduto = ?";
        String sql2 = "insert into produtos_deletados (idProduto, nome, marca, quantidade, valor)" +
                " values (?,?,?,?,?);";
        PreparedStatement p;
        PreparedStatement p1;
        try{
            p = this.conexao.prepareStatement(sql1);
            p1 = this.conexao.prepareStatement(sql2);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                p1.setInt(1, id);
                p1.setString(2, rs.getString("nome"));
                p1.setString(3, rs.getString("marca"));
                p1.setInt(4, rs.getInt("quantidade"));
                p1.setDouble(5, rs.getDouble("valor"));
                p1.execute();
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
