package org.orderoptions;

import org.DAO.DataAccessObject;
import org.models.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("orderdb")
public class Orderdb implements DataAccessObject<Order> {


    private NamedParameterJdbcTemplate jdbc;


    @Autowired
    public void setJdbc(DataSource jdbc) {


        this.jdbc = new NamedParameterJdbcTemplate(jdbc);

    }

    @Override
    public List<Order> getAllList() {
        return jdbc.query(" SELECT * FROM `orderr` order BY odate desc,cast(Substring(oid,13) as Unsigned)desc ", (rs, rowNum) ->getOrder(rs));
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
    public int insert(Order order) {
        String sql = "INSERT INTO `orderr`(`oid`, `odate`, `cuname`, `cuphone`, `remark`, `payid`) VALUES (:oid,:odate,:cuname,:cuphone,:remark,:payid)";

        return jdbc.query(sql, (rs, rowNum) -> getOrder(rs));

    }

    @Override
    public int update(Order order) {
        return 0;
    }

    @Override
    public int[] updateBatch(List<Order> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<Order> t) {
        return new int[0];
    }

    public Order getOrder(ResultSet rs) throws SQLException {

        Order order = new Order();
        order.setOid(rs.getString("oid"));
        order.setOdate(rs.getDate("odate"));
        order.setCuname(rs.getString("cuname"));
        order.setCuphone(rs.getString("cuphone"));
        order.setPayid(rs.getInt("payid"));

        return order;


    }


}
