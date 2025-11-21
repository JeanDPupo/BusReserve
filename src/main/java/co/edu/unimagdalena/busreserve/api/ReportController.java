package co.edu.unimagdalena.busreserve.api;

import co.edu.unimagdalena.busreserve.services.interfaces.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
public class ReportController {

    private final ReportService service;

    @GetMapping("/occupancy/{tripId}")
    public ResponseEntity<Map<String, Object>> getOccupancyReport(@PathVariable Long tripId) {
        return ResponseEntity.ok(service.getOccupancyReport(tripId));
    }

    @GetMapping("/sales")
    public ResponseEntity<Map<String, Object>> getSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(service.getSalesReport(from, to));
    }

    @GetMapping("/no-show")
    public ResponseEntity<Map<String, Object>> getNoShowReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(service.getNoShowReport(from, to));
    }

    @GetMapping("/parcel-delivery")
    public ResponseEntity<Map<String, Object>> getParcelDeliveryReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(service.getParcelDeliveryReport(from, to));
    }

    @GetMapping("/driver-performance/{driverId}")
    public ResponseEntity<Map<String, Object>> getDriverPerformance(@PathVariable Long driverId) {
        return ResponseEntity.ok(service.getDriverPerformance(driverId));
    }

}
