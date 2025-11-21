package co.edu.unimagdalena.busreserve.domine.entities;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;


import java.util.List;

@Entity
@Table(name = "buses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plate;
    private Integer capacity;

    @Builder.Default
    private Boolean available = true;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> amenities;

    @OneToMany(mappedBy = "bus")
    private List<Trip> trips;
}
