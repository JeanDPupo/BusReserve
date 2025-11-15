// ==================== UserServiceImpl.java ====================
package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.UserDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Role;
import co.edu.unimagdalena.busreserve.domine.entities.User;
import co.edu.unimagdalena.busreserve.domine.repositories.UserRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.mapper.UserMapper;
import co.edu.unimagdalena.busreserve.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(UserCreateRequest req) {
        if (userRepo.findByEmail(req.email()).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        User user = mapper.toEntity(req);
        user.setPasswordHash(passwordEncoder.encode(req.password()));

        return mapper.toResponse(userRepo.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse get(Long id) {
        return userRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("User %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByEmail(String email) {
        return userRepo.findByEmail(email)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("User with email %s not found".formatted(email)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> list(Pageable pageable) {
        return userRepo.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findByRole(Role role) {
        return userRepo.findByRole(role)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest req) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User %d not found".formatted(id)));

        mapper.patch(user, req);

        return mapper.toResponse(userRepo.save(user));
    }

    @Override
    public void delete(Long id) {
        if (!userRepo.existsById(id)) {
            throw new NotFoundException("User %d not found".formatted(id));
        }
        userRepo.deleteById(id);
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User %d not found".formatted(id)));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }
}

// ==================== SeatServiceImpl.java ====================

// ==================== FareRuleServiceImpl.java ====================

// ==================== SeatHoldServiceImpl.java ====================

// ==================== BaggageServiceImpl.java ====================

// ==================== ParcelServiceImpl.java ====================

// ==================== AssignmentServiceImpl.java ====================

// ==================== IncidentServiceImpl.java ====================

// ==================== ConfigServiceImpl.java ====================

// ==================== ReportServiceImpl.java ====================
