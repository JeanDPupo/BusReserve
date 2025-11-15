package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.ParcelDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Parcel;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ParcelMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "status", constant = "CREATED")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "proofPhotoUrl", ignore = true)
    @Mapping(target = "deliveryOtp", ignore = true)
    Parcel toEntity(ParcelCreateRequest req);
    
    ParcelResponse toResponse(Parcel entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "fromStopId", ignore = true)
    @Mapping(target = "toStopId", ignore = true)
    @Mapping(target = "senderName", ignore = true)
    @Mapping(target = "senderPhone", ignore = true)
    @Mapping(target = "receiverName", ignore = true)
    @Mapping(target = "receiverPhone", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "deliveryOtp", ignore = true)
    void patch(@MappingTarget Parcel target, ParcelUpdateRequest changes);
}
