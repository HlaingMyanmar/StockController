package org.expense_options;

import org.DAO.DataAccessObject;
import org.models.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("exp_viewdb")
public class Exp_Viewdb implements DataAccessObject<Exp_View> {

    private NamedParameterJdbcTemplate jdbc;


    @Autowired
    public void setJdbc(DataSource jdbc) {


        this.jdbc = new NamedParameterJdbcTemplate(jdbc);

    }
    @Override
    public List<Exp_View> getAllList() {

        String sql = """
                
                SELECT `expense_id`, `expense_date`, `category_id`, `amount`, `description`, `created_at`, `updated_at` FROM `expense`
                ORDER BY
                    CAST(SUBSTRING(`expense_id`, 6, 8) AS UNSIGNED) DESC,         
                    CAST(SUBSTRING_INDEX(`expense_id`, '-', -1) AS UNSIGNED) DESC; 
                
                
                
                """;

        return jdbc.query(sql, (rs, rowNum) -> getExpViewData(rs));
    }
    public List<Exp_View> getAllListView() {

        String sql = """
                
                SELECT e.expense_id, e.expense_date, ec.category_name, e.amount,e.description, e.created_at, e.updated_at FROM `expense` e
                inner join exp_category ec on ec.category_id = e.category_id
                ORDER BY
                CAST(SUBSTRING(e.expense_id, 6, 8) AS UNSIGNED) ASC,        
                CAST(SUBSTRING_INDEX(e.expense_id, '-', -1) AS UNSIGNED) ASC
                
                
                """;

        return jdbc.query(sql, (rs, rowNum) -> getExpViewDataView(rs));
    }

    @Override
    public Brand getBrandById(String id, String name) {
        return null;
    }

    @Override
    public int getDeleteById(String id) {


        String sql = "DELETE FROM `expense` WHERE `expense_id`=id";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("expense_id", id);

        return jdbc.update(sql, parameterSource);
    }

    @Override
    public int insert(Exp_View expView) {

        String sql = "INSERT INTO `expense`(`expense_id`, `expense_date`, `category_id`, `amount`, `description`) VALUES (:expense_id,:expense_date,:category_id,:total,:description)";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(expView);

        return jdbc.update(sql,param);
    }

    @Override
    public int update(Exp_View expView) {


        String sql = """
                
                UPDATE `expense` SET `expense_date`=::expense_date,`category_id`=:category_id,`amount`=:total,`description`=:description,`updated_at`=:updated_at WHERE `expense_id`=:expense_id
                
                
                """;

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(expView);

        return jdbc.update(sql,param);
    }

    @Override
    public int[] updateBatch(List<Exp_View> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<Exp_View> t) {
        return new int[0];
    }

    private Exp_View getExpViewData(ResultSet rs) throws SQLException {


        Exp_View expView = new Exp_View();

        expView.setExpense_id(rs.getString("expense_id"));
        expView.setExpense_date(rs.getDate("expense_date"));
        expView.setCategory_id(rs.getInt("category_id"));
        expView.setDescription(rs.getString("description"));
        expView.setCreated_at(rs.getTimestamp("created_at"));
        expView.setUpdated_at(rs.getTimestamp("updated_at"));
        expView.setTotal(rs.getInt("amount"));


        return expView;



    }

    private Exp_View getExpViewDataView(ResultSet rs) throws SQLException {


        Exp_View expView = new Exp_View();

        expView.setExpense_id(rs.getString("expense_id"));
        expView.setExpense_date(rs.getDate("expense_date"));
        expView.setCategory_name(rs.getString("category_name"));
        expView.setDescription(rs.getString("description"));
        expView.setCreated_at(rs.getTimestamp("created_at"));
        expView.setUpdated_at(rs.getTimestamp("updated_at"));
        expView.setTotal(rs.getInt("amount"));


        return expView;



    }
}
