package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.TripDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Bus;
import co.edu.unimagdalena.busreserve.domine.entities.Route;
import co.edu.unimagdalena.busreserve.domine.entities.Trip;
import co.edu.unimagdalena.busreserve.domine.entities.TripStatus;
import co.edu.unimagdalena.busreserve.domine.repositories.BusRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.RouteRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.TripRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.TripService;
import co.edu.unimagdalena.busreserve.services.mapper.TripMapper;
import co.edu.unimagdalena.busreserve.services.interfaces.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepo;
    private final RouteRepository routeRepo;
    private final BusRepository busRepo;
    private final TripMapper mapper;

    @Override
    public TripResponse create(TripCreateRequest req) {
        Route route = routeRepo.findById(req.routeId())
                .orElseThrow(() -> new NotFoundException("Route %d not found".formatted(req.routeId())));

        Bus bus = busRepo.findById(req.busId())
                .orElseThrow(() -> new NotFoundException("Bus %d not found".formatted(req.busId())));

        if (!bus.getAvailable()) {
            throw new IllegalStateException("Bus %s is not available".formatted(bus.getPlate()));
        }

        Trip trip = mapper.toEntity(req);
        trip.setRoute(route);
        trip.setBus(bus);

        return mapper.toResponse(tripRepo.save(trip));
    }

    @Override
    @Transactional(readOnly = true)
    public TripResponse get(Long id) {
        return tripRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Trip %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TripResponse> list(Pageable pageable) {
        return tripRepo.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripResponse> findAvailableTrips(String origin, String destination, LocalDateTime date) {
        List<TripStatus> availableStatuses = List.of(TripStatus.SCHEDULED, TripStatus.BOARDING);
        return tripRepo.findAvailableTrips(origin, destination, date, availableStatuses)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripResponse> findByStatus(TripStatus status) {
        return tripRepo.findByStatusAndDepartureAtBefore(status, LocalDateTime.now().plusYears(1))
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public TripResponse update(Long id, TripUpdateRequest req) {
        Trip trip = tripRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Trip %d not found".formatted(id)));

        mapper.patch(trip, req);

        return mapper.toResponse(tripRepo.save(trip));
    }

    @Override
    public void delete(Long id) {
        Trip trip = tripRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Trip %d not found".formatted(id)));

        if (trip.getStatus() != TripStatus.SCHEDULED) {
            throw new IllegalStateException("Cannot delete trip with status %s".formatted(trip.getStatus()));
        }

        tripRepo.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, TripStatus status) {
        Trip trip = tripRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Trip %d not found".formatted(id)));

        trip.setStatus(status);
        tripRepo.save(trip);
    }

    @Override
    public void openBoarding(Long id) {
        Trip trip = tripRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Trip %d not found".formatted(id)));

        if (trip.getStatus() != TripStatus.SCHEDULED) {
            throw new IllegalStateException("Can only open boarding for scheduled trips");
        }

        trip.setStatus(TripStatus.BOARDING);
        tripRepo.save(trip);
    }

    @Override
    public void closeBoarding(Long id) {
        Trip trip = tripRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Trip %d not found".formatted(id)));

        if (trip.getStatus() != TripStatus.BOARDING) {
            throw new IllegalStateException("Trip is not in boarding status");
        }

        trip.setStatus(TripStatus.SCHEDULED);
        tripRepo.save(trip);
    }

    @Override
    public void depart(Long id) {
        Trip trip = tripRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Trip %d not found".formatted(id)));

        if (trip.getStatus() != TripStatus.BOARDING) {
            throw new IllegalStateException("Can only depart from boarding status");
        }

        trip.setStatus(TripStatus.DEPARTED);
        tripRepo.save(trip);
    }
}
