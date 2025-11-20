package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Ticket;
import co.edu.unimagdalena.busreserve.domine.entities.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

    // Obtener tickets de un pasajero
    List<Ticket> findByPassengerId(Long passengerId);

    // Buscar tickets por estado
    List<Ticket> findByStatus(String status);

    // Buscar ticket por código QR
    Optional<Ticket> findByQrCode(String qrCode);
}
