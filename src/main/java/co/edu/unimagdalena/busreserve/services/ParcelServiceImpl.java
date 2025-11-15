package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.ParcelDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Parcel;
import co.edu.unimagdalena.busreserve.domine.entities.ParcelStatus;
import co.edu.unimagdalena.busreserve.domine.repositories.ParcelRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.ParcelService;
import co.edu.unimagdalena.busreserve.services.mapper.ParcelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ParcelServiceImpl implements ParcelService {

    private final ParcelRepository parcelRepo;
    private final ParcelMapper mapper;

    @Override
    public ParcelResponse create(ParcelCreateRequest req) {
        Parcel parcel = mapper.toEntity(req);
        parcel.setCode("PCL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        parcel.setDeliveryOtp(String.format("%04d", (int)(Math.random() * 10000)));

        return mapper.toResponse(parcelRepo.save(parcel));
    }

    @Override
    @Transactional(readOnly = true)
    public ParcelResponse get(Long id) {
        return parcelRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Parcel %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public ParcelResponse getByCode(String code) {
        return parcelRepo.findByCode(code)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Parcel with code %s not found".formatted(code)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParcelResponse> findByFromStop(Long fromStopId) {
        return parcelRepo.findByFromStopIdAndStatus(fromStopId, ParcelStatus.CREATED)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParcelResponse> findByToStop(Long toStopId) {
        return parcelRepo.findByToStopIdAndStatusIn(toStopId, List.of(ParcelStatus.IN_TRANSIT, ParcelStatus.DELIVERED))
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParcelResponse> findByStatus(ParcelStatus status) {
        return parcelRepo.findByStatusAndTripId(status, null)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public ParcelResponse update(Long id, ParcelUpdateRequest req) {
        Parcel parcel = parcelRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Parcel %d not found".formatted(id)));

        mapper.patch(parcel, req);

        return mapper.toResponse(parcelRepo.save(parcel));
    }

    @Override
    public void delete(Long id) {
        if (!parcelRepo.existsById(id)) {
            throw new NotFoundException("Parcel %d not found".formatted(id));
        }
        parcelRepo.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, ParcelStatus status) {
        Parcel parcel = parcelRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Parcel %d not found".formatted(id)));

        parcel.setStatus(status);
        parcelRepo.save(parcel);
    }

    @Override
    public void deliverParcel(String code, String otp, String proofPhotoUrl) {
        Parcel parcel = parcelRepo.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Parcel with code %s not found".formatted(code)));

        if (!otp.equals(parcel.getDeliveryOtp())) {
            throw new IllegalStateException("Invalid OTP");
        }

        parcel.setStatus(ParcelStatus.DELIVERED);
        parcel.setProofPhotoUrl(proofPhotoUrl);
        parcelRepo.save(parcel);
    }
}
