package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Baggage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BaggageRepository extends JpaRepository<Baggage,Long> {
    List<Baggage> findByTicketId(Long ticketId);
    Optional<Baggage> findByTagCode(String tagCode);
}
