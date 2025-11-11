package co.edu.unimagdalena.busreserve.domine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Long fromStopId;
    private Long toStopId;
    private Double price;

    @Enumerated(EnumType.STRING)
    private ParcelStatus status;

    private String proofPhotoUrl;
    private String deliveryOtp;

    private LocalDateTime createdAt;
}
