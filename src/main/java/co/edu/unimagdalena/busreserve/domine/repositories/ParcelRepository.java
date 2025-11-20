package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Baggage;
import co.edu.unimagdalena.busreserve.domine.entities.Parcel;
import co.edu.unimagdalena.busreserve.domine.entities.ParcelStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParcelRepository extends JpaRepository<Parcel,Long> {
    // Crear una encomienda
    Parcel save(Parcel parcel);

    // Buscar encomienda por código
    Optional<Parcel> findByCode(String code);

    // Buscar encomiendas por estado (IN_TRANSIT, DELIVERED, FAILED)
    List<Parcel> findByStatus(String status);
}
