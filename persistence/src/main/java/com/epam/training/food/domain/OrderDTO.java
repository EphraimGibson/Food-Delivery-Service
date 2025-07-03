package com.epam.training.food.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(Long orderId, Long customerId, List<OrderItemDTO> orderItemDTOList,
                       BigDecimal price, LocalDateTime timestampCreated) {
}
