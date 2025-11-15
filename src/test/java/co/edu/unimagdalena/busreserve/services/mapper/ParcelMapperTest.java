package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.ParcelDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Parcel;
import co.edu.unimagdalena.busreserve.domine.entities.ParcelStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class ParcelMapperTest {

    private final ParcelMapper mapper = Mappers.getMapper(ParcelMapper.class);

    @Test
    void toEntity_shouldMapCreateRequestWithDefaults() {
        var req = new ParcelCreateRequest(
                "Ana Gomez", "+573001111111",
                "Pedro Lopez", "+573002222222",
                1L, 5L, 25000.0
        );
        
        Parcel entity = mapper.toEntity(req);
        
        assertThat(entity.getSenderName()).isEqualTo("Ana Gomez");
        assertThat(entity.getSenderPhone()).isEqualTo("+573001111111");
        assertThat(entity.getReceiverName()).isEqualTo("Pedro Lopez");
        assertThat(entity.getReceiverPhone()).isEqualTo("+573002222222");
        assertThat(entity.getFromStopId()).isEqualTo(1L);
        assertThat(entity.getToStopId()).isEqualTo(5L);
        assertThat(entity.getPrice()).isEqualTo(25000.0);
        assertThat(entity.getStatus()).isEqualTo(ParcelStatus.CREATED);
        assertThat(entity.getCreatedAt()).isNotNull();
    }

    @Test
    void toResponse_shouldMapEntity() {
        var entity = Parcel.builder()
                .id(10L)
                .code("PCL-12345")
                .senderName("Lucia Martinez")
                .senderPhone("+573003333333")
                .receiverName("Jorge Silva")
                .receiverPhone("+573004444444")
                .fromStopId(2L)
                .toStopId(6L)
                .price(30000.0)
                .status(ParcelStatus.IN_TRANSIT)
                .deliveryOtp("1234")
                .build();
        
        ParcelResponse dto = mapper.toResponse(entity);
        
        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.code()).isEqualTo("PCL-12345");
        assertThat(dto.status()).isEqualTo(ParcelStatus.IN_TRANSIT);
        assertThat(dto.deliveryOtp()).isEqualTo("1234");
    }

    @Test
    void patch_shouldOnlyUpdateStatusAndPhoto() {
        var entity = Parcel.builder()
                .id(8L)
                .code("PCL-999")
                .status(ParcelStatus.IN_TRANSIT)
                .proofPhotoUrl(null)
                .build();
        
        var changes = new ParcelUpdateRequest(ParcelStatus.DELIVERED, "https://cdn.example.com/proof.jpg");
        
        mapper.patch(entity, changes);
        
        assertThat(entity.getStatus()).isEqualTo(ParcelStatus.DELIVERED);
        assertThat(entity.getProofPhotoUrl()).isEqualTo("https://cdn.example.com/proof.jpg");
        assertThat(entity.getCode()).isEqualTo("PCL-999");
    }
}