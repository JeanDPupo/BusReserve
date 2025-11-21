package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Parcel;
import co.edu.unimagdalena.busreserve.domine.entities.ParcelStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParcelRepository extends JpaRepository<Parcel,Long> {

    Optional<Parcel> findByCode(String code);

    List<Parcel> findByStatus(ParcelStatus status);

    List<Parcel> findByFromStopId(Long fromStopId);

    List<Parcel> findByToStopId(Long toStopId);

    List<Parcel> findByFromStopIdAndToStopId(Long fromStopId, Long toStopId);
}
