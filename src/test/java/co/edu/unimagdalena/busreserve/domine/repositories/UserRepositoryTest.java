package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Role;
import co.edu.unimagdalena.busreserve.domine.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepositoryTest extends AbstractRepositoryIT{
    @Autowired
    private UserRepository userRepository;

    private User passenger;
    private User driver;
    private User dispatcher;

    @BeforeEach
    void setUp() {

        passenger = userRepository.save(
                User.builder()
                        .name("Carlos")
                        .email("carlos@mail.com")
                        .phone("3001112222")
                        .passwordHash("pass1")
                        .role(Role.PASSENGER)
                        .build()
        );

        driver = userRepository.save(
                User.builder()
                        .name("Luis")
                        .email("driver@mail.com")
                        .phone("3003334444")
                        .passwordHash("pass2")
                        .role(Role.DRIVER)
                        .build()
        );

        dispatcher = userRepository.save(
                User.builder()
                        .name("Laura")
                        .email("dispatcher@mail.com")
                        .phone("3005556666")
                        .passwordHash("pass3")
                        .role(Role.DISPATCHER)
                        .build()
        );
    }

    // ============================================
    // TEST 1: findByEmail (exists)
    // ============================================
    @Test
    void shouldFindUserByEmail() {
        Optional<User> result = userRepository.findByEmail("driver@mail.com");

        assertTrue(result.isPresent());
        assertEquals(driver.getId(), result.get().getId());
    }

    // ============================================
    // TEST 2: findByEmail (not exists)
    // ============================================
    @Test
    void shouldReturnEmptyForNonExistingEmail() {
        Optional<User> result = userRepository.findByEmail("unknown@mail.com");
        assertTrue(result.isEmpty());
    }

    // ============================================
    // TEST 3: findByPhone
    // ============================================
    @Test
    void shouldFindUserByPhone() {
        Optional<User> result = userRepository.findByPhone("3001112222");

        assertTrue(result.isPresent());
        assertEquals(passenger.getId(), result.get().getId());
    }

    // ============================================
    // TEST 4: findByIdAndRole (valid)
    // ============================================
    @Test
    void shouldFindUserByIdAndRole() {
        Optional<User> result = userRepository.findByIdAndRole(driver.getId(), Role.DRIVER);

        assertTrue(result.isPresent());
        assertEquals(driver.getId(), result.get().getId());
    }

    // ============================================
    // TEST 5: findByIdAndRole (wrong role)
    // ============================================
    @Test
    void shouldReturnEmptyWhenRoleDoesNotMatch() {
        Optional<User> result = userRepository.findByIdAndRole(passenger.getId(), Role.DRIVER);

        assertTrue(result.isEmpty());
    }
}
