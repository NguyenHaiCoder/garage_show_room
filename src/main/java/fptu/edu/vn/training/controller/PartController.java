//package fptu.edu.vn.training.controller;
//
//import fptu.edu.vn.training.model.entity.Users;
//import fptu.edu.vn.training.model.request.PartRequest;
//import fptu.edu.vn.training.model.response.ApiResponse;
//import fptu.edu.vn.training.model.response.PartResponse;
//import fptu.edu.vn.training.service.PartService;
//
//import jakarta.validation.Valid;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/parts")
//public class PartController {
//
//    private final PartService partService;
//
//    // ============================
//    // CREATE
//    // ============================
//    @PostMapping
//    public ResponseEntity<ApiResponse<PartResponse>> create(
//            @Valid @RequestBody PartRequest request,
//            @AuthenticationPrincipal Users user) {
//
//        PartResponse data = partService.createPart(request, user.getUserId());
//
//        return ResponseEntity.ok(
//                ApiResponse.success("Tạo phụ tùng thành công", data)
//        );
//    }
//
//    // ============================
//    // UPDATE
//    // ============================
//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse<PartResponse>> update(
//            @PathVariable Integer id,
//            @Valid @RequestBody PartRequest request,
//            @AuthenticationPrincipal Users user) {
//
//        PartResponse data = partService.updatePart(id, request, user.getUserId());
//
//        return ResponseEntity.ok(
//                ApiResponse.success("Cập nhật phụ tùng thành công", data)
//        );
//    }
//
//    // ============================
//    // GET ALL
//    // ============================
//    @GetMapping
//    public ResponseEntity<ApiResponse<?>> getAll() {
//
//        return ResponseEntity.ok(
//                ApiResponse.success(
//                        "Danh sách phụ tùng",
//                        partService.getAllParts()
//                )
//        );
//    }
//
//    // ============================
//    // GET BY ID
//    // ============================
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<PartResponse>> getById(@PathVariable Integer id) {
//
//        PartResponse data = partService.getPart(id);
//
//        return ResponseEntity.ok(
//                ApiResponse.success("Thông tin phụ tùng", data)
//        );
//    }
//
//    // ============================
//    // DELETE
//    // ============================
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Integer id) {
//
//        partService.deletePart(id);
//
//        return ResponseEntity.ok(
//                ApiResponse.success("Xóa phụ tùng thành công", null)
//        );
//    }
//}