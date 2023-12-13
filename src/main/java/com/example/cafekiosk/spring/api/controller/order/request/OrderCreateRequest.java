package com.example.cafekiosk.spring.api.controller.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    @NotEmpty(message = "상품 번호 리스트는 필수입니다.")
    private List<String> productNumbers;

    @Builder
    public OrderCreateRequest(List<String> productNumbers){
        this.productNumbers = productNumbers;
    }

    public OrderCreateRequest toServiceRequest() {
        return OrderCreateRequest.builder()
                .productNumbers(productNumbers)
                .build();
    }
}
