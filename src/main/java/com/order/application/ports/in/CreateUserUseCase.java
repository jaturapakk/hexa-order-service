package com.order.application.ports.in;

import com.order.domain.model.Money;
import com.order.domain.model.UserId;

public interface CreateUserUseCase {
    UserId execute(Command command);

    record Command(String name, String email, Money initialBalance) {}
}
