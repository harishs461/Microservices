package com.harishs461.InventoryService.controller;

import com.harishs461.InventoryService.dto.InventoryRequest;
import com.harishs461.InventoryService.dto.InventoryResponse;
import com.harishs461.InventoryService.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }



    @PostMapping
    public void createInventoryItem(@RequestBody InventoryRequest inventoryRequest) {
        inventoryService.createInventoryItem(inventoryRequest);
    }
}
