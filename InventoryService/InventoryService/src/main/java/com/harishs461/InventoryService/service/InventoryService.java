package com.harishs461.InventoryService.service;

import com.harishs461.InventoryService.dto.InventoryRequest;
import com.harishs461.InventoryService.dto.InventoryResponse;
import com.harishs461.InventoryService.model.Inventory;
import com.harishs461.InventoryService.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode)
                .stream()
                .map(inventory -> mapToDto(inventory))
                .collect(Collectors.toList());
    }

    private InventoryResponse mapToDto(Inventory inventory) {

        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity()>1?true:false)
                .build();
    }
    public void createInventoryItem(InventoryRequest inventoryRequest) {

        Inventory inventory = Inventory.builder()
                .skuCode(inventoryRequest.getSkuCode())
                .quantity(inventoryRequest.getQuantity())
                .build();
        inventoryRepository.save(inventory);
    }
}
