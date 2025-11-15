package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.BusDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Bus;
import co.edu.unimagdalena.busreserve.domine.repositories.BusRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.mapper.BusMapper;
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
class BusServiceImplTest {

    @Mock
    private BusRepository busRepo;
    @Spy
    private BusMapper mapper = Mappers.getMapper(BusMapper.class);
    @InjectMocks
    private BusServiceImpl service;

    @Test
    void create_shouldSaveAndReturnBus() {
        var req = new BusCreateRequest("ABC-123", 45, "{\"wifi\": true}");

        when(busRepo.save(any(Bus.class))).thenAnswer(inv -> {
            Bus b = inv.getArgument(0);
            b.setId(1L);
            return b;
        });

        BusResponse result = service.create(req);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.plate()).isEqualTo("ABC-123");
        assertThat(result.available()).isTrue();
        verify(busRepo).save(any(Bus.class));
    }

    @Test
    void get_shouldReturnBusWhenExists() {
        var bus = Bus.builder()
                .id(5L)
                .plate("XYZ-789")
                .capacity(50)
                .available(true)
                .build();

        when(busRepo.findById(5L)).thenReturn(Optional.of(bus));

        BusResponse result = service.get(5L);

        assertThat(result.id()).isEqualTo(5L);
        assertThat(result.plate()).isEqualTo("XYZ-789");
    }

    @Test
    void get_shouldThrowExceptionWhenNotFound() {
        when(busRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Bus 99 not found");
    }

    @Test
    void getByPlate_shouldReturnBusWhenExists() {
        var bus = Bus.builder()
                .id(3L)
                .plate("DEF-456")
                .capacity(40)
                .build();

        when(busRepo.findByPlate("DEF-456")).thenReturn(Optional.of(bus));

        BusResponse result = service.getByPlate("DEF-456");

        assertThat(result.id()).isEqualTo(3L);
        assertThat(result.plate()).isEqualTo("DEF-456");
    }

    @Test
    void findAvailable_shouldReturnOnlyAvailableBuses() {
        var buses = List.of(
                Bus.builder().id(1L).plate("A").available(true).build(),
                Bus.builder().id(2L).plate("B").available(true).build()
        );

        when(busRepo.findByAvailableTrue()).thenReturn(buses);

        List<BusResponse> result = service.findAvailable();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(BusResponse::available).containsOnly(true);
    }

    @Test
    void update_shouldPatchAndSave() {
        var bus = Bus.builder()
                .id(3L)
                .plate("OLD-PLATE")
                .capacity(40)
                .available(true)
                .build();

        when(busRepo.findById(3L)).thenReturn(Optional.of(bus));
        when(busRepo.save(any())).thenReturn(bus);

        var changes = new BusUpdateRequest(48, "{\"wifi\": true}", false);

        BusResponse result = service.update(3L, changes);

        assertThat(result.capacity()).isEqualTo(48);
        assertThat(result.available()).isFalse();
        verify(busRepo).save(bus);
    }

    @Test
    void setAvailability_shouldUpdateAvailableFlag() {
        var bus = Bus.builder()
                .id(5L)
                .plate("TEST-123")
                .available(true)
                .build();

        when(busRepo.findById(5L)).thenReturn(Optional.of(bus));

        service.setAvailability(5L, false);

        assertThat(bus.getAvailable()).isFalse();
        verify(busRepo).save(bus);
    }

    @Test
    void delete_shouldCallRepositoryWhenExists() {
        when(busRepo.existsById(7L)).thenReturn(true);

        service.delete(7L);

        verify(busRepo).deleteById(7L);
    }

    @Test
    void delete_shouldThrowExceptionWhenNotExists() {
        when(busRepo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(NotFoundException.class);

        verify(busRepo, never()).deleteById(any());
    }

    @Test
    void list_shouldReturnPagedResults() {
        var buses = List.of(
                Bus.builder().id(1L).plate("A").build(),
                Bus.builder().id(2L).plate("B").build()
        );
        var page = new PageImpl<>(buses);

        when(busRepo.findAll(any(Pageable.class))).thenReturn(page);

        Page<BusResponse> result = service.list(PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
    }
}