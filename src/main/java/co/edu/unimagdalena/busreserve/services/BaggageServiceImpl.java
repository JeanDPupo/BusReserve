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
public abstract class BaggageServiceImpl implements BaggageService {

    private final BaggageRepository baggageRepo;
    private final TicketRepository ticketRepo;
    private final BaggageMapper mapper;

    @Override
    public BaggageResponse create(BaggageCreateRequest req, Long ticketId) {
        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket %d not found".formatted(ticketId)));

        Baggage baggage = mapper.toEntity(req);
        baggage.setTicket(ticket);

        return mapper.toResponse(baggageRepo.save(baggage));
    }

    @Override
    @Transactional(readOnly = true)
    public BaggageResponse get(Long id) {
        return baggageRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Baggage %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public BaggageResponse getByTagCode(String tagCode) {
        return baggageRepo.findByTagCode(tagCode)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Baggage with tag %s not found".formatted(tagCode)));
    }
}