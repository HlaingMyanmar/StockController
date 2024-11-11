package org.incomeoptions;


import org.DAO.DataAccessObject;
import org.expense_options.Exp_View;
import org.models.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("income_viewdb")
public class Income_Viewdb implements DataAccessObject<Income_View> {

    private NamedParameterJdbcTemplate jdbc;


    @Autowired
    public void setJdbc(DataSource jdbc) {


        this.jdbc = new NamedParameterJdbcTemplate(jdbc);

    }


    @Override
    public List<Income_View> getAllList() {

        String sql = """
                
                SELECT i.income_id,i.income_date, ic.cat_name, i.amount,i.description,p.paymethodname, i.created_at,i.updated_at FROM `income` i
                               inner join income_category ic on ic.cat_id = i.income_type
                               inner join payment p on i.payid = p.payid
                               ORDER BY
                               CAST(SUBSTRING(i.income_id, 6, 8) AS UNSIGNED) ASC,       
                               CAST(SUBSTRING_INDEX(i.income_id, '-', -1) AS UNSIGNED) ASC      
                
                
                """;

        return jdbc.query(sql, (rs, rowNum) -> getIncomeView_View(rs));
    }

    public List<Income_View> getAllListID() {

        String sql = """

                SELECT `income_id`, `income_date`, `income_type`, `amount`, `payid`, `description`, `created_at`, `updated_at` FROM `income` 
                ORDER BY
                CAST(SUBSTRING(`income_id`, 6, 8) AS UNSIGNED) DESC,
                CAST(SUBSTRING_INDEX(`income_id`, '-', -1) AS UNSIGNED) DESC     
               
             
                
                
                
                """;

        return jdbc.query(sql, (rs, rowNum) -> getIncomeView(rs));
    }


    @Override
    public Brand getBrandById(String id, String name) {
        return null;
    }

    @Override
    public int getDeleteById(String id) {

        String sql = "DELETE FROM `income` WHERE `income_id` = :income_id";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("income_id", id);

        return jdbc.update(sql, parameterSource);
    }

    @Override
    public int insert(Income_View incomeView) {

        String sql = "INSERT INTO `income`(`income_id`, `income_date`, `income_type`, `amount`, `payid`, `description`) VALUES (:income_id,:income_date,:income_type,:amount,:payid,:description)";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(incomeView);

        return jdbc.update(sql, param);
    }

    @Override
    public int update(Income_View incomeView) {

        String sql = "UPDATE `income` SET `income_type`=:income_type,`amount`=:amount,`payid`=:payid,`description`=:description,`updated_at`=:updated_at WHERE `income_id`=:income_id";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(incomeView);

        return jdbc.update(sql, param);
    }
    public int updateAmount(Income_View incomeView) {

        String sql = "UPDATE `income` SET `amount`=:amount,`updated_at`=:updated_at WHERE `income_id`=:income_id";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(incomeView);

        return jdbc.update(sql, param);
    }

    public int updateDesc(Income_View incomeView) {

        String sql = "UPDATE `income` SET `description`=:description,`updated_at`=:updated_at WHERE `income_id`=:income_id";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(incomeView);

        return jdbc.update(sql, param);
    }

    public int paymentupdate(Income_View incomeView) {


        String sql = """
                
                 UPDATE `income`
                 SET `updated_at` = NOW(), `payid` = :payid
                 WHERE `income_id` = :income_id
                
                
                """;

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(incomeView);

        return jdbc.update(sql,param);
    }

    @Override
    public int[] updateBatch(List<Income_View> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<Income_View> t) {
        return new int[0];
    }


    private  Income_View getIncomeView(ResultSet rs) throws SQLException {


        Income_View incomeView = new Income_View();

        incomeView.setIncome_id(rs.getString("income_id"));
        incomeView.setIncome_date(rs.getDate("income_date"));
        incomeView.setIncome_type(rs.getInt("income_type"));
        incomeView.setAmount(rs.getInt("amount"));
        incomeView.setPayid(rs.getInt("payid"));
        incomeView.setDescription(rs.getString("description"));
        incomeView.setCreated_at(rs.getTimestamp("created_at"));
        incomeView.setUpdated_at(rs.getTimestamp("updated_at"));

        return incomeView;




    }

    private  Income_View getIncomeView_View(ResultSet rs) throws SQLException {


        Income_View incomeView = new Income_View();

        incomeView.setIncome_id(rs.getString("income_id"));
        incomeView.setIncome_date(rs.getDate("income_date"));
        incomeView.setIncome_name(rs.getString("cat_name"));
        incomeView.setAmount(rs.getInt("amount"));
        incomeView.setPaymentmethod(rs.getString("paymethodname"));
        incomeView.setDescription(rs.getString("description"));
        incomeView.setCreated_at(rs.getTimestamp("created_at"));
        incomeView.setUpdated_at(rs.getTimestamp("updated_at"));

        return incomeView;




    }
}
