package com.study.rdb_study.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 관리자용
    @PostMapping
    public ResponseEntity<ProductResponse> save(@RequestBody ProductRequest request) {
        ProductResponse response = productService.save(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getProductId())
                .toUri();

        return ResponseEntity
                // .created(URI.create("/products/" + response.getProductId()))
                .created(location)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        ProductResponse response = productService.findById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    // 관리자용
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                       @RequestBody ProductRequest request) {
        productService.update(id, request);

        return ResponseEntity.noContent().build();
    }

    // 관리자용
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        productService.deleteById(id);

        return ResponseEntity.noContent().build();

    }
}