package co.edu.unimagdalena.busreserve.api.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class FareRuleDtos {

    public record FareRuleCreateRequest(
            Long routeId,
            Long fromStopId,
            Long toStopId,
            BigDecimal basePrice,
            List<String> discounts,
            Boolean dynamicPricing
    ) implements Serializable {}

    public record FareRuleUpdateRequest(
            BigDecimal basePrice,
            List<String> discounts,
            Boolean dynamicPricing
    ) implements Serializable {}

    public record FareRuleResponse(
            Long id,
            Long routeId,
            Long fromStopId,
            Long toStopId,
            BigDecimal basePrice,
            List<String> discounts,
            Boolean dynamicPricing
    ) implements Serializable {}
}