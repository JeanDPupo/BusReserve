package co.edu.unimagdalena.busreserve.domine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "stops")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @OneToMany(mappedBy = "origin")
    private List<Route> origins = new ArrayList<>();

    @OneToMany(mappedBy = "destination")
    private List<Route> destinations = new ArrayList<>();

    private String name;
    private Integer stopOrder;
    private Double lat;
    private Double lng;

    // Relación OneToMany para FareRule como origen (fromStop)
    @OneToMany(mappedBy = "fromStop")
    private Set<FareRule> fromFareRules;

    // Relación OneToMany para FareRule como destino (toStop)
    @OneToMany(mappedBy = "toStop")
    private Set<FareRule> toFareRules;

    // Relación OneToMany para Parcel como lugar de origen (fromStop)
    @OneToMany(mappedBy = "fromStop")
    private Set<Parcel> parcelsFrom;

    // Relación OneToMany para Parcel como lugar de destino (toStop)
    @OneToMany(mappedBy = "toStop")
    private Set<Parcel> parcelsTo;

    @OneToMany(mappedBy = "fromStop")
    private Set<Ticket> ticketsFrom;

    // Relación OneToMany para Parcel como lugar de destino (toStop)
    @OneToMany(mappedBy = "toStop")
    private Set<Ticket> ticketsTo;
}
