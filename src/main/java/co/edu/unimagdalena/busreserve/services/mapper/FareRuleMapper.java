package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.FareRuleDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.FareRule;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FareRuleMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    FareRule toEntity(FareRuleCreateRequest req);
    
    @Mapping(target = "routeId", source = "route.id")
    FareRuleResponse toResponse(FareRule entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "fromStopId", ignore = true)
    @Mapping(target = "toStopId", ignore = true)
    void patch(@MappingTarget FareRule target, FareRuleUpdateRequest changes);
}
