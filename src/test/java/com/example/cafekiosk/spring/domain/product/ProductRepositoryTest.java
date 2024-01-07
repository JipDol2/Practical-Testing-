package com.example.cafekiosk.spring.domain.product;

import com.example.cafekiosk.spring.IntegrationTestSupport;
import com.example.cafekiosk.spring.domain.product.Product;
import com.example.cafekiosk.spring.domain.product.ProductRepository;
import com.example.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.example.cafekiosk.spring.domain.product.ProductType;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static com.example.cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;

@Transactional
class ProductRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    void findAllBySellingStatusIn(){
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);

        productRepository.saveAll(List.of(product1,product2,product3));

        //when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber","name","sellingStatus")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001","아메리카노",SELLING),
                        Tuple.tuple("002","카페라떼",HOLD)
                );
    }

    @Test
    @DisplayName("상품번호 리스트로 상품들을 조회한다")
    void findAllByProductNumberIn(){
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);

        productRepository.saveAll(List.of(product1,product2,product3));
        //when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber","name","sellingStatus")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001","아메리카노",SELLING),
                        Tuple.tuple("002","카페라떼",HOLD)
                );
    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품 번호를 읽어온다")
    void findLatestProductNumber(){
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);

        productRepository.saveAll(List.of(product1,product2,product3));
        //when
        String latestProductNumber = productRepository.findLatestProductNumber();

        //then
        assertThat(latestProductNumber).isEqualTo("003");
    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품 번호를 읽어올때, 상품이 하나도 없는 경우에는 null 을 반환한다.")
    void findLatestProductNumberWhenProductIsEmpty(){
        //when
        String latestProductNumber = productRepository.findLatestProductNumber();

        //then
        assertThat(latestProductNumber).isNull();
    }

    private Product createProduct(String productNumber, ProductType productType, ProductSellingStatus sellingStatus, String name, int price) {
        Product product1 = Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
        return product1;
    }

}