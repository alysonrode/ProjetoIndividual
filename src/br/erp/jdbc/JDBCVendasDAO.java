package br.erp.jdbc;

import br.erp.modelo.Produto;
import br.erp.modelo.Venda;
import javassist.bytecode.stackmap.BasicBlock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBCVendasDAO {

    private Connection conec;

    public JDBCVendasDAO(Connection conec){
        this.conec = conec;
    }

    public double getValorProduto(Produto produto){

        String sql = "SELECT valor FROM Produto WHERE idProduto = ?;";
        PreparedStatement p;
        double valor = 0;
        try{
            p = this.conec.prepareStatement(sql);
            p.setInt(1, produto.getId());
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                valor = rs.getDouble("valor");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return valor;
    }
    public boolean cadastraVenda(Venda venda){

        String sql = "insert into Vendas (valorTotal, dataVenda, vendedor_id) values (?,?,?)";
        PreparedStatement p;

        try{
            p = this.conec.prepareStatement(sql);
            p.setDouble(1, venda.getValorTotal());
            p.setString(2, venda.getDataVenda());
            p.setInt(3, venda.getUsuario().getId());

            p.execute();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        int idVenda = getLasId();
        int idProduto = 0;
        int quantidade = 0;
        try {
            for (Produto produto : venda.getListProduts()) {
                idProduto = produto.getId();
                sql = "insert into venda_produtos (idVenda, idProduto) values (?,?);";
                p = this.conec.prepareStatement(sql);

                p.setInt(1, idVenda);
                p.setInt(2, idProduto);

                p.execute();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        try{
            for(Produto produto : venda.getListProduts()){
                idProduto = produto.getId();
                quantidade = produto.getQuantidade();
                int quantidadeAntiga = 0;

                sql = "Select quantidade from Produto where idProduto = ?;";

                p = this.conec.prepareStatement(sql);
                p.setInt(1, idProduto);
                ResultSet rs = p.executeQuery();
                if(rs.next()){
                    quantidadeAntiga = rs.getInt("quantidade");
                }

                sql = "update Produto set quantidade = ? where idProduto = ?;";
                p = this.conec.prepareStatement(sql);
                p.setInt(1, (quantidadeAntiga - quantidade));
                p.setInt(2, idProduto);

                p.execute();

            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public int getLasId(){
        String sql = "Select Max(idVenda) as idVenda from Vendas;";
        PreparedStatement p;
        int id = 0;
        try{
            p = this.conec.prepareStatement(sql);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                id = rs.getInt("idVenda");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }
}
