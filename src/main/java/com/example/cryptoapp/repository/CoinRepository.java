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
        String jpql = "select c from Coin c where c.name like :name";
        TypedQuery<Coin> query = entityManager.createQuery(jpql, Coin.class);
        query.setParameter("name", "%"+name+"%");
        return query.getResultList();

    }
@Transactional
    public boolean deleteById(int id) {
        Coin coin = entityManager.find(Coin.class, id);
        if(!entityManager.contains(coin)){
          coin = entityManager.merge(coin);
        }

        entityManager.remove(coin);
        return true;
    }
}
