package fptu.edu.vn.training.model.mapper.impl;

import fptu.edu.vn.training.model.entity.PartCategory;
import fptu.edu.vn.training.model.enums.CategoryStatus;
import fptu.edu.vn.training.model.mapper.PartCategoryMapper;
import fptu.edu.vn.training.model.request.PartCategoryRequest;
import fptu.edu.vn.training.model.response.PartCategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class PartCategoryMapperImpl implements PartCategoryMapper {

    @Override
    public PartCategoryResponse toResponse(PartCategory entity) {
        if (entity == null) {
            return null;
        }

        return PartCategoryResponse.builder()
                .categoryId(entity.getCategoryId())
                .categoryCode(entity.getCategoryCode())
                .categoryName(entity.getCategoryName())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .build();
    }

    @Override
    public PartCategory toEntity(PartCategoryRequest request) {
        if (request == null) {
            return null;
        }

        CategoryStatus status = request.getStatus() != null
                ? request.getStatus()
                : CategoryStatus.ACTIVE;

        return PartCategory.builder()
                .categoryCode(request.getCategoryCode())
                .categoryName(request.getCategoryName())
                .description(request.getDescription())
                .status(status)
                .build();
    }
}
