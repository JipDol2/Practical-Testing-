package com.example.cafekiosk.spring.api.service.order;

import com.example.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.example.cafekiosk.spring.api.service.order.response.OrderResponse;
import com.example.cafekiosk.spring.domain.order.OrderRepository;
import com.example.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import com.example.cafekiosk.spring.domain.product.Product;
import com.example.cafekiosk.spring.domain.product.ProductRepository;
import com.example.cafekiosk.spring.domain.product.ProductType;
import com.example.cafekiosk.spring.domain.stock.Stock;
import com.example.cafekiosk.spring.domain.stock.StockRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static com.example.cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
//@Transactional
@SpringBootTest
//@DataJpaTest
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void tearDown(){
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    void createOrder(){
        //given
        Product product1 = createProduct(HANDMADE,"001",1000);
        Product product2 = createProduct(HANDMADE,"002",2000);
        Product product3 = createProduct(HANDMADE,"003",3000);

        productRepository.saveAll(List.of(product1,product2,product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        //when
        LocalDateTime registerDateTime = LocalDateTime.now();
        OrderResponse orderResponse = orderService.createOrder(request, registerDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime","totalPrice")
                .contains(registerDateTime,3000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001",1000),
                        Tuple.tuple("002",2000)
                );
    }

    @Test
    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    void createOrderWithDuplicateProductNumbers(){
        //given
        Product product1 = createProduct(HANDMADE,"001",1000);
        Product product2 = createProduct(HANDMADE,"002",2000);
        Product product3 = createProduct(HANDMADE,"003",3000);

        productRepository.saveAll(List.of(product1,product2,product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001","001"))
                .build();

        //when
        LocalDateTime registerDateTime = LocalDateTime.now();
        OrderResponse orderResponse = orderService.createOrder(request, registerDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime","totalPrice")
                .contains(registerDateTime,2000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001",1000),
                        Tuple.tuple("001",1000)
                );
    }

    @Test
    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    void createOrderWithStock(){
        //given
        Product product1 = createProduct(BOTTLE,"001",1000);
        Product product2 = createProduct(BAKERY,"002",2000);
        Product product3 = createProduct(HANDMADE,"003",3000);

        productRepository.saveAll(List.of(product1,product2,product3));

        Stock stock1 = Stock.create("001",2);
        Stock stock2 = Stock.create("002",2);

        stockRepository.saveAll(List.of(stock1,stock2));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001","002","003"))
                .build();

        //when
        LocalDateTime registerDateTime = LocalDateTime.now();
        OrderResponse orderResponse = orderService.createOrder(request, registerDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime","totalPrice")
                .contains(registerDateTime,7000);
        assertThat(orderResponse.getProducts()).hasSize(4)
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001",1000),
                        Tuple.tuple("001",1000),
                        Tuple.tuple("002",2000),
                        Tuple.tuple("003",3000)
                );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                .extracting("productNumber","quantity")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001",0),
                        Tuple.tuple("002",1)
                );
    }

    @Test
    @DisplayName("재고가 없는 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
    void createOrderWithNoStock(){
        //given
        Product product1 = createProduct(BOTTLE,"001",1000);
        Product product2 = createProduct(BAKERY,"002",2000);
        Product product3 = createProduct(HANDMADE,"003",3000);

        productRepository.saveAll(List.of(product1,product2,product3));

        Stock stock1 = Stock.create("001",1);
        Stock stock2 = Stock.create("002",1);

        stockRepository.saveAll(List.of(stock1,stock2));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001","002","003"))
                .build();

        //when //then
        LocalDateTime registerDateTime = LocalDateTime.now();

        assertThatThrownBy(()->orderService.createOrder(request, registerDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("재고가 부족한 상품이 있습니다.");
    }

    private Product createProduct(ProductType type,String productNumber,int price){
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }
}