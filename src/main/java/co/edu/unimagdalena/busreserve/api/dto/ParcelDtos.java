package co.edu.unimagdalena.busreserve.api.dto;

import co.edu.unimagdalena.busreserve.domine.entities.ParcelStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParcelDtos {

    public record ParcelCreateRequest(
            @NotBlank String senderName,
            @NotBlank String senderPhone,
            @NotBlank String receiverName,
            @NotBlank String receiverPhone,
            @NotNull Long fromStopId,
            @NotNull Long toStopId
            // price and code removed (calculated/generated server-side)
    ) implements Serializable {}

    public record ParcelUpdateRequest(
            ParcelStatus status,
            String proofPhotoUrl
    ) implements Serializable {}

    public record ParcelResponse(
            Long id,
            String code,
            String senderName,
            String senderPhone,
            String receiverName,
            String receiverPhone,
            Long fromStopId,
            Long toStopId,
            BigDecimal price,
            ParcelStatus status,
            String proofPhotoUrl,
            String deliveryOtp,
            LocalDateTime createdAt
    ) implements Serializable {}
}
