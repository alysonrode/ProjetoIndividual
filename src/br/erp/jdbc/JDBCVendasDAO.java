package br.erp.jdbc;

import br.erp.modelo.Produto;
import br.erp.modelo.Venda;
import com.mysql.cj.xdevapi.Result;
import javassist.bytecode.stackmap.BasicBlock;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
                sql = "insert into venda_produtos (idVenda, idProduto, quantidadeProduto) values (?,?,?);";
                p = this.conec.prepareStatement(sql);

                p.setInt(1, idVenda);
                p.setInt(2, idProduto);
                p.setInt(3, produto.getQuantidade());

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
    public List<Venda> search(String busca, int minValue, int maxValue){
        String sql = "select * from Vendas v ";
        List<Venda> sells = new ArrayList<>();
        Venda venda = null;
        if(!busca.equals("")){
            if(isInt(busca)){
                sql += "where v.idVenda = ?";
            }
            else if(isDate(busca)){
                sql += "where v.dataVenda = ?";
            }
            else{
                sql += "inner join Usuario u on " +
                        "u.idUsuario = v.vendedor_id"+
                        " where u.primeiroNome like ?";
            }
        }
        sql += " order by v.dataVenda desc";

        if(minValue != -1 && maxValue != -1) {

            sql += " limit " + minValue + "," + maxValue + ";";

        }else{
            sql += ";";
        }

        PreparedStatement p;
        try{
            p = this.conec.prepareStatement(sql);
            if(!busca.equals("")){
                if(isInt(busca)){
                    p.setInt(1, Integer.parseInt(busca));
                }
                else if(isDate(busca)){
                    String[] str = busca.split("/");
                    String dataFormated = str[2] + "/" + str[1] + "/" + str[0];
                    p.setString(1, dataFormated);
                }
                else{
                    p.setString(1, busca);
                }
            }

            ResultSet rs = p.executeQuery();
            while(rs.next()){

                venda = new Venda();

                JDBCUsuarioDAO jdbcUsuarioDAO = new JDBCUsuarioDAO(conec);
                venda.setUsuario(jdbcUsuarioDAO.buscarPorId(rs.getInt("vendedor_id")));

                Date data = rs.getDate("dataVenda");
                String datavenda = (data.getDate() + 1) + "/" + (data.getMonth() + 1) + "/" + (data.getYear() + 1900);
                venda.setDataVenda(datavenda);

                venda.setValorTotal(rs.getDouble("valortotal"));
                venda.setIdVenda(rs.getInt("idVenda"));

                sells.add(venda);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sells;
    }
    public boolean isInt(String value){
        try{
            Integer.parseInt(value);
            return true;
        }catch (NumberFormatException e){
            return false;
        }


    }
    public boolean isDate(String value){
        if(value.matches("([0-9]{2})\\/([0-9]{2})\\/([0-9]{4})")){
            return true;
        }else{
            return false;
        }
    }
    public List<Produto> montaProdutos(int idVenda){
        String sql = "select * from venda_produtos where idVenda = ?";

        JDBCProdutoDAO jdbcProdutoDAO = new JDBCProdutoDAO(conec);
        List<Produto> produtos = new ArrayList<>();
        Produto produto = null;
        PreparedStatement p;
        try{
            p = this.conec.prepareStatement(sql);
            p.setInt(1, idVenda);
            ResultSet rs = p.executeQuery();

            while (rs.next()){
                produto = jdbcProdutoDAO.getProductById(rs.getInt("idProduto"));
                produto.setQuantidade(rs.getInt("quantidadeProduto"));
                produtos.add(produto);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return produtos;
    }

    public boolean updateVenda(Venda venda){
        String sql = "update Vendas set valorTotal = ? where idVenda = ?";

        PreparedStatement p;
        try{

            p = this.conec.prepareStatement(sql);
            p.setDouble(1,venda.getValorTotal());
            p.setInt(2, venda.getIdVenda());
            p.execute();

            sql = "delete from venda_produtos where idVenda = ?";

            p = this.conec.prepareStatement(sql);
            p.setInt(1, venda.getIdVenda());

            p.execute();
            for(Produto produto : venda.getListProduts()){
                String sql2 = "insert into venda_produtos (idVenda, idProduto, quantidadeProduto) values (?,?,?);";
                PreparedStatement p2 = this.conec.prepareStatement(sql2);

                p2.setInt(1, venda.getIdVenda());
                p2.setInt(2, produto.getId());
                p2.setInt(3, produto.getQuantidade());

                p2.execute();

            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deletar(int id){
        String sql = "delete from Vendas where idVenda = ?";
        PreparedStatement p;
        try{
            p = this.conec.prepareStatement(sql);
            p.setInt(1,id);
            p.execute();

            sql = "delete from venda_produtos where idVenda = ?";

            p = this.conec.prepareStatement(sql);
            p.setInt(1,id);
            p.execute();

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    return true;
    }
}
