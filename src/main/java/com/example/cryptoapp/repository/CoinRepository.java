package com.example.cryptoapp.repository;

import com.example.cryptoapp.dto.CoinTransactionDTO;
import com.example.cryptoapp.entity.Coin;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@EnableConfigurationProperties
public class CoinRepository {
//    private static String INSERT = "insert into coin (name, datetime, price, quantity) values (?,?,?,?)";
//    private static String SELECT_ALL = "select name, sum(quantity) as quantity from coin group by name";
//    private static String DELETE = "delete from coin where id = ?";
//    private static String SELECT_BY_NAME = "select * from coin where name = ?";
//    private static String UPDATE = "update coin set name = ?, price = ?, quantity = ? where id = ?";
//    private JdbcTemplate jdbcTemplate;

    private EntityManager entityManager;

    public CoinRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Coin update(Coin coin) {
        entityManager.merge(coin);

        return coin;
    }

    @Transactional
    public Coin insert(Coin coin) {
        this.entityManager.persist(coin);
        return coin;
    }

    public List<CoinTransactionDTO> getAll() {
        String jpql = "select new com.example.cryptoapp.dto.CoinTransactionDTO(c.name, sum(c.quantity)) from Coin c group by c.name";
        TypedQuery<CoinTransactionDTO> query = entityManager.createQuery(jpql, CoinTransactionDTO.class);
        return query.getResultList();

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
