package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.StopDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Route;
import co.edu.unimagdalena.busreserve.domine.entities.Stop;
import co.edu.unimagdalena.busreserve.domine.repositories.RouteRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.StopRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.mapper.StopMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StopServiceImplTest {

    @Mock
    private StopRepository stopRepo;
    @Mock
    private RouteRepository routeRepo;
    @Spy
    private StopMapper mapper = Mappers.getMapper(StopMapper.class);
    @InjectMocks
    private StopServiceImpl service;

    @Test
    void create_shouldCreateStopWhenRouteExists() {
        var req = new StopCreateRequest(1L, "Barranquilla Terminal", 1, 10.9685, -74.7813);
        var route = Route.builder().id(1L).code("STMR-BGT").build();

        when(routeRepo.findById(1L)).thenReturn(Optional.of(route));
        when(stopRepo.save(any())).thenAnswer(inv -> {
            Stop s = inv.getArgument(0);
            s.setId(10L);
            return s;
        });

        StopResponse result = service.create(req);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.name()).isEqualTo("Barranquilla Terminal");
        verify(stopRepo).save(any(Stop.class));
    }

    @Test
    void create_shouldThrowExceptionWhenRouteNotFound() {
        var req = new StopCreateRequest(99L, "Test Stop", 1, 10.0, -74.0);

        when(routeRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Route 99 not found");

        verify(stopRepo, never()).save(any());
    }

    @Test
    void get_shouldReturnStopWhenExists() {
        var route = Route.builder().id(5L).code("STMR-CTG").build();
        var stop = Stop.builder()
                .id(10L)
                .route(route)
                .name("Fundación")
                .orderIndex(2)
                .build();

        when(stopRepo.findById(10L)).thenReturn(Optional.of(stop));

        StopResponse result = service.get(10L);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.name()).isEqualTo("Fundación");
    }

    @Test
    void get_shouldThrowExceptionWhenNotFound() {
        when(stopRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Stop 99 not found");
    }

    @Test
    void listByRoute_shouldReturnStopsOrderedByOrder() {
        var route = Route.builder().id(1L).build();
        var stops = List.of(
                Stop.builder().id(1L).route(route).name("Stop A").orderIndex(1).build(),
                Stop.builder().id(2L).route(route).name("Stop B").orderIndex(2).build()
        );

        when(routeRepo.existsById(1L)).thenReturn(true);
        when(stopRepo.findByRouteIdOrderByOrderAsc(1L)).thenReturn(stops);

        List<StopResponse> result = service.listByRoute(1L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(StopResponse::name).containsExactly("Stop A", "Stop B");
    }

    @Test
    void listByRoute_shouldThrowExceptionWhenRouteNotFound() {
        when(routeRepo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.listByRoute(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void update_shouldPatchAndSave() {
        var stop = Stop.builder()
                .id(5L)
                .name("Old Name")
                .orderIndex(1)
                .lat(10.0)
                .lng(-74.0)
                .build();

        when(stopRepo.findById(5L)).thenReturn(Optional.of(stop));
        when(stopRepo.save(any())).thenReturn(stop);

        var changes = new StopUpdateRequest("New Name", null, 11.0, null);

        StopResponse result = service.update(5L, changes);

        assertThat(result.name()).isEqualTo("New Name");
        assertThat(result.lat()).isEqualTo(11.0);
        verify(stopRepo).save(stop);
    }

    @Test
    void delete_shouldCallRepositoryWhenExists() {
        when(stopRepo.existsById(7L)).thenReturn(true);

        service.delete(7L);

        verify(stopRepo).deleteById(7L);
    }

    @Test
    void delete_shouldThrowExceptionWhenNotExists() {
        when(stopRepo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(NotFoundException.class);

        verify(stopRepo, never()).deleteById(any());
    }
}