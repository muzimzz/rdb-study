package com.study.rdb_study.admin.product;


import com.study.rdb_study.product.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    @PostMapping
    public ResponseEntity<AdminProductResponse> save(@RequestBody AdminProductCreateRequest request) {
        AdminProductResponse response = adminProductService.save(request);

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

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody AdminProductCreateRequest request) {
        adminProductService.update(id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id) {
        adminProductService.updateStatus(id, ProductStatus.DISCONTINUED);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AdminProductResponse>> findAll() {

        List<AdminProductResponse> responseList = adminProductService.findAll();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminProductResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(adminProductService.findById(id));
    }

    @GetMapping("/unsold")
    public ResponseEntity<List<AdminUnsoldProductResponse>> findLongUnsoldProducts() {
        return ResponseEntity.ok(adminProductService.findLongUnsoldProducts());
    }
}
