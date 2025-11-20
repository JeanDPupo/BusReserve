package co.edu.unimagdalena.busreserve.domine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private User passenger;

    private String seatNumber;
    @ManyToOne
    @JoinColumn(name = "fromStopId", nullable = false)
    private Stop fromStop;

    @ManyToOne
    @JoinColumn(name = "toStopId", nullable = false)
    private Stop toStop;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private String qrCode;
}
