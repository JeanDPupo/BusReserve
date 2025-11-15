package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.SeatHoldDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.HoldStatus;
import co.edu.unimagdalena.busreserve.domine.entities.SeatHold;
import co.edu.unimagdalena.busreserve.domine.entities.Trip;
import co.edu.unimagdalena.busreserve.domine.entities.User;
import co.edu.unimagdalena.busreserve.domine.repositories.SeatHoldRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.TripRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.UserRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.SeatHoldService;
import co.edu.unimagdalena.busreserve.services.mapper.SeatHoldMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatHoldServiceImpl implements SeatHoldService {

    private final SeatHoldRepository holdRepo;
    private final TripRepository tripRepo;
    private final UserRepository userRepo;
    private final SeatHoldMapper mapper;

    @Override
    public SeatHoldResponse holdSeat(SeatHoldCreateRequest req) {
        Trip trip = tripRepo.findById(req.tripId())
                .orElseThrow(() -> new NotFoundException("Trip %d not found".formatted(req.tripId())));

        User user = userRepo.findById(req.userId())
                .orElseThrow(() -> new NotFoundException("User %d not found".formatted(req.userId())));

        if (!isSeatAvailable(req.tripId(), req.seatNumber())) {
            throw new IllegalStateException("Seat %s is not available".formatted(req.seatNumber()));
        }

        SeatHold hold = mapper.toEntity(req);
        hold.setTrip(trip);
        hold.setUser(user);
        hold.setStatus(HoldStatus.HOLD);
        hold.setExpiresAt(LocalDateTime.now().plusMinutes(10));  // WHAT HAPPEND!?

        return mapper.toResponse(holdRepo.save(hold));
    }

    @Override
    @Transactional(readOnly = true)
    public SeatHoldResponse get(Long id) {
        return holdRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("SeatHold %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatHoldResponse> listByTrip(Long tripId) {
        return holdRepo.findByTripIdAndSeatNumberAndStatus(tripId, null, HoldStatus.HOLD)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public void releaseHold(Long id) {
        SeatHold hold = holdRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("SeatHold %d not found".formatted(id)));

        hold.setStatus(HoldStatus.EXPIRED);
        holdRepo.save(hold);
    }

    @Override
    public void expireHolds() {
        int expired = holdRepo.expireHolds(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSeatAvailable(Long tripId, String seatNumber) {
        return holdRepo.findActiveOrExpiredHold(tripId, seatNumber, LocalDateTime.now())
                .isEmpty();
    }
}
