package com.epam.training.food.data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.epam.training.food.domain.Order;
import com.epam.training.food.domain.OrderDTO;
import com.epam.training.food.domain.OrderItem;
import com.epam.training.food.domain.OrderItemDTO;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class OrderWriter {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void writeOrdersToFile(List<OrderDTO> orders, Path outputPath) {
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)){
            writer.write(addOrders(orders));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String addOrders(List<OrderDTO> orders) {
        return orders
                .stream()
                .map(order -> addOrderItems(
                        order.orderItemDTOList(),
                        order.price(),
                        order.timestampCreated(),
                        order.orderId(),
                        order.customerId()
                ))
                .collect(Collectors.joining("\n"));
    }

    private String addOrderItems(List<OrderItemDTO> orderItems, BigDecimal totalPrice, LocalDateTime timestampCreated, Long orderId, long customerId) {
        return orderItems
                .stream()
                .map(orderItem -> addOrderItem(
                        orderItem,
                        totalPrice,
                        timestampCreated,
                        orderId,
                        customerId
                ))
                .collect(Collectors.joining("\n"));
    }

    private String addOrderItem(OrderItemDTO orderItem, BigDecimal totalPrice, LocalDateTime timestampCreated, Long orderId, long customerId) {
        return String.join(
                ",",
                Long.toString(orderId),
                Long.toString(customerId),
                orderItem.foodName(),
                Integer.toString(orderItem.pieces()),
                orderItem.price().toPlainString(),
                timestampCreated.format(DATE_TIME_FORMATTER),
                totalPrice.toPlainString());
    }
}
