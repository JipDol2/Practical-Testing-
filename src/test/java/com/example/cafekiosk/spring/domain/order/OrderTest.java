package com.example.cafekiosk.spring.domain.order;

import com.example.cafekiosk.spring.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.cafekiosk.spring.domain.order.OrderStatus.*;
import static com.example.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.example.cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;

class OrderTest {

    @Test
    @DisplayName("상품리스트에서 주문의 총 금액을 계산한다")
    void calculateTotalPrice(){
        //given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        //when
        Order order = Order.create(products, LocalDateTime.now());

        //then
        assertThat(order.getTotalPrice()).isEqualTo(3000);
    }

    @Test
    @DisplayName("주문 생성 시 주문 상태는 INIT 이다")
    void calculateStatusInit(){
        //given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        //when
        Order order = Order.create(products, LocalDateTime.now());

        //then
        assertThat(order.getOrderStatus()).isEqualTo(INIT);
        assertThat(order.getOrderStatus()).isEqualByComparingTo(INIT);
    }

    @Test
    @DisplayName("주문 생성 시 주문 등록 시간을 기록한다.")
    void registeredDateTime(){
        //given
        LocalDateTime registerDateTime = LocalDateTime.now();
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        //when
        Order order = Order.create(products, registerDateTime);


        //then
        assertThat(order.getRegisteredDateTime()).isEqualTo(registerDateTime);
    }

    private Product createProduct(String productNumber, int price){
        return Product.builder()
                .type(HANDMADE)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }

}