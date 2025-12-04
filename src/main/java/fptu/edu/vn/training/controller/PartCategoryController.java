package fptu.edu.vn.training.controller;

import fptu.edu.vn.training.model.request.PartCategoryRequest;
import fptu.edu.vn.training.model.response.ApiResponse;
import fptu.edu.vn.training.model.response.PartCategoryResponse;
import fptu.edu.vn.training.service.PartCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class PartCategoryController {

    private final PartCategoryService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PartCategoryResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách nhóm phụ tùng thành công", service.getAll()));
    }

    @GetMapping("/trash")
    public ResponseEntity<ApiResponse<List<PartCategoryResponse>>> getDeleted() {
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đã xóa thành công", service.getDeleted()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PartCategoryResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success("Chi tiết nhóm phụ tùng", service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PartCategoryResponse>> create(@RequestBody PartCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Tạo nhóm phụ tùng thành công", service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PartCategoryResponse>> update(
            @PathVariable Integer id,
            @RequestBody PartCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật nhóm phụ tùng thành công", service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Integer id) {
        service.softDelete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa nhóm phụ tùng thành công"));
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<PartCategoryResponse>> restore(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success("Khôi phục thành công", service.restore(id)));
    }
}

