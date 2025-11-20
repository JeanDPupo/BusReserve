package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigRepositoryTest extends AbstractRepositoryIT{
    @Autowired
    private ConfigRepository configRepository;

    private Config configA;
    private Config configB;

    @BeforeEach
    void setUp() {

        // =============================
        // GIVEN: System configurations
        // =============================
        configA = configRepository.save(
                Config.builder()
                        .keyName("system.email.sender")
                        .value("noreply@empresa.com")
                        .build()
        );

        configB = configRepository.save(
                Config.builder()
                        .keyName("pricing.dynamic.enabled")
                        .value("true")
                        .build()
        );
    }

    // =============================
    // TEST 1 — findByKeyName
    // =============================
    @Test
    void shouldFindConfigByKeyName() {

        Optional<Config> result =
                configRepository.findByKeyName("pricing.dynamic.enabled");

        assertTrue(result.isPresent());
        assertEquals(configB.getId(), result.get().getId());
        assertEquals("true", result.get().getValue());
    }

    // =============================
    // TEST 2 — keyName not found
    // =============================
    @Test
    void shouldReturnEmptyWhenKeyDoesNotExist() {

        Optional<Config> result =
                configRepository.findByKeyName("non.existent.key");

        assertTrue(result.isEmpty());
    }

    // =============================
    // TEST 3 — canStoreMultipleKeys
    // =============================
    @Test
    void shouldStoreMultipleConfigurations() {

        long count = configRepository.count();
        assertEquals(2, count);
    }
}
