package com.study.rdb_study.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> save(@RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.save(request);

        return ResponseEntity
                .created(URI.create("/customers/" + response.getCustomerId()))
                .body(response);
    }
 /*
    # 일반 사용자용
    GET    /customers/me           - 내 정보 조회
    PUT    /customers/me           - 내 정보 수정
    PATCH  /customers/me/password  - 비밀번호 변경
    PATCH  /customers/me/deactivate - 회원 탈퇴 (status 변경)

    # 관리자용
    GET    /customers              - 전체 조회
    GET    /customers/{id}         - 특정 회원 조회
    DELETE /customers/{id}         - 물리 삭제 (극히 예외적)
*/

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable Long id) {
        CustomerResponse response = customerService.findById(id);
        return ResponseEntity.ok(response);
    }

    // 관리자용
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                       @RequestBody CustomerRequest request) {
        customerService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest request) {
        customerService.updatePassword(id, request.getInputPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> withdraw(@PathVariable Long id) {
        customerService.withdraw(id);
        return ResponseEntity.noContent().build();
    }
}
