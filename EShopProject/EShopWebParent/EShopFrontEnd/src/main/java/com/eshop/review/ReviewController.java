package com.eshop.review;

import com.eshop.Utility;
import com.eshop.common.entity.Customer;
import com.eshop.common.entity.Review;
import com.eshop.common.entity.product.Product;
import com.eshop.customer.CustomerService;
import com.eshop.exception.ProductNotFoundException;
import com.eshop.exception.ReviewNotFoundException;
import com.eshop.product.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ReviewController {

    String defaultRedirect = "redirect:/reviews/page/1?sortField=reviewTime&sortDir=desc";

    @Autowired private ReviewService reviewService;
    @Autowired private CustomerService customerService;

    @Autowired private ProductService productService;

    @GetMapping("/reviews")
    public String listFirstPage(Model model) {
        return defaultRedirect;
    }

    @GetMapping("/reviews/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             Model model, HttpServletRequest request,
                             String keyWord, String sortField, String sortDir
    ) {
        Customer customer = getAuthenticatedCustomer(request);
        Page<Review> page = reviewService.listByCustomerByPage(customer, keyWord, pageNum, sortField, sortDir);
        List<Review> listReviews = page.getContent();

        long startCount = (pageNum - 1) * ReviewService.REVIEWS_PER_PAGE;
        long endCount = startCount + ReviewService.REVIEWS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("listReviews", listReviews);

        return "reviews/reviews";
    }

    @GetMapping("/reviews/detail/{id}")
    public String viewReview(@PathVariable("id") Integer id, Model model,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        try {
            Review review = reviewService.getByCustomerAndId(customer,id);
            model.addAttribute("review", review);
            return "reviews/review_detail_modal";
        } catch (ReviewNotFoundException e) {
            redirectAttributes.addFlashAttribute(e.getMessage());
            return defaultRedirect;
        }
    }

    @GetMapping("/ratings/{productAlias}/page/{pageNum}")
    public String listByProduct(Model model,
                                @PathVariable(name = "productAlias") String productAlias,
                                @PathVariable(name = "pageNum") int pageNum,
                                String sortField, String sortDir) {
        Product product = null;

        try {
            product = productService.getProduct(productAlias);
        } catch (ProductNotFoundException e) {
            return "error/404";
        }
        Page<Review> page = reviewService.listByProduct(product, pageNum, sortField, sortDir);
        List<Review> listReviews = page.getContent();

        long startCount = (pageNum - 1) * ReviewService.REVIEWS_BY_PRODUCT_PER_PAGE;
        long endCount = startCount + ReviewService.REVIEWS_BY_PRODUCT_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("listReviews", listReviews);
        model.addAttribute("pageTitle", "Reviews for " + product.getShortName());
        model.addAttribute("product", product);

        return "reviews/review_product";
    }

    @GetMapping("/ratings/{productAlias}")
    public String listByProductFirstPage(Model model, @PathVariable(name = "productAlias") String productAlias) {
        return listByProduct(model, productAlias, 1, "reviewTime", "desc");
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getMailOfAuthenticatedCustomer(request);

        return customerService.getCustomerByEmail(email);
    }
}
