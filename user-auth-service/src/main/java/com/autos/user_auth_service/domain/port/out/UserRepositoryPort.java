package com.autos.user_auth_service.domain.port.out;

import com.autos.user_auth_service.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}