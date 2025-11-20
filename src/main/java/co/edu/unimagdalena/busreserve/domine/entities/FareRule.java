package co.edu.unimagdalena.busreserve.domine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fareRules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FareRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "fromStopId", nullable = false)
    private Stop fromStop;

    @ManyToOne
    @JoinColumn(name = "toStopId", nullable = false)
    private Stop toStop;

    private Double basePrice;

    @Column(columnDefinition = "jsonb")
    private String discounts;

    private Boolean dynamicPricing;
}
