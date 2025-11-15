package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.ParcelDtos.*;
        import co.edu.unimagdalena.busreserve.domine.entities.Parcel;
import co.edu.unimagdalena.busreserve.domine.entities.ParcelStatus;
import co.edu.unimagdalena.busreserve.domine.repositories.ParcelRepository;
import co.edu.unimagdalena.busreserve.services.mapper.ParcelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
        import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParcelServiceImplTest {

    @Mock
    private ParcelRepository parcelRepo;
    @Spy
    private ParcelMapper mapper = Mappers.getMapper(ParcelMapper.class);
    @InjectMocks
    private ParcelServiceImpl service;

    @Test
    void create_shouldGenerateCodeAndOtp() {
        var req = new ParcelCreateRequest(
                "Ana", "+573001111111",
                "Pedro", "+573002222222",
                1L, 5L, 25000.0
        );

        when(parcelRepo.save(any())).thenAnswer(inv -> {
            Parcel p = inv.getArgument(0);
            p.setId(10L);
            return p;
        });

        ParcelResponse result = service.create(req);

        assertThat(result.code()).startsWith("PCL-");
        assertThat(result.deliveryOtp()).hasSize(4);
        assertThat(result.status()).isEqualTo(ParcelStatus.CREATED);
        verify(parcelRepo).save(any(Parcel.class));
    }

    @Test
    void deliverParcel_shouldUpdateStatusWhenOtpIsValid() {
        var parcel = Parcel.builder()
                .id(5L)
                .code("PCL-ABC123")
                .deliveryOtp("1234")
                .status(ParcelStatus.IN_TRANSIT)
                .build();

        when(parcelRepo.findByCode("PCL-ABC123")).thenReturn(Optional.of(parcel));
        when(parcelRepo.save(any())).thenReturn(parcel);

        service.deliverParcel("PCL-ABC123", "1234", "https://photo.jpg");

        assertThat(parcel.getStatus()).isEqualTo(ParcelStatus.DELIVERED);
        assertThat(parcel.getProofPhotoUrl()).isEqualTo("https://photo.jpg");
        verify(parcelRepo).save(parcel);
    }

    @Test
    void deliverParcel_shouldThrowExceptionWhenOtpIsInvalid() {
        var parcel = Parcel.builder()
                .id(5L)
                .code("PCL-ABC123")
                .deliveryOtp("1234")
                .status(ParcelStatus.IN_TRANSIT)
                .build();

        when(parcelRepo.findByCode("PCL-ABC123")).thenReturn(Optional.of(parcel));

        assertThatThrownBy(() -> service.deliverParcel("PCL-ABC123", "9999", "https://photo.jpg"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid OTP");

        verify(parcelRepo, never()).save(any());
    }
}