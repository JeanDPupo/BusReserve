package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.ConfigDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Config;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ConfigMapper {

    @Mapping(target = "id", ignore = true)
    Config toEntity(ConfigCreateRequest req);

    ConfigResponse toResponse(Config config);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keyName", ignore = true)
    void patch(@MappingTarget Config target, ConfigUpdateRequest changes);
}
