package org.stockServices;

import org.DAO.DataAccessObject;
import org.models.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("stocksetpricedb")
public class StockSetPricedb implements DataAccessObject<StockSetPrice> {



    private NamedParameterJdbcTemplate jdbc;

    @Autowired
    public void setJdbc(DataSource jdbc) {
        this.jdbc = new NamedParameterJdbcTemplate(jdbc);
    }


    @Override
    public List<StockSetPrice> getAllList() {




        return null;
    }

    public List<StockSetPrice> getList(String stockcode){

        String sql = """
                
                SELECT p.puid,p.pudate, phs.org_price as price
                FROM stock st
                INNER JOIN purchase_has_stock phs ON st.stockcode = phs.stockcode
                INNER JOIN purchase p ON p.puid = phs.puid
                WHERE st.stockcode = :stockcode
                ORDER by p.puid desc;
                
  
                
                """;

        MapSqlParameterSource parameterSource = new MapSqlParameterSource("stockcode", stockcode);

        return  jdbc.query(sql,parameterSource,(rs, rowNum) ->getStockSetPrice(rs));


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
    public int insert(StockSetPrice stockSetPrice) {
        return 0;
    }

    @Override
    public int update(StockSetPrice stockSetPrice) {
        return 0;
    }

    @Override
    public int[] updateBatch(List<StockSetPrice> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<StockSetPrice> t) {
        return new int[0];
    }

    public StockSetPrice getStockSetPrice(ResultSet rs) throws SQLException {



        StockSetPrice stockSetPrice = new StockSetPrice();


        stockSetPrice.setPuid(rs.getString("puid"));
        stockSetPrice.setPudate(rs.getDate("pudate"));
        stockSetPrice.setPrice(rs.getInt("price"));

        return stockSetPrice;


    }
}
