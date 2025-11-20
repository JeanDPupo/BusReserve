package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Stop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StopRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private StopRepository stopRepository;

    @Test
    void shouldCreateAndFindStopByCode() {
        // arrange
        Stop bog = new Stop();
        bog.setId(1L);
        bog.setName("Bogotá");
        Stop saved = stopRepository.save(bog);

        // act
        var found = stopRepository.findById(1L);

        // assert
        assertThat(saved.getId()).isNotNull();
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Bogotá");
    }
}