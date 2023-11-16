package com.example.ecommerce.controller;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ICategoryService;
import com.example.ecommerce.service.IProductService;
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
@RequestMapping("/home")
public class ShopController {

    private final Logger log = LoggerFactory.getLogger(ShopController.class);

    private final IProductService productService;
    private final ICategoryService categoryService;

    public ShopController(IProductService productService, ICategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
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
        } else if(categoryId != null && (minPrice == null && maxPrice == null)){
            products = productService.getProductsByCategory(categoryId, pageable);
        } else if(categoryId == null &&(minPrice != null || maxPrice != null)){
            products = productService.getProductsByPrice(minPrice, maxPrice, pageable);
        } else {
            products = productService.getProductsByCategoryAndPrice(minPrice, maxPrice, categoryId, pageable);
        }

        List<Category> categoryList = categoryService.findAll();

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("products", products);
        model.addAttribute("productsNumber", products.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());

        return "shop/shop";
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable(name = "id") int id, Model model){

        Product product = new Product();
        Optional<Product> optionalProduct = productService.findById(id);
        product = optionalProduct.get();

        //Products to Show
        List<Product> productList = productService.findAll();
        int productsToShow = 8;
        List<Product> productsToShowList = productList.subList(0, Math.min(productsToShow, productList.size()));

        model.addAttribute("productsToShow", productsToShowList);
        model.addAttribute("product", product);

        return "shop/product";
    }

}
