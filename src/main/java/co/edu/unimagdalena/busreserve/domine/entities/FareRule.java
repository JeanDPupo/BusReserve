package co.edu.unimagdalena.busreserve.domine.entities;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.List;

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
    @JoinColumn(name = "fromStop_id")
    private Stop fromStop;


    @ManyToOne
    @JoinColumn(name = "toStop_id")
    private Stop toStop;

    private BigDecimal basePrice;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> discounts;

    private Boolean dynamicPricing;
}