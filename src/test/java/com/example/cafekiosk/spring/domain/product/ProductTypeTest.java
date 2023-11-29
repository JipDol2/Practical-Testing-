package com.example.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeTest {


    @Test
    @DisplayName("상품 타입이 재고 관련 타입인지 체크한다")
    void containsStockType(){
        //given
        ProductType type = ProductType.HANDMADE;

        //when
        boolean response = ProductType.containsStockType(type);

        //then
        assertThat(response).isFalse();
    }

    @Test
    @DisplayName("상품 타입이 재고 관련 타입인지 체크한다")
    void containsStockType2(){
        //given
        ProductType type = ProductType.BOTTLE;

        //when
        boolean response = ProductType.containsStockType(type);

        //then
        assertThat(response).isTrue();
    }
}