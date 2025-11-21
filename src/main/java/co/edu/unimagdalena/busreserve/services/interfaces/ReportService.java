package co.edu.unimagdalena.busreserve.services.interfaces;

import java.time.LocalDateTime;
import java.util.Map;

public interface ReportService {
    Map<String, Object> getOccupancyReport(Long tripId);
    Map<String, Object> getSalesReport(LocalDateTime from, LocalDateTime to);
    Map<String, Object> getNoShowReport(LocalDateTime from, LocalDateTime to);
    Map<String, Object> getParcelDeliveryReport(LocalDateTime from, LocalDateTime to);
    Map<String, Object> getDriverPerformance(Long driverId);
}