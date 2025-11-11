package co.edu.unimagdalena.busreserve.domine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "baggages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Baggage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private Double weightKg;
    private Double fee;
    private String tagCode;
}