package com.eshop.admin.customer.controller;

import com.eshop.admin.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
    @Autowired private CustomerService customerService;

    @PostMapping("/customers/check_email")
    @ResponseBody // Đảm bảo trả về chuỗi phản hồi thay vì một trang
    public String checkDuplicatedEmail(Integer id, String email) {
        return customerService.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }

}
