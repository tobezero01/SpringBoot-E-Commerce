package com.eshop.customer;

import com.eshop.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("Select c from Customer c Where c.email = ?1")
    public Customer findByEmail(String email);

    @Query("select c from Customer c Where c.verificationCode = ?1")
    public Customer findByVerificationCode(String code);

    @Query("Update Customer c Set c.enabled = true Where c.id = ?1")
    @Modifying
    public void enabled(Integer id);
}