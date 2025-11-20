package co.edu.unimagdalena.busreserve.domine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne @JoinColumn(name = "dispatcher_id")
    private User dispatcher;

    private Boolean checklistOk;
    private LocalDateTime assignedAt;
}
