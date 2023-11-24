package com.example.ecommerce.controller;

import com.example.ecommerce.model.Detail;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.IOrderService;
import com.example.ecommerce.service.IProductService;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final IOrderService orderService;
    private final IProductService productService;

    public OrderController(IOrderService orderService, IProductService productService){
        this.orderService = orderService;
        this.productService = productService;
    }

    @GetMapping("")
    public String list(@RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                       Model model){

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Order> orders = orderService.getOrders(pageable);
        model.addAttribute("orders", orders);
        model.addAttribute("ordersNumber", orders.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());

        return "admin/orders/orders";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable(name = "id")int id,
                          Model model){
        Optional<Order> optionalOrder = orderService.findById(id);
        if(optionalOrder.isPresent()){
            Order order = optionalOrder.get();
            model.addAttribute("details", order.getDetails());
        }
        return "/admin/orders/details";
    }

    @GetMapping("/send/{id}")
    public String send(@PathVariable(name = "id")int id){
        Optional<Order> optionalOrder = orderService.findById(id);
        if(optionalOrder.isPresent()){
            Order order = optionalOrder.get();
            order.setState("Send");
            orderService.save(order);
        }

        List<Detail> details = optionalOrder.get().getDetails();
        for(Detail detail:details){
            int idProduct = detail.getProduct().getId();
            int quantity = detail.getQuantity();
            int stock = detail.getProduct().getStock();
            int newStock = stock - quantity;
            int sells = detail.getProduct().getSells();
            int newSells = sells + quantity;

            log.info("idProduct: " + idProduct +
                    " quantity: " + quantity +
                    " stock: " + stock +
                    " new Stock: " + newStock +
                    " sells: " + sells +
                    " new Sells: " + newSells);

            Optional<Product> optionalProduct = productService.findById(idProduct);
            if(optionalProduct.isPresent()){
                Product product = optionalProduct.get();
                product.setStock(newStock);
                product.setSells(newSells);
                productService.save(product);
            }
        }

        return "redirect:/order";
    }

}
