package co.edu.unimagdalena.busreserve.domine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private EntityType entityType; // TRIP, TICKET, PARCEL
    private Long entityId;

    private IncidentType type; // SECURITY, DELIVERY_FAIL, OVERBOOK, VEHICLE
    private String note;

    private LocalDateTime createdAt;
}
