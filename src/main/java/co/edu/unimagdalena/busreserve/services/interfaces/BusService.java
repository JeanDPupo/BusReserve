package co.edu.unimagdalena.busreserve.services.interfaces;

import co.edu.unimagdalena.busreserve.api.dto.BusDtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BusService {
    BusResponse create(BusCreateRequest req);
    BusResponse get(Long id);
    BusResponse getByPlate(String plate);
    Page<BusResponse> list(Pageable pageable);
    List<BusResponse> findAvailable();
    BusResponse update(Long id, BusUpdateRequest req);
    void delete(Long id);
    void setAvailability(Long id, boolean available);
}
