//package fptu.edu.vn.training.service.impl;
//
//import fptu.edu.vn.training.model.entity.PartCategory;
//import fptu.edu.vn.training.model.enums.CategoryStatus;
//import fptu.edu.vn.training.model.mapper.PartCategoryMapper;
//import fptu.edu.vn.training.model.request.PartCategoryRequest;
//import fptu.edu.vn.training.model.response.PartCategoryResponse;
//import fptu.edu.vn.training.repository.PartCategoryRepository;
//import fptu.edu.vn.training.service.PartCategoryService;
//import fptu.edu.vn.training.utils.AuditLogUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class PartCategoryServiceImpl implements PartCategoryService {
//
//    private final PartCategoryRepository repository;
//    private final PartCategoryMapper mapper;
//    private final AuditLogUtils audit;
//
//    private PartCategory getCategoryOrThrow(Integer id) {
//        return repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//    }
//
//    @Override
//    public List<PartCategoryResponse> getAll() {
//        return repository.findAll().stream()
//                .filter(c -> c.getStatus() != CategoryStatus.DELETED)
//                .map(mapper::toResponse)
//                .toList();
//    }
//
//    @Override
//    public List<PartCategoryResponse> getDeleted() {
//        return repository.findAll().stream()
//                .filter(c -> c.getStatus() == CategoryStatus.DELETED)
//                .map(mapper::toResponse)
//                .toList();
//    }
//
//    @Override
//    public PartCategoryResponse getById(Integer id) {
//        return mapper.toResponse(getCategoryOrThrow(id));
//    }
//
//    @Override
//    public PartCategoryResponse create(PartCategoryRequest request) {
//
//        if (repository.existsByCategoryCode(request.getCategoryCode())) {
//            throw new RuntimeException("Category code already exists");
//        }
//
//        PartCategory entity = mapper.toEntity(request);
//        entity.setStatus(CategoryStatus.ACTIVE);
//
//        PartCategory saved = repository.save(entity);
//
//        audit.log("CATEGORY_CREATED: " + saved.getCategoryId());
//
//        return mapper.toResponse(saved);
//    }
//
//    @Override
//    public PartCategoryResponse update(Integer id, PartCategoryRequest request) {
//        PartCategory category = getCategoryOrThrow(id);
//
//        if (request.getCategoryCode() != null && !request.getCategoryCode().isBlank()) {
//            category.setCategoryCode(request.getCategoryCode());
//        }
//        if (request.getCategoryName() != null && !request.getCategoryName().isBlank()) {
//            category.setCategoryName(request.getCategoryName());
//        }
//        if (request.getDescription() != null) {
//            category.setDescription(request.getDescription());
//        }
//        if (request.getStatus() != null) {
//            category.setStatus(request.getStatus());
//        }
//
//        PartCategory saved = repository.save(category);
//
//        audit.log("CATEGORY_UPDATED: " + id);
//
//        return mapper.toResponse(saved);
//    }
//
//    @Override
//    public void softDelete(Integer id) {
//        PartCategory category = getCategoryOrThrow(id);
//
//        category.setStatus(CategoryStatus.DELETED);
//        repository.save(category);
//
//        audit.log("CATEGORY_DELETED: " + id);
//    }
//
//    @Override
//    public PartCategoryResponse restore(Integer id) {
//        PartCategory category = getCategoryOrThrow(id);
//
//        category.setStatus(CategoryStatus.ACTIVE);
//        PartCategory saved = repository.save(category);
//
//        audit.log("CATEGORY_RESTORED: " + id);
//
//        return mapper.toResponse(saved);
//    }
//}
//
