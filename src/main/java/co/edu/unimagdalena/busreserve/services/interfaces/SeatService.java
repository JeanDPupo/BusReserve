package co.edu.unimagdalena.busreserve.services.interfaces;

import co.edu.unimagdalena.busreserve.api.dto.SeatDtos.*;

import java.util.List;

public interface SeatService {
    SeatResponse create(SeatCreateRequest req);
    SeatResponse get(Long id);
    List<SeatResponse> listByBus(Long busId);
    void delete(Long id);
    void createSeatsForBus(Long busId, int totalSeats);
}
