package org.databases;

import org.DAO.DataAccessObject;
import org.models.Brand;
import org.models.PurchaseList;
import org.models.PurchaseStockList;
import org.models.PurchasehasStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Component("purchasehastockdb")
public class PurchasehasStockdb implements DataAccessObject<PurchasehasStock> {


    private NamedParameterJdbcTemplate jdbc;

    @Autowired
    public void setJdbc(DataSource jdbc) {
        this.jdbc = new NamedParameterJdbcTemplate(jdbc);
    }

    @Override
    public List<PurchasehasStock> getAllList() {

        String sql = """
                
      SELECT `puid`, `stockcode`, `qty`, `org_price` FROM `purchase_has_stock` WHERE 1
                
                
                """;

        return jdbc.query(sql, (rs, event) ->getPurchasstock(rs));
    }

    public List<PurchaseList>  getPurchaseDashboardList(){

        String sql = """
                
            SELECT p.puid, p.pudate, s.suname,
            SUM(phs.qty * phs.org_price) AS total
            FROM `purchase` p
            INNER JOIN purchase_has_stock phs ON phs.puid = p.puid
            INNER JOIN supplier s ON s.sid = phs.sid
            GROUP BY p.puid, p.pudate, s.suname
            ORDER BY p.puid DESC;
                
                
                """;

        return jdbc.query(sql, (rs, event) ->getPuchaseList(rs));

    }

    public List<PurchaseStockList>getPurchaseStockList(String purchaseCode){

        String sql = """
                
                SELECT s.stockname,phs.qty,phs.org_price as price,
                (phs.qty*phs.org_price) as total from`purchase_has_stock` phs
                inner JOIN stock s on phs.stockcode=s.stockcode
                where phs.puid = :puid
                
                """;

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("puid",purchaseCode);

        return jdbc.query(sql, parameterSource, (rs, rowNum) -> getPuchaseStockList(rs));




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
    public int insert(PurchasehasStock purchasehasStock) {

        String sql = "INSERT INTO `purchase_has_stock`(`puid`, `sid`, `stockcode`, `qty`, `org_price`, `remarks`) VALUES (:puid,:sid,:stockcode,:qty,:org_price,:remarks)";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(purchasehasStock);

        return jdbc.update(sql,param );
    }

    @Override
    public int update(PurchasehasStock purchasehasStock) {

        String sql = "UPDATE `purchase_has_stock` SET `qty`=:qty,`org_price`=:org_price WHERE `stockcode`=:stockcode AND `puid` =:puid";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(purchasehasStock);

        return jdbc.update(sql,param );
    }

    @Override
    public int[] updateBatch(List<PurchasehasStock> t) {
        return new int[0];
    }

    @Transactional
    @Override
    public int[] insertBatch(List<PurchasehasStock> t) {

        String sql = "INSERT INTO `purchase_has_stock`(`puid`, `sid`, `stockcode`, `qty`, `org_price`, `remarks`) VALUES (:puid,:sid,:stockcode,:qty,:org_price,:remarks)";

        SqlParameterSource[] param = SqlParameterSourceUtils.createBatch(t.toArray());

        return jdbc.batchUpdate(sql,param);
    }

    public PurchaseList getPuchaseList(ResultSet rs) throws SQLException {

        PurchaseList purchaseList = new PurchaseList();

        purchaseList .setPuid(rs.getString("puid"));
        purchaseList .setPudate(rs.getDate("pudate"));
        purchaseList .setTotal(rs.getInt("total"));
        purchaseList .setSuname(rs.getString("suname"));



        return  purchaseList;

    }
    public org.models.PurchaseStockList getPuchaseStockList(ResultSet rs) throws SQLException {

        PurchaseStockList stockList = new PurchaseStockList();

        stockList .setStockname(rs.getString("stockname"));
        stockList .setQty(rs.getInt("qty"));
        stockList .setPrice(rs.getInt("price"));
        stockList .setTotal(rs.getInt("total"));


        return  stockList;

    }

    public PurchasehasStock getPurchasstock(ResultSet rs) throws SQLException {

        PurchasehasStock purchasehasStock = new PurchasehasStock();

        purchasehasStock.setPuid(rs.getString("puid"));
        purchasehasStock.setStockcode(rs.getString("stockcode"));
        purchasehasStock.setQty(rs.getInt("qty"));
        purchasehasStock.setOrg_price(rs.getInt("org_price"));

    return purchasehasStock;

    }


}
