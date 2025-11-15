package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Baggage;
import co.edu.unimagdalena.busreserve.domine.entities.Parcel;
import co.edu.unimagdalena.busreserve.domine.entities.ParcelStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParcelRepository extends JpaRepository<Parcel,Long> {
    Optional<Parcel> findByCode(String code);
    List<Parcel> findByFromStopIdAndStatus(Long fromStopId, ParcelStatus status);
    List<Parcel> findByToStopIdAndStatusIn(Long toStopId, List<ParcelStatus> statuses);
    List<Parcel> findByStatusAndTripId(ParcelStatus status, Long tripId);
}
