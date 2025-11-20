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

    private String origin;
    private String destination;

    private Double distanceKm;
    private Integer durationMin;

    @OneToMany(mappedBy = "route")
    private List<Stop> stops = new ArrayList<>();
}
