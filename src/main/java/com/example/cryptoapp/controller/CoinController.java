package com.example.cryptoapp.controller;

import com.example.cryptoapp.entity.Coin;
import com.example.cryptoapp.repository.CoinRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@RestController
@RequestMapping("/coin")
public class CoinController {
    private CoinRepository coinRepository;

    public CoinController(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    @Bean
    public void init() {

        System.out.println("GENERATE DATA ON COIN.");

        Coin c1 = new Coin();
        c1.setName("BITCOIN");
        c1.setPrice(new BigDecimal(100000));
        c1.setQuantity(new BigDecimal(200));
        c1.setDateTime(new Timestamp(System.currentTimeMillis()));
        Coin c2 = new Coin();
        c2.setName("ETERIUM");
        c2.setPrice(new BigDecimal(15000));
        c2.setQuantity(new BigDecimal(2000));
        c2.setDateTime(new Timestamp(System.currentTimeMillis()));
        Coin c3 = new Coin();
        c3.setName("POKERFI");
        c3.setPrice(new BigDecimal(1));
        c3.setQuantity(new BigDecimal(200000));
        c3.setDateTime(new Timestamp(System.currentTimeMillis()));

        coinRepository.insert(c1);
        coinRepository.insert(c2);
        coinRepository.insert(c3);

    }

    @GetMapping()
    public ResponseEntity getAll() {
        return new ResponseEntity(this.coinRepository.getAll(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody Coin coin) {
        try {
            coin.setDateTime(new Timestamp(System.currentTimeMillis()));
            return new ResponseEntity(this.coinRepository.insert(coin), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity get(@PathVariable String name) {
        try {
            return new ResponseEntity(this.coinRepository.getByName(name), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity get(@PathVariable int id) {
        try {
            this.coinRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NO_CONTENT);
        }

    }

    @PutMapping()
    public ResponseEntity put(@RequestBody Coin coin) {
        try {
            coin.setDateTime(new Timestamp(System.currentTimeMillis()));
            return new ResponseEntity(coinRepository.update(coin), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NO_CONTENT);
        }
    }
}
