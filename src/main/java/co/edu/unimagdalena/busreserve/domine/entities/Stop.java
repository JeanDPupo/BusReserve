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


    private String name;
    private Integer stopOrder;
    private Double lat;
    private Double lng;

    // Tickets cuyo trayecto INICIA en esta parada
    @OneToMany(mappedBy = "fromStop")
    private Set<Ticket> ticketsFrom;

    // Tickets cuyo trayecto TERMINA en esta parada
    @OneToMany(mappedBy = "toStop")
    private Set<Ticket> ticketsTo;

    // Encomiendas enviadas desde esta parada
    @OneToMany(mappedBy = "fromStop")
    private Set<Parcel> parcelsFrom;

    // Encomiendas destinadas a esta parada
    @OneToMany(mappedBy = "toStop")
    private Set<Parcel> parcelsTo;

    // Tarifas aplicables DESDE esta parada
    @OneToMany(mappedBy = "fromStop")
    private Set<FareRule> fareRulesFrom;

    // Tarifas aplicables HASTA esta parada
    @OneToMany(mappedBy = "toStop")
    private Set<FareRule> fareRulesTo;

}
