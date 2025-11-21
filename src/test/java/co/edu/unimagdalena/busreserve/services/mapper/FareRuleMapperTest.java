package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.FareRuleDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.FareRule;
import co.edu.unimagdalena.busreserve.domine.entities.Route;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class FareRuleMapperTest {
;
    private final FareRuleMapper mapper = Mappers.getMapper(FareRuleMapper.class);

    @Test
    void toEntity_shouldMapCreateRequest() {
        var req = new FareRuleCreateRequest(1L, 2L, 5L, 50000.0, "{\"student\": 0.1}", true);
        
        FareRule entity = mapper.toEntity(req);
        
        assertThat(entity.getFromStopId()).isEqualTo(2L);
        assertThat(entity.getToStopId()).isEqualTo(5L);
        assertThat(entity.getBasePrice()).isEqualTo(50000.0);
        assertThat(entity.getDiscounts()).isEqualTo("{\"student\": 0.1}");
        assertThat(entity.getDynamicPricing()).isTrue();
    }

    @Test
    void toResponse_shouldMapEntityWithRouteId() {
        var route = Route.builder().id(3L).code("STMR-BGT").build();
        var entity = FareRule.builder()
                .id(10L)
                .route(route)
                .fromStopId(1L)
                .toStopId(4L)
                .basePrice(75000.0)
                .discounts("{\"senior\": 0.15}")
                .dynamicPricing(false)
                .build();
        
        FareRuleResponse dto = mapper.toResponse(entity);
        
        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.routeId()).isEqualTo(3L);
        assertThat(dto.fromStopId()).isEqualTo(1L);
        assertThat(dto.toStopId()).isEqualTo(4L);
        assertThat(dto.basePrice()).isEqualTo(75000.0);
        assertThat(dto.dynamicPricing()).isFalse();
    }

    @Test
    void patch_shouldUpdateOnlyProvidedFields() {
        var entity = FareRule.builder()
                .id(5L)
                .fromStopId(1L)
                .toStopId(3L)
                .basePrice(60000.0)
                .discounts(null)
                .dynamicPricing(false)
                .build();
        
        var changes = new FareRuleUpdateRequest(65000.0, "{\"student\": 0.2}", null);
        
        mapper.patch(entity, changes);
        
        assertThat(entity.getBasePrice()).isEqualTo(65000.0);
        assertThat(entity.getDiscounts()).isEqualTo("{\"student\": 0.2}");
        assertThat(entity.getDynamicPricing()).isFalse();
        assertThat(entity.getFromStopId()).isEqualTo(1L);
    }
}