package co.edu.unimagdalena.busreserve.services.interfaces;

import co.edu.unimagdalena.busreserve.api.dto.UserDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponse create(UserCreateRequest req);
    UserResponse get(Long id);
    UserResponse getByEmail(String email);
    Page<UserResponse> list(Pageable pageable);
    List<UserResponse> findByRole(Role role);
    UserResponse update(Long id, UserUpdateRequest req);
    void delete(Long id);
    void changePassword(Long id, String newPassword);
}
