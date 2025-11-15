package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.UserDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Role;
import co.edu.unimagdalena.busreserve.domine.entities.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toEntity_shouldMapCreateRequest() {
        var req = new UserCreateRequest("Juan Perez", "juan@test.com", "+573001234567", Role.PASSENGER, "password123");
        
        User entity = mapper.toEntity(req);
        
        assertThat(entity.getName()).isEqualTo("Juan Perez");
        assertThat(entity.getEmail()).isEqualTo("juan@test.com");
        assertThat(entity.getPhone()).isEqualTo("+573001234567");
        assertThat(entity.getRole()).isEqualTo(Role.PASSENGER);
        assertThat(entity.getStatus()).isTrue();
        assertThat(entity.getCreatedAt()).isNotNull();
    }

    @Test
    void toResponse_shouldMapEntity() {
        var entity = User.builder()
                .id(1L)
                .name("Maria Lopez")
                .email("maria@test.com")
                .phone("+573007654321")
                .role(Role.DRIVER)
                .status(true)
                .createdAt(LocalDateTime.now())
                .build();
        
        UserResponse dto = mapper.toResponse(entity);
        
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Maria Lopez");
        assertThat(dto.email()).isEqualTo("maria@test.com");
        assertThat(dto.role()).isEqualTo(Role.DRIVER);
        assertThat(dto.status()).isTrue();
    }

    @Test
    void patch_shouldIgnoreNullsAndImmutableFields() {
        var entity = User.builder()
                .id(5L)
                .name("Old Name")
                .email("old@test.com")
                .phone("+573001111111")
                .role(Role.CLERK)
                .passwordHash("hashedpass")
                .createdAt(LocalDateTime.now().minusDays(10))
                .build();
        
        var changes = new UserUpdateRequest("New Name", null, "+573002222222");
        
        mapper.patch(entity, changes);
        
        assertThat(entity.getName()).isEqualTo("New Name");
        assertThat(entity.getEmail()).isEqualTo("old@test.com");
        assertThat(entity.getPhone()).isEqualTo("+573002222222");
        assertThat(entity.getRole()).isEqualTo(Role.CLERK);
        assertThat(entity.getPasswordHash()).isEqualTo("hashedpass");
    }
}
