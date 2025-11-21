package co.edu.unimagdalena.busreserve.services.interfaces;

import co.edu.unimagdalena.busreserve.api.dto.TripDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.TripStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TripService {

    TripResponse create(TripCreateRequest req);
    TripResponse get(Long id);
    Page<TripResponse> list(Pageable pageable);
    List<TripResponse> findAvailableTrips(String origin, String destination, LocalDateTime date);
    List<TripResponse> findByStatus(TripStatus status);
    TripResponse update(Long id, TripUpdateRequest req);
    void delete(Long id);
    void updateStatus(Long id, TripStatus status);
    void openBoarding(Long id);
    void closeBoarding(Long id);
    void depart(Long id);
}
