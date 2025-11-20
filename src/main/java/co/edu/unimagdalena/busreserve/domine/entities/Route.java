package co.edu.unimagdalena.busreserve.domine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    @ManyToOne
    @JoinColumn(name = "origin_id", nullable = false)
    private Stop origin;  // Relación con Stop como origen

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private Stop destination;  // Relación con Stop como destino

    private Double distanceKm;
    private Integer durationMin;

    @OneToMany(mappedBy = "route")
    private List<Stop> stops = new ArrayList<>();
}
