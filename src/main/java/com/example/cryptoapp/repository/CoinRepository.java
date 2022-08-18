package com.example.cryptoapp.repository;

import com.example.cryptoapp.dto.CoinTransactionDTO;
import com.example.cryptoapp.entity.Coin;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableConfigurationProperties
public class CoinRepository {
    private static String INSERT = "insert into coin (name, datetime, price, quantity) values (?,?,?,?)";
    private static String SELECT_ALL = "select name, sum(quantity) as quantity from coin group by name";
    private static String DELETE = "delete from coin where id = ?";
    private static String SELECT_BY_NAME = "select * from coin where name = ?";
    private static String UPDATE = "update coin set name = ?, price = ?, quantity = ? where id = ?";
    private JdbcTemplate jdbcTemplate;

    public CoinRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Coin update(Coin coin) {
        Object[] attr = new Object[]{
                coin.getName(),
                coin.getPrice(),
                coin.getQuantity(),
                coin.getId()
        };
        jdbcTemplate.update(UPDATE, attr);
        return coin;
    }

    public Coin insert(Coin coin) {
        Object[] attr = new Object[]{
                coin.getName(),
                coin.getDateTime(),
                coin.getPrice(),
                coin.getQuantity()
        };
        jdbcTemplate.update(INSERT, attr);
        return coin;
    }

    public List<CoinTransactionDTO> getAll() {
        return this.jdbcTemplate.query(SELECT_ALL, (rs, rowNum) -> {
            CoinTransactionDTO coin = new CoinTransactionDTO();
            coin.setName(rs.getString("name"));
            coin.setQuantity(rs.getBigDecimal("quantity"));
            return coin;
        });
    }


    public List<Coin> getByName(String name) {
        Object[] attr = new Object[]{name};
        return jdbcTemplate.query(SELECT_BY_NAME, (rs, rowNum) -> {
            Coin coin = new Coin();
            coin.setId(rs.getInt("id"));
            coin.setName(rs.getString("name"));
            coin.setQuantity(rs.getBigDecimal("quantity"));
            coin.setPrice(rs.getBigDecimal("price"));
            coin.setDateTime(rs.getTimestamp("dateTime"));

            return coin;
        }, attr);
    }

    public int deleteById(int id) {

        return this.jdbcTemplate.update(DELETE, id);

    }

}
