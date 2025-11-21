package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.ParcelDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Parcel;
import co.edu.unimagdalena.busreserve.domine.entities.ParcelStatus;
import co.edu.unimagdalena.busreserve.domine.entities.Stop;
import co.edu.unimagdalena.busreserve.domine.repositories.ParcelRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.StopRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.ParcelService;
import co.edu.unimagdalena.busreserve.services.mapper.ParcelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class ParcelServiceImpl implements ParcelService {

    private final ParcelRepository parcelRepo;
    private final StopRepository stopRepo;
    private final ParcelMapper mapper;

    @Override
    public ParcelResponse create(ParcelCreateRequest req) {

        Stop from = stopRepo.findById(req.fromStopId())
                .orElseThrow(() -> new NotFoundException("From stop not found"));

        Stop to = stopRepo.findById(req.toStopId())
                .orElseThrow(() -> new NotFoundException("To stop not found"));

        Parcel parcel = mapper.toEntity(req);
        parcel.setFromStop(from);
        parcel.setToStop(to);

        parcel.setCode("P-" + UUID.randomUUID().toString().substring(0, 8));
        parcel.setDeliveryOtp(String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999)));

        parcel.setPrice(BigDecimal.valueOf(5000)); // puedes cambiar a cálculo dinámico

        return mapper.toResponse(parcelRepo.save(parcel));
    }

    @Override
    @Transactional(readOnly = true)
    public ParcelResponse get(Long id) {
        return parcelRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Parcel not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public ParcelResponse getByCode(String code) {
        return parcelRepo.findByCode(code)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Parcel not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParcelResponse> findByFromStop(Long fromStopId) {
        return parcelRepo.findByFromStopId(fromStopId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParcelResponse> findByToStop(Long toStopId) {
        return parcelRepo.findByToStopId(toStopId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParcelResponse> findByStatus(ParcelStatus status) {
        return parcelRepo.findByStatus(status)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public ParcelResponse update(Long id, ParcelUpdateRequest req) {
        Parcel parcel = parcelRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Parcel not found"));

        mapper.patch(parcel, req);

        return mapper.toResponse(parcelRepo.save(parcel));
    }

    @Override
    public void delete(Long id) {
        Parcel parcel = parcelRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Parcel not found"));

        parcelRepo.delete(parcel);
    }

    @Override
    public void updateStatus(Long id, ParcelStatus status) {
        Parcel parcel = parcelRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Parcel not found"));

        parcel.setStatus(status);
        parcelRepo.save(parcel);
    }

    @Override
    public void deliverParcel(String code, String otp, String proofPhotoUrl) {
        Parcel parcel = parcelRepo.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Parcel not found"));

        if (!parcel.getDeliveryOtp().equals(otp))
            throw new NotFoundException("Invalid OTP");

        parcel.setProofPhotoUrl(proofPhotoUrl);
        parcel.setStatus(ParcelStatus.DELIVERED);

        parcelRepo.save(parcel);
    }
}