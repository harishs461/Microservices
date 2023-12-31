package com.harishs461.OrderService.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderRequest {
    private List<OrderLineItemDto> orderLineItemDtoList;
}
