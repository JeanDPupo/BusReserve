package co.edu.unimagdalena.busreserve.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class ConfigDtos {

    public record ConfigCreateRequest(
            @NotBlank String keyName,
            @NotBlank String value
    ) implements Serializable {}

    public record ConfigUpdateRequest(
            @NotBlank String value
    ) implements Serializable {}

    public record ConfigResponse(
            Long id,
            String keyName,
            String value
    ) implements Serializable {}
}