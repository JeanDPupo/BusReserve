package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.FareRuleDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.FareRule;
import co.edu.unimagdalena.busreserve.domine.entities.Route;
import co.edu.unimagdalena.busreserve.domine.repositories.FareRuleRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.mapper.FareRuleMapper;
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
class FareRuleServiceImplTest {

    @Mock
    private FareRuleRepository fareRuleRepo;
    @Mock
    private RouteRepository routeRepo;
    @Spy
    private FareRuleMapper mapper = Mappers.getMapper(FareRuleMapper.class);
    @InjectMocks
    private FareRuleServiceImpl service;

    @Test
    void create_shouldCreateFareRuleWhenRouteExists() {
        var req = new FareRuleCreateRequest(1L, 2L, 5L, 50000.0, null, false);
        var route = Route.builder().id(1L).code("STMR-BGT").build();

        when(routeRepo.findById(1L)).thenReturn(Optional.of(route));
        when(fareRuleRepo.save(any())).thenAnswer(inv -> {
            FareRule fr = inv.getArgument(0);
            fr.setId(10L);
            return fr;
        });

        FareRuleResponse result = service.create(req);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.basePrice()).isEqualTo(50000.0);
        verify(fareRuleRepo).save(any(FareRule.class));
    }

    @Test
    void create_shouldThrowExceptionWhenRouteNotFound() {
        var req = new FareRuleCreateRequest(99L, 1L, 2L, 50000.0, null, false);

        when(routeRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Route 99 not found");

        verify(fareRuleRepo, never()).save(any());
    }

    @Test
    void get_shouldReturnFareRuleWhenExists() {
        var route = Route.builder().id(5L).build();
        var fareRule = FareRule.builder()
                .id(10L)
                .route(route)
                .fromStopId(1L)
                .toStopId(3L)
                .basePrice(60000.0)
                .build();

        when(fareRuleRepo.findById(10L)).thenReturn(Optional.of(fareRule));

        FareRuleResponse result = service.get(10L);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.basePrice()).isEqualTo(60000.0);
    }

    @Test
    void listByRoute_shouldReturnFareRulesForRoute() {
        var route = Route.builder().id(1L).build();
        var fareRules = List.of(
                FareRule.builder().id(1L).route(route).basePrice(50000.0).build(),
                FareRule.builder().id(2L).route(route).basePrice(70000.0).build()
        );

        when(fareRuleRepo.findByRouteId(1L)).thenReturn(fareRules);

        List<FareRuleResponse> result = service.listByRoute(1L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(FareRuleResponse::basePrice)
                .contains(50000.0, 70000.0);
    }

    @Test
    void findByRouteAndStops_shouldReturnFareRuleWhenExists() {
        var route = Route.builder().id(1L).build();
        var fareRule = FareRule.builder()
                .id(5L)
                .route(route)
                .fromStopId(2L)
                .toStopId(5L)
                .basePrice(55000.0)
                .build();

        when(fareRuleRepo.findByRouteAndStops(1L, 2L, 5L))
                .thenReturn(Optional.of(fareRule));

        FareRuleResponse result = service.findByRouteAndStops(1L, 2L, 5L);

        assertThat(result.id()).isEqualTo(5L);
        assertThat(result.fromStopId()).isEqualTo(2L);
        assertThat(result.toStopId()).isEqualTo(5L);
    }

    @Test
    void calculatePrice_shouldReturnBasePriceWhenNoDynamicPricing() {
        var route = Route.builder().id(1L).build();
        var fareRule = FareRule.builder()
                .route(route)
                .basePrice(50000.0)
                .dynamicPricing(false)
                .build();

        when(fareRuleRepo.findByRouteAndStops(1L, 2L, 5L))
                .thenReturn(Optional.of(fareRule));

        Double result = service.calculatePrice(1L, 2L, 5L);

        assertThat(result).isEqualTo(50000.0);
    }

    @Test
    void calculatePrice_shouldApplyDynamicPricingMultiplier() {
        var route = Route.builder().id(1L).build();
        var fareRule = FareRule.builder()
                .route(route)
                .basePrice(50000.0)
                .dynamicPricing(true)
                .build();

        when(fareRuleRepo.findByRouteAndStops(1L, 2L, 5L))
                .thenReturn(Optional.of(fareRule));

        Double result = service.calculatePrice(1L, 2L, 5L);

        assertThat(result).isEqualTo(60000.0);
    }

    @Test
    void calculatePrice_shouldReturnDefaultWhenNoFareRuleFound() {
        when(fareRuleRepo.findByRouteAndStops(1L, 2L, 5L))
                .thenReturn(Optional.empty());

        Double result = service.calculatePrice(1L, 2L, 5L);

        assertThat(result).isEqualTo(50000.0);
    }

    @Test
    void update_shouldPatchAndSave() {
        var fareRule = FareRule.builder()
                .id(5L)
                .basePrice(60000.0)
                .dynamicPricing(false)
                .build();

        when(fareRuleRepo.findById(5L)).thenReturn(Optional.of(fareRule));
        when(fareRuleRepo.save(any())).thenReturn(fareRule);

        var changes = new FareRuleUpdateRequest(65000.0, null, true);

        FareRuleResponse result = service.update(5L, changes);

        assertThat(result.basePrice()).isEqualTo(65000.0);
        assertThat(result.dynamicPricing()).isTrue();
        verify(fareRuleRepo).save(fareRule);
    }

    @Test
    void delete_shouldCallRepositoryWhenExists() {
        when(fareRuleRepo.existsById(7L)).thenReturn(true);

        service.delete(7L);

        verify(fareRuleRepo).deleteById(7L);
    }

    @Test
    void delete_shouldThrowExceptionWhenNotExists() {
        when(fareRuleRepo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(NotFoundException.class);

        verify(fareRuleRepo, never()).deleteById(any());
    }
}