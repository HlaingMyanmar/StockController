package org.databases;

import org.DAO.DataAccessObject;
import org.models.Brand;
import org.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("categorydb")
public class Categorydb implements DataAccessObject<Category>{

    private NamedParameterJdbcTemplate jdbc;

    @Autowired
    public void setJdbc(DataSource jdbc) {
        this.jdbc = new NamedParameterJdbcTemplate(jdbc);
    }

    @Override
    public List<Category> getAllList() {

        return jdbc.query("SELECT * FROM `category` WHERE cidd  is null;", (rs, rowNum) ->getBrand(rs));

    }

    public List<Category> getAllListforCIDD(){


        String sql = """
                
                SELECT `cid`, `cname`, `cidd`
                FROM `category`
                ORDER BY
                    CAST(SUBSTRING(`cid`, 6, 8) AS UNSIGNED) DESC,
                    CAST(SUBSTRING_INDEX(`cid`, '-', -1) AS UNSIGNED) desc;
                
                
                """;


        return jdbc.query(sql, (rs, rowNum) ->getBrand(rs));
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
    public int insert(Category category) {

        String sql = "INSERT INTO `category`(`cid`, `cname`, `cidd`) VALUES (:cid,:cname,:cidd)";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(category);

        return jdbc.update(sql,param );

    }

    @Override
    public int update(Category category) {

        String sql = "UPDATE `category` SET `cname` = :cname WHERE `category`.`cid` = :cid;";

        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(category);

        return jdbc.update(sql,param );

    }

    @Override
    public int[] updateBatch(List<Category> t) {
        return new int[0];
    }

    @Override
    public int[] insertBatch(List<Category> t) {
        return new int[0];
    }


    public Category getBrand(ResultSet rs) throws SQLException {

    Category category = new Category();

        category.setCid(rs.getString("cid"));
        category.setCname(rs.getString("cname"));
        category.setCidd(rs.getString("cidd"));

    return  category;

}






}
