package co.edu.unimagdalena.busreserve.api.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class FareRuleDtos {
    public record FareRuleCreateRequest(
            @NotNull Long routeId,
            @NotNull Long fromStopId,
            @NotNull Long toStopId,
            @NotNull Double basePrice,
            String discounts,
            Boolean dynamicPricing
    ) implements Serializable {}

    public record FareRuleUpdateRequest(
            Double basePrice,
            String discounts,
            Boolean dynamicPricing
    ) implements Serializable {}

    public record FareRuleResponse(
            Long id,
            Long routeId,
            Long fromStopId,
            Long toStopId,
            Double basePrice,
            String discounts,
            Boolean dynamicPricing
    ) implements Serializable {}
}
