package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.FareRuleDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.FareRule;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FareRuleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "fromStop", ignore = true)
    @Mapping(target = "toStop", ignore = true)
    FareRule toEntity(FareRuleCreateRequest req);

    @Mapping(target = "routeId",     source = "route.id")
    @Mapping(target = "fromStopId",  source = "fromStop.id")
    @Mapping(target = "toStopId",    source = "toStop.id")
    FareRuleResponse toResponse(FareRule entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "fromStop", ignore = true)
    @Mapping(target = "toStop", ignore = true)
    void patch(@MappingTarget FareRule target, FareRuleUpdateRequest changes);
}