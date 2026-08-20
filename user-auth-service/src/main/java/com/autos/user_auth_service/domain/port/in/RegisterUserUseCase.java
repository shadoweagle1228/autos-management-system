package com.autos.user_auth_service.domain.port.in;

import com.autos.user_auth_service.domain.model.User;

public interface RegisterUserUseCase {
    User register(User user);
}