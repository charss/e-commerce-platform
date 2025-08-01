package com.example.order_svc.service;

import com.example.order_svc.client.ProductSvcClient;
import com.example.order_svc.client.UserSvcClient;
import com.example.order_svc.dto.*;
import com.example.order_svc.entity.Order;
import com.example.order_svc.entity.OrderItem;
import com.example.order_svc.exception.InvalidOrderItemException;
import com.example.order_svc.exception.InvalidOrderStateException;
import com.example.order_svc.exception.OrderNotFoundException;
import com.example.order_svc.repository.OrderItemRepository;
import com.example.order_svc.repository.OrderRepository;
import com.example.shared.dto.BulkCreateInventoryMovementDto;
import com.example.shared.dto.CreateInventoryMovementDto;
import com.example.shared.dto.ProductVariantBasicDto;
import com.example.shared.dto.UserWithIdDto;
import com.example.shared.enums.MovementType;
import com.example.shared.enums.OrderStatus;
import com.example.shared.enums.SourceType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepo;
    @Autowired
    OrderItemRepository orderItemRepo;
    @Autowired
    UserSvcClient userSvcClient;
    @Autowired
    ProductSvcClient productSvcClient;
    @Autowired
    OrderHistoryService orderHistoryService;

    public List<OrderDto> getAllOrders() {
        return orderRepo.findAll().stream()
                .map(OrderDto::from)
                .toList();
    }

    public OrderDto getOrderById(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(
                () -> new OrderNotFoundException("Order #" + id + "not found."));
        return OrderDto.from(order);
    }

    @Transactional
    public OrderDto createOrder(CreateOrderDto orderDto) {
        UserWithIdDto user = userSvcClient.getUserById(orderDto.userId());

        Order order = new Order();
        order.setUserId(user.id());
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepo.saveAndFlush(order);

        orderHistoryService.createOrderHistory(savedOrder);

        Double total = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CreateOrderItemDto itemDto : orderDto.items()) {
            OrderItem orderItem = buildOrderItem(itemDto, savedOrder);
            orderItems.add(orderItem);
            total += orderItem.getSubtotal();
        }
        order.setTotalAmount(total);
        orderItemRepo.saveAll(orderItems);
        savedOrder.setItems(orderItems);

        return OrderDto.from(savedOrder);
    }

    private OrderItem buildOrderItem(CreateOrderItemDto orderItemDto, Order order) {
        ProductVariantBasicDto product = productSvcClient.getProductVariantById(orderItemDto.productVarId());

        validateOrderItemQuantity(orderItemDto.quantity(), product);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductVariantId(orderItemDto.productVarId());
        orderItem.setSku(product.sku());
        orderItem.setQuantity(orderItemDto.quantity());
        orderItem.setPricePerUnit(product.price());
        orderItem.setSubtotal(orderItemDto.quantity() * product.price());
        return orderItem;
    }

    @Transactional
    public OrderDto updateToPaid(UpdateOrderStatusDto updateDto) {
        Order order = orderRepo.findById(updateDto.id()).orElseThrow(
                () -> new OrderNotFoundException("Order #" + updateDto.id() + " not found."));

        if (!order.getStatus().equals(OrderStatus.PENDING))
                throw new InvalidOrderStateException("Only pending orders can be confirmed.");

        // TODO: Validate Payment

        productSvcClient.processBulkStockMovement(new BulkCreateInventoryMovementDto(
                order.getItems().stream()
                        .map(item -> new CreateInventoryMovementDto(
                                item.getProductVariantId(),
                                item.getQuantity(),
                                MovementType.RESERVE,
                                SourceType.ORDER,
                                Optional.of(String.valueOf(order.getId()))
                        ))
                        .toList()
        ));

        order.setStatus(OrderStatus.PAID);
        orderRepo.save(order);
        orderHistoryService.createOrderHistory(order);
        return OrderDto.from(order);
//        return updateToConfirm(updateDto);
    }

    @Transactional
    public OrderDto updateToConfirm(UpdateOrderStatusDto updateDto) {
        Order order = orderRepo.findById(updateDto.id()).orElseThrow(
                () -> new OrderNotFoundException("Order #" + updateDto.id() + " not found."));

        if (!order.getStatus().equals(OrderStatus.PAID))
            throw new InvalidOrderStateException("Only paid orders can be confirmed.");

        productSvcClient.processBulkStockMovement(new BulkCreateInventoryMovementDto(
                order.getItems().stream()
                        .map(item -> new CreateInventoryMovementDto(
                                item.getProductVariantId(),
                                item.getQuantity(),
                                MovementType.OUT,
                                SourceType.ORDER,
                                Optional.of(String.valueOf(order.getId()))
                        ))
                        .toList()
        ));

        order.setStatus(OrderStatus.CONFIRMED);
        orderRepo.save(order);
        orderHistoryService.createOrderHistory(order);
        return OrderDto.from(order);
    }

    @Transactional
    public OrderDto updateToCancelled(UpdateOrderStatusDto updateDto) {
        Order order = orderRepo.findById(updateDto.id()).orElseThrow(
                () -> new OrderNotFoundException("Order #" + updateDto.id() + " not found."));

        if (!order.getStatus().equals(OrderStatus.PAID))
            throw new InvalidOrderStateException("Only paid orders can be cancelled.");

        productSvcClient.processBulkStockMovement(new BulkCreateInventoryMovementDto(
                order.getItems().stream()
                        .map(item -> new CreateInventoryMovementDto(
                                item.getProductVariantId(),
                                item.getQuantity(),
                                MovementType.ORDER_CANCELLED,
                                SourceType.ORDER,
                                Optional.of(String.valueOf(order.getId()))
                        ))
                        .toList()
        ));

        order.setStatus(OrderStatus.CANCELLED);
        orderRepo.save(order);
        orderHistoryService.createOrderHistory(order);
        return OrderDto.from(order);
    }

    private void validateOrderItemQuantity(Integer quantity, ProductVariantBasicDto product) {
        if (quantity < 0) {
            throw new InvalidOrderItemException("Quantity cannot be negative");
        }
        if (quantity > product.stock()) {
            throw new InvalidOrderItemException("Invalid item " + product.sku() + ". Requested stock (" + quantity + ") exceeds available stock (" + product.stock() + ")");
        }
    }


}
