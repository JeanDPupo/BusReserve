package co.edu.unimagdalena.busreserve;

import org.springframework.boot.SpringApplication;

public class TestBusReserveApplication {

    public static void main(String[] args) {
        SpringApplication.from(BusReserveApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
