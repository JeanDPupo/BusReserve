package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.domine.entities.ParcelStatus;
import co.edu.unimagdalena.busreserve.domine.entities.TicketStatus;
import co.edu.unimagdalena.busreserve.domine.entities.Trip;
import co.edu.unimagdalena.busreserve.domine.repositories.*;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final TripRepository tripRepo;
    private final TicketRepository ticketRepo;
    private final ParcelRepository parcelRepo;
    private final AssignmentRepository assignmentRepo;

    @Override
    public Map<String, Object> getOccupancyReport(Long tripId) {
        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip %d not found".formatted(tripId)));

        Long soldSeats = ticketRepo.countSoldSeats(tripId);
        Integer totalCapacity = trip.getBus().getCapacity();
        Double occupancyRate = (soldSeats.doubleValue() / totalCapacity) * 100;

        Map<String, Object> report = new HashMap<>();
        report.put("tripId", tripId);
        report.put("routeCode", trip.getRoute().getCode());
        report.put("busPlate", trip.getBus().getPlate());
        report.put("totalCapacity", totalCapacity);
        report.put("soldSeats", soldSeats);
        report.put("availableSeats", totalCapacity - soldSeats);
        report.put("occupancyRate", String.format("%.2f%%", occupancyRate));
        report.put("departureAt", trip.getDepartureAt());

        return report;
    }

    @Override
    public Map<String, Object> getSalesReport(LocalDateTime from, LocalDateTime to) {
        var tickets = ticketRepo.findByStatusAndTrip_DepartureAtBefore((TicketStatus.SOLD), to)
                .stream()
                .filter(t -> t.getTrip().getDepartureAt().isAfter(from))
                .toList();

        Double totalRevenue = tickets.stream()
                .mapToDouble(t -> t.getPrice())
                .sum();

        Map<String, Object> salesByPaymentMethod = new HashMap<>();
        tickets.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        t -> t.getPaymentMethod().toString(),
                        java.util.stream.Collectors.counting()
                ))
                .forEach(salesByPaymentMethod::put);

        Map<String, Object> report = new HashMap<>();
        report.put("period", Map.of("from", from, "to", to));
        report.put("totalTickets", tickets.size());
        report.put("totalRevenue", totalRevenue);
        report.put("averageTicketPrice", tickets.isEmpty() ? 0 : totalRevenue / tickets.size());
        report.put("salesByPaymentMethod", salesByPaymentMethod);

        return report;
    }

    @Override
    public Map<String, Object> getNoShowReport(LocalDateTime from, LocalDateTime to) {
        var noShowTickets = ticketRepo.findByStatusAndTrip_DepartureAtBefore(TicketStatus.NO_SHOW, to)
                .stream()
                .filter(t -> t.getTrip().getDepartureAt().isAfter(from))
                .toList();

        var totalTickets = ticketRepo.findByStatusAndTrip_DepartureAtBefore(TicketStatus.SOLD, to)
                .stream()
                .filter(t -> t.getTrip().getDepartureAt().isAfter(from))
                .count() + noShowTickets.size();

        Double noShowRate = totalTickets > 0 
                ? (noShowTickets.size() * 100.0) / totalTickets 
                : 0.0;

        Map<String, Object> report = new HashMap<>();
        report.put("period", Map.of("from", from, "to", to));
        report.put("totalNoShows", noShowTickets.size());
        report.put("totalTickets", totalTickets);
        report.put("noShowRate", String.format("%.2f%%", noShowRate));

        return report;
    }

    @Override
    public Map<String, Object> getParcelDeliveryReport(LocalDateTime from, LocalDateTime to) {
        var parcels = parcelRepo.findAll().stream()
                .filter(p -> p.getCreatedAt().isAfter(from) && p.getCreatedAt().isBefore(to))
                .toList();

        long delivered = parcels.stream()
                .filter(p -> p.getStatus() == ParcelStatus.DELIVERED)
                .count();

        long failed = parcels.stream()
                .filter(p -> p.getStatus() == ParcelStatus.FAILED)
                .count();

        Double deliveryRate = parcels.isEmpty() 
                ? 0.0 
                : (delivered * 100.0) / parcels.size();

        Map<String, Object> report = new HashMap<>();
        report.put("period", Map.of("from", from, "to", to));
        report.put("totalParcels", parcels.size());
        report.put("delivered", delivered);
        report.put("failed", failed);
        report.put("inTransit", parcels.size() - delivered - failed);
        report.put("deliveryRate", String.format("%.2f%%", deliveryRate));

        return report;
    }

    @Override
    public Map<String, Object> getDriverPerformance(Long driverId) {
        var assignments = assignmentRepo.findByDriverId(driverId);

        long totalTrips = assignments.size();
        long completedTrips = assignments.stream()
                .filter(a -> a.getChecklistOk() != null && a.getChecklistOk())
                .count();

        Map<String, Object> report = new HashMap<>();
        report.put("driverId", driverId);
        report.put("totalTrips", totalTrips);
        report.put("completedTrips", completedTrips);
        report.put("pendingTrips", totalTrips - completedTrips);
        report.put("completionRate", totalTrips > 0 
                ? String.format("%.2f%%", (completedTrips * 100.0) / totalTrips) 
                : "0.00%");

        return report;
    }
}