package com.eshop.order;

import com.eshop.common.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o JOIN o.orderDetails od JOIN od.product p " +
            "WHERE o.customer.id = ?2 AND (p.name LIKE %?1% OR CAST(o.status AS string) LIKE %?1%)")
    Page<Order> findALl(String keyWord, Integer customerId, Pageable pageable);


    @Query("SELECT o FROM Order o WHERE o.customer.id = ?1")
    public Page<Order> findAll(Integer customerId, Pageable pageable);
}
