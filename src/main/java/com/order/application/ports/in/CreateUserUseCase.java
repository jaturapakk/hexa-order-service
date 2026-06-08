package com.order.application.ports.in;

import com.order.domain.model.UserId;

import java.math.BigDecimal;

public interface CreateUserUseCase {
    UserId execute(Command command);
    record Command( String userName, BigDecimal initialBalance){}
}
