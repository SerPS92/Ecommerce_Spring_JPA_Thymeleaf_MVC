package com.example.ecommerce.controller;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Detail;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ICategoryService;
import com.example.ecommerce.service.IProductService;
import com.example.ecommerce.service.MailSenderService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class ShopController {

    private final Logger log = LoggerFactory.getLogger(ShopController.class);
    int detailsNumber;
    List<Detail> details = new ArrayList<Detail>();

    Order order = new Order();


    private final IProductService productService;
    private final ICategoryService categoryService;

    private final MailSenderService mailSenderService;

    public ShopController(IProductService productService,
                          ICategoryService categoryService,
                          MailSenderService mailSenderService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.mailSenderService = mailSenderService;
    }

    @GetMapping("")
    public String home(Model model) {

        //Categories
        List<Category> categoryList = categoryService.findAll();
        int maxCategoriesToShow = 8;
        List<Category> categoriesToShow = categoryList.subList(0, Math.min(maxCategoriesToShow, categoryList.size()));
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("categoriesToShow", categoriesToShow);

        //Offers
        List<Product> offerProducts = productService.findByOffer();
        int maxOfferProductsToShow = 2;
        int lastIndex = 0;
        List<Product> offerProductsToShow = offerProducts.subList(0, Math.min(maxOfferProductsToShow, offerProducts.size()));
        lastIndex += maxOfferProductsToShow;
        List<Product> offerProductsToShow2 = offerProducts.subList(0, Math.min(maxOfferProductsToShow, offerProducts.size()));
        model.addAttribute("offerProductsToShow", offerProductsToShow);
        model.addAttribute("offerProductsToShow2", offerProductsToShow2);


        //Recent Products
        List<Product> productList = productService.findAll();
        int productsToShow = 8;
        List<Product> recentProducts = productList.subList(0, Math.min(productsToShow, productList.size()));
        model.addAttribute("recentProducts", recentProducts);

        //Cart
        detailsNumber = details.size();
        model.addAttribute("detailsNumber", detailsNumber);

        return "shop/index.html";
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "pageSize", defaultValue = "9") int pageSize,
                       @RequestParam(name = "minPrice", required = false) Double minPrice,
                       @RequestParam(name = "maxPrice", required = false) Double maxPrice,
                       @RequestParam(name = "categoryId", required = false) Integer categoryId,
                       Model model) {

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Product> products;
        if (categoryId == null && (minPrice == null && maxPrice == null)) {
            products = productService.getProducts(pageable);
        } else if (categoryId != null && (minPrice == null && maxPrice == null)) {
            products = productService.getProductsByCategory(categoryId, pageable);
        } else if (categoryId == null && (minPrice != null || maxPrice != null)) {
            products = productService.getProductsByPrice(minPrice, maxPrice, pageable);
        } else {
            products = productService.getProductsByCategoryAndPrice(minPrice, maxPrice, categoryId, pageable);
        }

        List<Category> categoryList = categoryService.findAll();

        detailsNumber = details.size();
        model.addAttribute("detailsNumber", detailsNumber);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("products", products);
        model.addAttribute("productsNumber", products.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());

        return "shop/shop";
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable(name = "id") int id, Model model) {

        Product product = new Product();
        Optional<Product> optionalProduct = productService.findById(id);
        product = optionalProduct.get();

        //Products to Show
        List<Product> productList = productService.findAll();
        int productsToShow = 8;
        List<Product> productsToShowList = productList.subList(0, Math.min(productsToShow, productList.size()));

        detailsNumber = details.size();
        model.addAttribute("detailsNumber", detailsNumber);
        model.addAttribute("productsToShow", productsToShowList);
        model.addAttribute("product", product);

        return "shop/product";
    }

    //Mail Part
    @GetMapping("/contact")
    public String contact(Model model) {
        detailsNumber = details.size();
        model.addAttribute("detailsNumber", detailsNumber);
        return "shop/contact";
    }

    @GetMapping("/send")
    public String send(@RequestParam(name = "name") String name,
                       @RequestParam(name = "from") String from,
                       @RequestParam(name = "subject") String subject,
                       @RequestParam(name = "text") String text) {

        String to = "pruebaspring16@gmail.com";
        String content = "Name: " + name + "\n" + "Email: " + from + "\n" + "\n" + text;
        mailSenderService.sendEmail(to, subject, content);

        return "redirect:/home";
    }
    //Final Mail Part


    //Cart
    @PostMapping("/cart")
    public String cart(@RequestParam(name = "id") Integer id,
                       @RequestParam(name = "quantity") Integer quantity,
                       Model model,
                       HttpSession session) {

        Detail detail = new Detail();
        Product product = new Product();
        double total = 0;

        Optional<Product> optionalProduct = productService.findById(id);
        String offer = optionalProduct.get().getOffer();

        product = optionalProduct.get();

        detail.setQuantity(quantity);
        detail.setName(product.getName());
        detail.setProduct(product);
        if (offer.equalsIgnoreCase("No")) {
            detail.setPrice(product.getPrice());
            detail.setTotal(product.getPrice() * quantity);
        } else {
            detail.setPrice(product.getPrice_discount());
            detail.setTotal(product.getPrice_discount() * quantity);
        }

        Integer idProduct = product.getId();
        boolean added = details.stream().anyMatch(p -> p.getProduct().getId().equals(idProduct));
        if (!added) {
            details.add(detail);
        }

        total = details.stream().mapToDouble(dt -> dt.getTotal()).sum();
        order.setTotal(total);

        detailsNumber = details.size();
        model.addAttribute("detailsNumber", detailsNumber);
        model.addAttribute("order", order);
        model.addAttribute("details", details);
        model.addAttribute("detail", detail);

        return "shop/cart.html";
    }

    @GetMapping("/cart/delete/{id}")
    public String deleteProductCart(@PathVariable(name = "id") Integer id,
                                    Model model) {

        List<Detail> newDetailList = new ArrayList<Detail>();
        for (Detail detail : details) {
            if (detail.getProduct().getId() != id) {
                newDetailList.add(detail);
            }
        }
        details = newDetailList;

        double total = 0;
        total = details.stream().mapToDouble(dt -> dt.getTotal()).sum();
        order.setTotal(total);

        detailsNumber = details.size();
        model.addAttribute("detailsNumber", detailsNumber);

        model.addAttribute("order", order);
        model.addAttribute("details", details);
        return "shop/cart.html";
    }

    @GetMapping("/getCart")
    public String getCart(Model model){
        detailsNumber = details.size();
        model.addAttribute("detailsNumber", detailsNumber);

        model.addAttribute("details", details);
        model.addAttribute("order", order);
        return "shop/cart";
    }
    //Final cart

}
