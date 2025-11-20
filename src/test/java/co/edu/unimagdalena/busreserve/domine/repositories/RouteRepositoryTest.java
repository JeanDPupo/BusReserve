package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Route;
import co.edu.unimagdalena.busreserve.domine.entities.Stop;
import org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RouteRepositoryTest extends AbstractRepositoryIT{
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private StopRepository stopRepository;

    @Test
    void shouldFindRouteById() {
        Stop origin = Stop.builder()
                .name("Bogotá")
                .stopOrder(1)
                .lat(4.6)
                .lng(-74.0)
                .build();

        Stop destination = Stop.builder()
                .name("Medellín")
                .stopOrder(2)
                .lat(6.2)
                .lng(-75.5)
                .build();

        origin = stopRepository.save(origin);
        destination = stopRepository.save(destination);

        // Arrange
        Route route = new Route();
        route.setCode("R-BOG-MDE");
        route.setName("Bogotá -> Medellín");
        route.setOrigin(origin);
        route.setDestination(destination);
        route.setDistanceKm(400.0);
        route.setDurationMin(480);
        // Guardar la ruta
        routeRepository.save(route);

        // Act
        Route foundRoute = routeRepository.findById(route.getId()).orElse(null);

        // Assert
        assertThat(foundRoute).isNotNull();
        assertThat(foundRoute.getCode()).isEqualTo("R-BOG-MDE");
    }

    @Test
    void shouldFindRoutesByOrigin() {
        // Arrange
        Route route1 = new Route();
        route1.setCode("R-BOG-MDE");
        route1.setName("Bogotá -> Medellín");
        route1.setDistanceKm(400.0);
        route1.setDurationMin(480);
        routeRepository.save(route1);

        Route route2 = new Route();
        route2.setCode("R-BOG-CLO");
        route2.setName("Bogotá -> Cali");
        route2.setDistanceKm(450.0);
        route2.setDurationMin(500);
        routeRepository.save(route2);

        // Act
        List<Route> routesFromBogota = routeRepository.findByOriginId(1L); // Suponiendo que 1L es el ID de Bogotá

        // Assert
        assertThat(routesFromBogota).hasSize(2);
    }
}
