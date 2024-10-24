package org.saleorderoptions;

import org.DAO.DataAccessObject;
import org.models.Brand;
import org.saleoptions.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("saleorderdb")
public class SaleOrderdb implements DataAccessObject<SaleOrder> {

    private NamedParameterJdbcTemplate jdbc;

    @Autowired
    public void setJdbc(DataSource jdbc) {
        this.jdbc = new NamedParameterJdbcTemplate(jdbc);
    }


    @Override
    public List<SaleOrder> getAllList() {

        String sql = """
                
                SELECT
                    s.stockcode,
                    st.stockname,
                    w.wdesc,
                    s.qty,
                    s.price,
                    s.discount,
                    (s.price * s.qty) - s.discount AS total,
                    p.payid
                FROM
                    sale s
                INNER JOIN
                    stock st ON st.stockcode = s.stockcode
                INNER JOIN
                    warranty w ON w.wid = s.wid
                INNER JOIN
                    orderr orr ON orr.oid = s.oid
                INNER JOIN
                    payment p ON p.payid = orr.payid
                WHERE
                    orr.oid = '#O-20241023-3';          
                
                
                
                
                
                
                
                
                """;


        return List.of();
    }

    public List<SaleOrder> getFindID(String orderid) {
        String sql = """
            SELECT
                s.stockcode,
                st.stockname,
                w.wdesc,
                s.qty,
                s.price,
                s.discount,
                (s.price * s.qty) - s.discount AS total,
                p.payid
            FROM
                sale s
            INNER JOIN
                stock st ON st.stockcode = s.stockcode
            INNER JOIN
                warranty w ON w.wid = s.wid
            INNER JOIN
                orderr orr ON orr.oid = s.oid
            INNER JOIN
                payment p ON p.payid = orr.payid
            WHERE
                orr.oid = :orderid;          
            """;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("orderid", orderid);

        return jdbc.query(sql, parameters, (rs, rowNum) -> getSaleOrder(rs));
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
    public int insert(SaleOrder saleOrder) {
        return 0;
    }

    @Override
    public int update(SaleOrder saleOrder) {
        return 0;
    }

    @Override
    public int[] updateBatch(List<SaleOrder> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<SaleOrder> t) {
        return new int[0];
    }

    public SaleOrder getSaleOrder(ResultSet rs) throws SQLException {

        SaleOrder saleOrder = new SaleOrder();


        saleOrder.setStockcode(rs.getString("stockcode"));
        saleOrder.setStockname(rs.getString("stockname"));
        saleOrder.setWdesc(rs.getString("wdesc"));
        saleOrder.setQty(rs.getInt("qty"));
        saleOrder.setPrice(rs.getInt("price"));
        saleOrder.setDiscount(rs.getInt("discount"));
        saleOrder.setTotal(rs.getInt("total"));
        saleOrder.setPayid(rs.getInt("payid"));

        return saleOrder;



    }
}
