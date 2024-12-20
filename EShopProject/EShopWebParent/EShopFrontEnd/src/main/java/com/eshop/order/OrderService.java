package com.eshop.order;

import com.eshop.checkout.CheckoutInfo;
import com.eshop.common.entity.Address;
import com.eshop.common.entity.CartItem;
import com.eshop.common.entity.Customer;
import com.eshop.common.entity.order.*;
import com.eshop.common.entity.product.Product;
import com.eshop.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class OrderService {

    public static final int ORDERS_PER_PAGE = 5;
    @Autowired private OrderRepository orderRepository;

    public Order createOrder(Customer customer, Address address, List<CartItem> cartItems,
                             PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
        Order newOrder = new Order();

        newOrder.setOrderTime(new Date());
        if (paymentMethod.equals(PaymentMethod.PAYPAL)) {
            newOrder.setStatus(OrderStatus.PAID);
        }else {
            newOrder.setStatus(OrderStatus.NEW);

        }
        newOrder.setCustomer(customer);
        newOrder.setProductCost(checkoutInfo.getProductCost());
        newOrder.setSubtotal(checkoutInfo.getProductTotal());
        newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
        newOrder.setTax(0.0f);
        newOrder.setTotal(checkoutInfo.getPaymentTotal());
        newOrder.setPaymentMethod(paymentMethod);
        newOrder.setDeliverDays(checkoutInfo.getDeliverDays());
        newOrder.setDeliverDate(checkoutInfo.getDeliverDate());

        if (address == null) {
            newOrder.copyAddressFromCustomer();
        } else {
            newOrder.copyShippingAddress(address);
        }

        Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setUnitPrice(product.getDiscountPrice());
            orderDetail.setProductCost(product.getCost() * item.getQuantity());
            orderDetail.setSubtotal(item.getSubtotal());
            orderDetail.setShippingCost(item.getShippingCost());

            orderDetails.add(orderDetail);
        }

        return orderRepository.save(newOrder);
    }

    public Page<Order> listForCustomerByPage(Customer customer, int pageNum,
                                             String sortField, String sortDir, String keyWord) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);
        if (keyWord != null) {
            return orderRepository.findALl(keyWord, customer.getId(), pageable);
        }
        return orderRepository.findAll(customer.getId(), pageable);
    }

    public Order getOrder(Integer id, Customer customer) {
        return orderRepository.findByIdAndCustomer(id, customer);
    }

    public void setOrderReturnRequested(OrderReturnRequest request, Customer customer) throws OrderNotFoundException {
        Order order = orderRepository.findByIdAndCustomer(request.getOrderId(), customer);
        if (order == null) {
            throw new OrderNotFoundException("Order ID = " + request.getOrderId() + " not found");
        }
        if (order.isReturnRequested()) return;

        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrder(order);
        orderTrack.setUpdatedTime(new Date());
        orderTrack.setStatus(OrderStatus.RETURN_REQUESTED);

        String notes = "Reason : " + request.getReason();
        if (!"".equals(request.getNote())) {
            notes += ". " + request.getNote();
        }
         orderTrack.setNotes(notes);

        order.getOrderTracks().add(orderTrack);
        order.setStatus(OrderStatus.RETURN_REQUESTED);

        orderRepository.save(order);

    }

}
