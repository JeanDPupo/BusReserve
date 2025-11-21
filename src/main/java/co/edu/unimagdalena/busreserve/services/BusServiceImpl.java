package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.BusDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Bus;
import co.edu.unimagdalena.busreserve.domine.repositories.BusRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.BusService;
import co.edu.unimagdalena.busreserve.services.mapper.BusMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BusServiceImpl implements BusService {

    private final BusRepository busRepo;
    private final BusMapper mapper;

    @Override
    public BusResponse create(BusCreateRequest req) {
        boolean exists = busRepo.findByPlate(req.plate()).isPresent();
        if (exists)
            throw new NotFoundException("Plate already registered");

        Bus bus = mapper.toEntity(req);
        return mapper.toResponse(busRepo.save(bus));
    }

    @Override
    @Transactional(readOnly = true)
    public BusResponse get(Long id) {
        return busRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Bus not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public BusResponse getByPlate(String plate) {
        return busRepo.findByPlate(plate)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Bus not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusResponse> list(Pageable pageable) {
        return busRepo.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BusResponse> findAvailable() {
        return busRepo.findByAvailableTrue()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public BusResponse update(Long id, BusUpdateRequest req) {
        Bus bus = busRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Bus not found"));

        mapper.patch(bus, req);

        return mapper.toResponse(busRepo.save(bus));
    }

    @Override
    public void delete(Long id) {
        Bus bus = busRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Bus not found"));

        busRepo.delete(bus);
    }

    @Override
    public void setAvailability(Long id, boolean available) {
        Bus bus = busRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Bus not found"));

        bus.setAvailable(available);
        busRepo.save(bus);
    }
}
