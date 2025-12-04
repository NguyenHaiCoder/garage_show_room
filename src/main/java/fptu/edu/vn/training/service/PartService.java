package fptu.edu.vn.training.service;

import fptu.edu.vn.training.model.request.PartRequest;
import fptu.edu.vn.training.model.response.PartResponse;

import java.util.List;

public interface PartService {

    PartResponse createPart(PartRequest request, Integer creatorId);

    PartResponse updatePart(Integer id, PartRequest request, Integer modifierId);

    void deletePart(Integer id);

    PartResponse getPart(Integer id);

    List<PartResponse> getAllParts();
}
