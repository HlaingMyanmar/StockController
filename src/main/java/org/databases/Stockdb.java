package org.databases;

import org.DAO.DataAccessObject;
import org.models.Brand;
import org.models.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("stockdb")
public class Stockdb implements DataAccessObject<Stock> {


    private NamedParameterJdbcTemplate jdbc;

    @Autowired
    public void setJdbc(DataSource jdbc) {
        this.jdbc = new NamedParameterJdbcTemplate(jdbc);
    }


    @Override
    public List<Stock> getAllList() {
//        String sql = """
//
//        SELECT s.stockcode,s.stockname, c.cname, b.bname, s.qty,s.price,(s.qty*s.price) as total FROM `stock` s
//        INNER JOIN category c ON c.cid = s.cid
//        INNER JOIN brand b ON b.bid = s.bid
//        ORDER BY CAST(SUBSTRING(s.stockcode, 4) AS UNSIGNED) desc , CAST(SUBSTRING(s.stockcode, 13) AS UNSIGNED) DESC;
//
//
//            """;

        String sql = """

                SELECT
    s.stockcode,
    s.stockname,
    c.cname,
    b.bname,
    s.qty,
    s.price,
    (s.qty * s.price) AS total
FROM
    `stock` s
INNER JOIN
    category c ON c.cid = s.cid
INNER JOIN
    brand b ON b.bid = s.bid
ORDER BY
    CAST(SUBSTRING(s.stockcode, 3, 8) AS UNSIGNED) DESC,
    CAST(SUBSTRING_INDEX(s.stockcode, '-', -1) AS UNSIGNED) DESC;







""";

        return jdbc.query(sql, (rs, rowNum) -> getStock(rs));




    }

    @Override
    public Brand getBrandById(String id, String name) {
        return null;
    }

    @Override
    public int getDeleteById(String id) {
        return 0;
    }

    @Override
    public int insert(Stock stock) {


        String sql =  "INSERT INTO `stock`(`stockcode`, `stockname`, `qty`, `price`, `cid`, `bid`) VALUES (:stockcode, :stockname, :qty, :price, :ccode, :bid)";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(stock);

        return jdbc.update(sql,param );



    }

    public int delete(Stock stock){

        String sql =  "DELETE FROM `stock` WHERE stockcode=:stockcode";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(stock);

        return jdbc.update(sql,param );



    }
    @Transactional
    public int sumQty(Stock stock){

        String sql = "UPDATE stock SET qty = qty + :qty  WHERE stockcode = :stockcode";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(stock);

        return jdbc.update(sql,param );

    }

    @Transactional
    public int subQty(Stock stock){

        String sql = "UPDATE stock SET qty = qty- :qty  WHERE stockcode = :stockcode";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(stock);

        return jdbc.update(sql,param );

    }


    @Transactional
    public int setPrice(Stock stock){

        String sql = "UPDATE stock SET price = :price  WHERE stockcode = :stockcode";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(stock);
        System.out.println(stock.getPrice()+"\n"+stock.getStockcode());

        return jdbc.update(sql,param );

    }



    @Override
    public int update(Stock stock) {
        return 0;
    }

    @Override
    public int[] updateBatch(List<Stock> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<Stock> t) {
        return new int[0];
    }



    public Stock getStock(ResultSet rs) throws SQLException {

        Stock stock = new Stock();

        stock.setStockcode(rs.getString("stockcode"));
        stock.setStockname(rs.getString("stockname"));
        stock.setCcode(rs.getString("cname"));
        stock.setBid(rs.getString("bname"));
        stock.setQty(rs.getInt("qty"));
        stock.setPrice(rs.getInt("price"));
        stock.setTotal(rs.getInt("total"));

        return  stock;

    }
}
