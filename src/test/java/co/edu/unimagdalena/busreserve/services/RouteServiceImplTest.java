package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.RouteDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Route;
import co.edu.unimagdalena.busreserve.domine.repositories.RouteRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.mapper.RouteMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteServiceImplTest {

    @Mock
    private RouteRepository routeRepo;

    @Spy
    private RouteMapper mapper = Mappers.getMapper(RouteMapper.class);

    @InjectMocks
    private RouteServiceImpl service;

    @Test
    void create_shouldSaveAndReturnRoute() {
        var req = new RouteCreateRequest("STMR-BGT", "Santa Marta - Bogotá",
                "Santa Marta", "Bogotá", 950.0, 900);

        when(routeRepo.save(any(Route.class))).thenAnswer(inv -> {
            Route r = inv.getArgument(0);
            r.setId(1L);
            return r;
        });

        RouteResponse result = service.create(req);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.code()).isEqualTo("STMR-BGT");
        assertThat(result.origin()).isEqualTo("Santa Marta");
        verify(routeRepo).save(any(Route.class));
    }

    @Test
    void get_shouldReturnRouteWhenExists() {
        var route = Route.builder()
                .id(5L)
                .code("STMR-CTG")
                .name("Santa Marta - Cartagena")
                .origin("Santa Marta")
                .destination("Cartagena")
                .build();

        when(routeRepo.findById(5L)).thenReturn(Optional.of(route));

        RouteResponse result = service.get(5L);

        assertThat(result.id()).isEqualTo(5L);
        assertThat(result.code()).isEqualTo("STMR-CTG");
    }

    @Test
    void get_shouldThrowExceptionWhenNotFound() {
        when(routeRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Route 99 not found");
    }

    @Test
    void update_shouldPatchAndSave() {
        var route = Route.builder()
                .id(3L)
                .code("OLD")
                .name("Old Name")
                .origin("Origin")
                .destination("Dest")
                .build();

        when(routeRepo.findById(3L)).thenReturn(Optional.of(route));
        when(routeRepo.save(any())).thenReturn(route);

        var changes = new RouteUpdateRequest("New Name", null, null, null, null);

        RouteResponse result = service.update(3L, changes);

        assertThat(result.name()).isEqualTo("New Name");
        verify(routeRepo).save(route);
    }

    @Test
    void list_shouldReturnPagedResults() {
        var routes = List.of(
                Route.builder().id(1L).code("R1").build(),
                Route.builder().id(2L).code("R2").build()
        );
        var page = new PageImpl<>(routes);

        when(routeRepo.findAll(any(Pageable.class))).thenReturn(page);

        Page<RouteResponse> result = service.list(PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    void delete_shouldCallRepositoryWhenExists() {
        when(routeRepo.existsById(7L)).thenReturn(true);

        service.delete(7L);

        verify(routeRepo).deleteById(7L);
    }

    @Test
    void delete_shouldThrowExceptionWhenNotExists() {
        when(routeRepo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(NotFoundException.class);

        verify(routeRepo, never()).deleteById(any());
    }
}
