package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.BaggageDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Baggage;
import co.edu.unimagdalena.busreserve.domine.entities.Ticket;
import co.edu.unimagdalena.busreserve.domine.repositories.BaggageRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.TicketRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.BaggageService;
import co.edu.unimagdalena.busreserve.services.mapper.BaggageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BaggageServiceImpl implements BaggageService {

    private final BaggageRepository baggageRepo;
    private final TicketRepository ticketRepo;
    private final BaggageMapper mapper;

    @Override
    public BaggageResponse create(BaggageCreateRequest req, Long ticketId) {

        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        if (req.tagCode() != null) {
            boolean exists = baggageRepo.findByTagCode(req.tagCode()).isPresent();
            if (exists)
                throw new NotFoundException("Tag code already exists");
        }

        Baggage baggage = mapper.toEntity(req);
        baggage.setTicket(ticket);

        return mapper.toResponse(baggageRepo.save(baggage));
    }

    @Override
    @Transactional(readOnly = true)
    public BaggageResponse get(Long id) {
        return baggageRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Baggage not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public BaggageResponse getByTagCode(String tagCode) {
        return baggageRepo.findByTagCode(tagCode)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Baggage not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BaggageResponse> listByTicket(Long ticketId) {
        return baggageRepo.findByTicketId(ticketId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        Baggage baggage = baggageRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Baggage not found"));

        baggageRepo.delete(baggage);
    }
}