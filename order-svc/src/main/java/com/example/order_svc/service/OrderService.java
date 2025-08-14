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
import com.example.shared.dto.*;
import com.example.shared.enums.MovementType;
import com.example.shared.enums.NotificationType;
import com.example.shared.enums.OrderStatus;
import com.example.shared.enums.SourceType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shared.feign.notification.NotificationClient;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final UserSvcClient userSvcClient;
    private final ProductSvcClient productSvcClient;
    private final OrderHistoryService orderHistoryService;
    private final NotificationClient notifClient;

    public OrderService(OrderRepository orderRepo, OrderItemRepository orderItemRepo,
                        UserSvcClient userSvcClient, ProductSvcClient productSvcClient,
                        OrderHistoryService orderHistoryService, NotificationClient notifClient ) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.userSvcClient = userSvcClient;
        this.productSvcClient = productSvcClient;
        this.orderHistoryService = orderHistoryService;
        this.notifClient = notifClient;
    }

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
    public OrderResponseDto createOrder(CreateOrderDto orderDto) {
        final String CURRENCY = "PHP";
        UserWithIdDto user = userSvcClient.getUserById(orderDto.userId());

        Order order = new Order();
        order.setUserId(user.id());
        order.setUserFirstName(user.firstName());
        order.setUserLastName(user.lastName());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmountMinor(0L);
        order.setCurrency(CURRENCY);
        Order savedOrder = orderRepo.saveAndFlush(order);

        orderHistoryService.createOrderHistory(savedOrder);

        Long total = 0L;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CreateOrderItemDto itemDto : orderDto.items()) {
            OrderItem orderItem = buildOrderItem(itemDto, savedOrder);
            orderItems.add(orderItem);
            total += orderItem.getSubtotalMinor();
        }
        order.setTotalAmountMinor(total);
        orderItemRepo.saveAll(orderItems);
        savedOrder.setItems(orderItems);

        return OrderResponseDto.from(savedOrder);
    }

    private OrderItem buildOrderItem(CreateOrderItemDto orderItemDto, Order order) {
        ProductVariantBasicDto product = productSvcClient.getProductVariantById(orderItemDto.productVarId());

        validateOrderItemQuantity(orderItemDto.quantity(), product);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductVariantId(orderItemDto.productVarId());
        orderItem.setSku(product.sku());
        orderItem.setQuantity(orderItemDto.quantity());
        orderItem.setUnitPriceMinor(product.unitPriceMinor());
        orderItem.setCurrency(product.currency());
        orderItem.setSubtotalMinor(orderItemDto.quantity() * product.unitPriceMinor());
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

        List<Map<String, Object>> items = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            Map<String, Object> item1 = new HashMap<>();
            item1.put("sku", item.getSku());
            item1.put("quantity", item.getQuantity());
            item1.put("pricePerUnit", item.getUnitPriceMinor());

            items.add(item1);
        }

        Map<String, Object> data = Map.of(
                "email", "ecomtestmail@yopmail.com",
                "firstName", order.getUserFirstName(),
                "orderId", order.getId(),
                "orderDate", order.getOrderDate().toString(),
                "items", items,
                "total", order.getTotalAmountMinor()
        );

        notifClient.sendEmail(new CreateNotificationDto(
                NotificationType.ORDER_CONFIRMED,
                order.getUserId(),
                new ObjectMapper().valueToTree(data)
        ));
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
