//package fptu.edu.vn.training.model.mapper;
//
//import fptu.edu.vn.training.model.entity.Part;
//import fptu.edu.vn.training.model.request.PartRequest;
//import fptu.edu.vn.training.model.response.PartResponse;
//import org.mapstruct.*;
//
//@Mapper(componentModel = "spring")
//public interface PartMapper {
//
//    // ================================
//    // 1) Entity → Response
//    // ================================
//    @Mapping(target = "categoryName", source = "category.categoryName")
//    @Mapping(target = "createdByName", source = "createdBy.fullName")
//    PartResponse toResponse(Part part);
//
//    // ================================
//    // 2) Request → Entity
//    // ================================
//    @Mapping(target = "partId", ignore = true)
//    @Mapping(target = "partCode", ignore = true)
//    @Mapping(target = "category", ignore = true)
//    @Mapping(target = "createdById", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "modifiedBy", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "modifiedDate", ignore = true)
//    Part toEntity(PartRequest request);
//
//    // ================================
//    // 3) Update Entity từ Request (PUT)
//    // ================================
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(target = "partId", ignore = true)
//    @Mapping(target = "partCode", ignore = true)
//    @Mapping(target = "category", ignore = true)
//    @Mapping(target = "createdById", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "modifiedBy", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    void updateEntity(@MappingTarget Part entity, PartRequest request);
//
//}