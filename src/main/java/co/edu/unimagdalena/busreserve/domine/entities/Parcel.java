package co.edu.unimagdalena.busreserve.domine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "parcels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String senderName;
    private String senderPhone;
    private String receiverName;
    private String receiverPhone;

    @ManyToOne
    @JoinColumn(name = "from_stop_id")
    private Stop  fromStop;

    @ManyToOne
    @JoinColumn(name = "to_stop_id")
    private Stop toStop;

    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private ParcelStatus status;  // Enum con los valores: CREATED, IN_TRANSIT, DELIVERED, FAILED

    private String proofPhotoUrl;
    private String deliveryOtp;
}
