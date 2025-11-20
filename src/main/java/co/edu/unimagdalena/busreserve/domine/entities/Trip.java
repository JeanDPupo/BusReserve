package co.edu.unimagdalena.busreserve.domine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "trips")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

    private LocalDate date;
    private LocalDateTime departureAt;
    private LocalDateTime arrivalEta;

    @Enumerated(EnumType.STRING)
    private TripStatus status;

    @OneToMany(mappedBy = "trip")
    private List<Ticket> tickets;
}