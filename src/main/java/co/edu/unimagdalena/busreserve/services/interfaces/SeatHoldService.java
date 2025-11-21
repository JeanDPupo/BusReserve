package co.edu.unimagdalena.busreserve.services.interfaces;

import co.edu.unimagdalena.busreserve.api.dto.SeatHoldDtos.*;

import java.util.List;

public interface SeatHoldService {
    SeatHoldResponse holdSeat(SeatHoldCreateRequest req);
    SeatHoldResponse get(Long id);
    List<SeatHoldResponse> listByTrip(Long tripId);
    void releaseHold(Long id);
    void expireHolds();
    boolean isSeatAvailable(Long tripId, String seatNumber);
}
