package com.eshop.admin.order;

import com.eshop.admin.order.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class OrderDetailRepositoryTest {

    @Autowired private OrderDetailRepository orderDetailRepository;

    //findWithCategoryAndTimeBetween
    //findWithProductAndTimeBetween

}
