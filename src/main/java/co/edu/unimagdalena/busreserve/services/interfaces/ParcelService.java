package co.edu.unimagdalena.busreserve.services.interfaces;

import co.edu.unimagdalena.busreserve.api.dto.ParcelDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.ParcelStatus;

import java.util.List;

public interface ParcelService {
    ParcelResponse create(ParcelCreateRequest req);
    ParcelResponse get(Long id);
    ParcelResponse getByCode(String code);
    List<ParcelResponse> findByFromStop(Long fromStopId);
    List<ParcelResponse> findByToStop(Long toStopId);
    List<ParcelResponse> findByStatus(ParcelStatus status);
    ParcelResponse update(Long id, ParcelUpdateRequest req);
    void delete(Long id);
    void updateStatus(Long id, ParcelStatus status);
    void deliverParcel(String code, String otp, String proofPhotoUrl);
}
