package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.AssignmentDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Assignment;
import co.edu.unimagdalena.busreserve.domine.entities.Role;
import co.edu.unimagdalena.busreserve.domine.entities.Trip;
import co.edu.unimagdalena.busreserve.domine.entities.User;
import co.edu.unimagdalena.busreserve.domine.repositories.AssigmentRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.TripRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.UserRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.mapper.AssignmentMapper;
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
class AssignmentServiceImplTest {

    @Mock
    private AssigmentRepository assignmentRepo;
    @Mock
    private TripRepository tripRepo;
    @Mock
    private UserRepository userRepo;
    @Spy
    private AssignmentMapper mapper = Mappers.getMapper(AssignmentMapper.class);
    @InjectMocks
    private AssignmentServiceImpl service;

    @Test
    void create_shouldCreateAssignmentWhenValidRoles() {
        var req = new AssignmentCreateRequest(1L, 2L, 3L, false);
        var trip = Trip.builder().id(1L).build();
        var driver = User.builder().id(2L).role(Role.DRIVER).name("Driver").build();
        var dispatcher = User.builder().id(3L).role(Role.DISPATCHER).name("Dispatcher").build();

        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));
        when(userRepo.findById(2L)).thenReturn(Optional.of(driver));
        when(userRepo.findById(3L)).thenReturn(Optional.of(dispatcher));
        when(assignmentRepo.save(any())).thenAnswer(inv -> {
            Assignment a = inv.getArgument(0);
            a.setId(10L);
            return a;
        });

        AssignmentResponse result = service.create(req);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.driverName()).isEqualTo("Driver");
        assertThat(result.dispatcherName()).isEqualTo("Dispatcher");
        verify(assignmentRepo).save(any(Assignment.class));
    }

    @Test
    void create_shouldThrowExceptionWhenTripNotFound() {
        var req = new AssignmentCreateRequest(99L, 2L, 3L, false);

        when(tripRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Trip 99 not found");

        verify(assignmentRepo, never()).save(any());
    }

    @Test
    void create_shouldThrowExceptionWhenDriverNotDriver() {
        var req = new AssignmentCreateRequest(1L, 2L, 3L, false);
        var trip = Trip.builder().id(1L).build();
        var notDriver = User.builder().id(2L).role(Role.PASSENGER).build();

        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));
        when(userRepo.findById(2L)).thenReturn(Optional.of(notDriver));

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("must be a driver");

        verify(assignmentRepo, never()).save(any());
    }

    @Test
    void create_shouldThrowExceptionWhenDispatcherNotDispatcher() {
        var req = new AssignmentCreateRequest(1L, 2L, 3L, false);
        var trip = Trip.builder().id(1L).build();
        var driver = User.builder().id(2L).role(Role.DRIVER).build();
        var notDispatcher = User.builder().id(3L).role(Role.CLERK).build();

        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));
        when(userRepo.findById(2L)).thenReturn(Optional.of(driver));
        when(userRepo.findById(3L)).thenReturn(Optional.of(notDispatcher));

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("must be a dispatcher");

        verify(assignmentRepo, never()).save(any());
    }

    @Test
    void getByTrip_shouldReturnAssignmentWhenExists() {
        var assignment = Assignment.builder()
                .id(5L)
                .trip(Trip.builder().id(10L).build())
                .driver(User.builder().id(2L).name("Driver").build())
                .dispatcher(User.builder().id(3L).name("Dispatcher").build())
                .build();

        when(assignmentRepo.findByTripId(10L)).thenReturn(Optional.of(assignment));

        AssignmentResponse result = service.getByTrip(10L);

        assertThat(result.id()).isEqualTo(5L);
        assertThat(result.tripId()).isEqualTo(10L);
    }

    @Test
    void findByDriver_shouldReturnAssignmentsForDriver() {
        var assignments = List.of(
                Assignment.builder().id(1L).driver(User.builder().id(5L).name("Driver").build())
                        .dispatcher(User.builder().id(2L).name("Disp").build()).build(),
                Assignment.builder().id(2L).driver(User.builder().id(5L).name("Driver").build())
                        .dispatcher(User.builder().id(2L).name("Disp").build()).build()
        );

        when(assignmentRepo.findByDriverId(5L)).thenReturn(assignments);

        List<AssignmentResponse> result = service.findByDriver(5L);

        assertThat(result).hasSize(2);
    }

    @Test
    void approveChecklist_shouldSetChecklistOkToTrue() {
        var assignment = Assignment.builder()
                .id(5L)
                .checklistOk(false)
                .build();

        when(assignmentRepo.findById(5L)).thenReturn(Optional.of(assignment));

        service.approveChecklist(5L);

        assertThat(assignment.getChecklistOk()).isTrue();
        verify(assignmentRepo).save(assignment);
    }

    @Test
    void update_shouldPatchAndSave() {
        var assignment = Assignment.builder()
                .id(5L)
                .checklistOk(false)
                .build();

        when(assignmentRepo.findById(5L)).thenReturn(Optional.of(assignment));
        when(assignmentRepo.save(any())).thenReturn(assignment);

        var changes = new AssignmentUpdateRequest(true);

        AssignmentResponse result = service.update(5L, changes);

        assertThat(result.checklistOk()).isTrue();
        verify(assignmentRepo).save(assignment);
    }

    @Test
    void delete_shouldCallRepositoryWhenExists() {
        when(assignmentRepo.existsById(7L)).thenReturn(true);

        service.delete(7L);

        verify(assignmentRepo).deleteById(7L);
    }
}