package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.HoldStatus;
import co.edu.unimagdalena.busreserve.domine.entities.SeatHold;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeatHoldRepository extends JpaRepository<SeatHold,Long> {
    // ============================================================
    // 1. Verificar si un asiento ya está reservado temporalmente
    //    Evita doble venta mientras el usuario compra
    // ============================================================
    Optional<SeatHold> findByTripIdAndSeatNumber(Long tripId, Integer seatNumber);


    // ============================================================
    // 2. Buscar holds activos para un usuario
    //    Útil para reanudar compra y mostrar "mis reservas temporales"
    // ============================================================
    List<SeatHold> findByUserIdAndStatus(Long userId, HoldStatus status);


    // ============================================================
    // 3. Buscar holds activos de un viaje
    //    Necesario para mostrar asientos ocupados en la UI y panel
    // ============================================================
    List<SeatHold> findByTripIdAndStatus(Long tripId, HoldStatus status);


    // ============================================================
    // 4. Buscar holds expirados para limpiar automáticamente
    //
    //    Esto sirve para:
    //     - Tarea @Scheduled
    //     - Liberar sillas
    //
    //    IMPORTANTE: Solo funciona si SeatHold tiene expiresAt.
    // ============================================================
    List<SeatHold> findByStatusAndExpiresAtBefore(
            HoldStatus status,
            LocalDateTime expiresAt
    );


    // ============================================================
    // 5. Buscar holds de un usuario en un viaje específico
    //    Útil para bloquear múltiples asientos en la compra
    // ============================================================
    List<SeatHold> findByUserIdAndTripIdAndStatus(Long userId, Long tripId, HoldStatus status);

}
