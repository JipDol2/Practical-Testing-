package com.example.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

    @DisplayName("상품 타입이 재고 관련 타입인지 체크한다.")
    @CsvSource({"HANDMADE,false","BOTTLE,true","BAKERY,true"})
    @ParameterizedTest
    void containsStockType3(ProductType productType,boolean expected){

        //when
        boolean response = ProductType.containsStockType(productType);

        //then
        assertThat(response).isEqualTo(expected);
    }

    @DisplayName("상품 타입이 재고 관련 타입인지 체크한다.")
    @MethodSource("provideProductTypesForCheckingStockType")
    @ParameterizedTest
    void containsStockType4(ProductType productType,boolean expected){
        //when
        boolean response = ProductType.containsStockType(productType);

        //then
        assertThat(response).isEqualTo(expected);
    }

    private static Stream<Arguments> provideProductTypesForCheckingStockType(){
        return Stream.of(
                Arguments.of(ProductType.HANDMADE,false),
                Arguments.of(ProductType.BOTTLE,true),
                Arguments.of(ProductType.BAKERY,true)
        );
    }

}