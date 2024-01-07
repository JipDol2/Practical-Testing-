package com.example.cafekiosk.spring.domain.stock;

import com.example.cafekiosk.spring.IntegrationTestSupport;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class StockRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private StockRepository stockRepository;


    @Test
    @DisplayName("상품번호 리스트로 재고를 조회한다")
    void findAllByProductNumberIn(){
        //given
        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        Stock stock3 = Stock.create("003", 1);

        stockRepository.saveAll(List.of(stock1,stock2,stock3));

        //when
        List<Stock> findStocks = stockRepository.findAllByProductNumberIn(List.of("001","002"));

        //then
        assertThat(findStocks).hasSize(2)
                .extracting("productNumber","quantity")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001",2),
                        Tuple.tuple("002",2)
                );
    }
}