package com.harishs461.OrderService.service;

import com.harishs461.OrderService.dto.InventoryResponse;
import com.harishs461.OrderService.dto.OrderLineItemDto;
import com.harishs461.OrderService.dto.OrderRequest;
import com.harishs461.OrderService.model.Order;
import com.harishs461.OrderService.model.OrderLineItems;
import com.harishs461.OrderService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final String inventoryUrl;

    @Autowired
    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate, @Value("${inventory-url}") String inventoryUrl) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.inventoryUrl = inventoryUrl;
    }

    public void createOrder(OrderRequest orderRequest) {

        List<OrderLineItems> orderLineItemsList =
                orderRequest.getOrderLineItemDtoList()
                        .stream().map(this::mapToDto)
                        .collect(Collectors.toList());

        List<String> skuCodesList = orderLineItemsList.stream()
                .map(OrderLineItems::getSkuCode)
                .collect(Collectors.toList());

        //check if skuCodes are available in inventory
        String generatedUrl = inventoryUrl + "?"
                + String.join("&", skuCodesList.stream()
                .map(sku -> "skuCode=" + sku)
                .collect(Collectors.toList()));
        ResponseEntity<List<InventoryResponse>> response = restTemplate.
                exchange(generatedUrl, HttpMethod.GET,null,
                        new ParameterizedTypeReference<>() {
                        });
        List<InventoryResponse> inventoryResponses = response.getBody();
        boolean result = inventoryResponses.stream().allMatch(InventoryResponse::isInStock);
        if(result) {
            Order order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .orderLineItemsList(orderLineItemsList)
                            .build();
            orderRepository.save(order);
        }
        else {
            throw new IllegalArgumentException("Order not in stock!");
        }

    }

    public OrderLineItems mapToDto(OrderLineItemDto orderLineItemDto) {
        return OrderLineItems.builder()
                .price(orderLineItemDto.getPrice())
                .quantity(orderLineItemDto.getQuantity())
                .skuCode(orderLineItemDto.getSkuCode())
                .build();
    }
}
