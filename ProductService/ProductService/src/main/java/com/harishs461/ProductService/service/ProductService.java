package com.harishs461.ProductService.service;

import com.harishs461.ProductService.dto.ProductRequest;
import com.harishs461.ProductService.dto.ProductResponse;
import com.harishs461.ProductService.model.Product;
import com.harishs461.ProductService.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {

        Product product = Product.builder()
                                .title(productRequest.getTitle())
                                .description(productRequest.getDescription())
                                .price(productRequest.getPrice())
                                .build();
        productRepository.save(product);
    }

    public List<ProductResponse> getAllProducts() {

        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> mapToProductResponse(product)).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .description(product.getDescription())
                .title(product.getTitle())
                .price(product.getPrice())
                .build();
    }
}
