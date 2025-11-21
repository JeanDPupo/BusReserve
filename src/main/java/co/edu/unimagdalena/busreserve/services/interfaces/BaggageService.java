package co.edu.unimagdalena.busreserve.services.interfaces;

import co.edu.unimagdalena.busreserve.api.dto.BaggageDtos.*;

import java.util.List;

public interface BaggageService {
    BaggageResponse create(BaggageCreateRequest req, Long ticketId);
    BaggageResponse get(Long id);
    BaggageResponse getByTagCode(String tagCode);
    List<BaggageResponse> listByTicket(Long ticketId);
    void delete(Long id);
}
