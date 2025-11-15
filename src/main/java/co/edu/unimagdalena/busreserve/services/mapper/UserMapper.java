package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.UserDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "true")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "passwordHash", source = "password")
    User toEntity(UserCreateRequest req);
    
    UserResponse toResponse(User entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore =  true)
    void patch(@MappingTarget User target, UserUpdateRequest changes);
}
