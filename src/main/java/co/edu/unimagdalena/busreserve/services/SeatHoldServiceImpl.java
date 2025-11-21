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

    private final SeatHoldRepository seatHoldRepo;
    private final TripRepository tripRepo;
    private final UserRepository userRepo;
    private final SeatHoldMapper mapper;

    @Override
    public SeatHoldResponse holdSeat(SeatHoldCreateRequest req) {
        Trip trip = tripRepo.findById(req.tripId())
                .orElseThrow(() -> new NotFoundException("Trip not found"));

        User user = userRepo.findById(req.userId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        boolean exists = seatHoldRepo.findByTripIdAndSeatNumber(
                req.tripId(), req.seatNumber()
        ).isPresent();

        if (exists)
            throw new NotFoundException("Seat already on hold");

        SeatHold hold = mapper.toEntity(req);
        hold.setTrip(trip);
        hold.setUser(user);

        return mapper.toResponse(seatHoldRepo.save(hold));
    }

    @Override
    @Transactional(readOnly = true)
    public SeatHoldResponse get(Long id) {
        return seatHoldRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("SeatHold not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatHoldResponse> listByTrip(Long tripId) {
        return seatHoldRepo.findByTripIdAndStatus(tripId, HoldStatus.HOLD)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public void releaseHold(Long id) {
        SeatHold hold = seatHoldRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("SeatHold not found"));

        hold.setStatus(HoldStatus.EXPIRED);
        seatHoldRepo.save(hold);
    }

    @Override
    public void expireHolds() {
        List<SeatHold> expired = seatHoldRepo.findByStatusAndExpiresAtBefore(
                HoldStatus.HOLD,
                LocalDateTime.now()
        );

        expired.forEach(h -> h.setStatus(HoldStatus.EXPIRED));
        seatHoldRepo.saveAll(expired);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSeatAvailable(Long tripId, String seatNumber) {
        Integer num = Integer.parseInt(seatNumber);

        return seatHoldRepo
                .findByTripIdAndSeatNumber(tripId, num)
                .isEmpty();
    }
}
