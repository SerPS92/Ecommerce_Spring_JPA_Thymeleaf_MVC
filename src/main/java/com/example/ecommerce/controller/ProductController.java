package com.example.ecommerce.controller;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ICategoryService;
import com.example.ecommerce.service.IProductService;
import com.example.ecommerce.service.UploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final IProductService productService;
    private final ICategoryService categoryService;
    private final UploadFileService uploadFileService;

    public ProductController(IProductService productService,
                             UploadFileService uploadFileService,
                             ICategoryService categoryService) {
        this.productService = productService;
        this.uploadFileService = uploadFileService;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String list(@RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                       Model model) {

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Product> products = productService.getProducts(pageable);
        model.addAttribute("products", products);
        model.addAttribute("productsNumber", products.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());

        log.info("Products: {}", products);

        return "admin/products/products";
    }

    @GetMapping("/create")
    public String create(Model model) {
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);
        return "admin/products/create";
    }

    @PostMapping("/save")
    public String save(Product product,
                       @RequestParam("img") MultipartFile file,
                       @RequestParam("img2") MultipartFile file2,
                       @RequestParam("img3") MultipartFile file3) throws IOException {

        log.info("product:{}", product);

        if (product.getId() == null) {
            String nameImage = uploadFileService.saveImage(file);
            product.setImage1(nameImage);
            String nameImage2 = uploadFileService.saveImage2(file2);
            product.setImage2(nameImage2);
            String nameImage3 = uploadFileService.saveImage3(file3);
            product.setImage3(nameImage3);
        }

        product.setOffer("No");

        productService.save(product);
        return "redirect:/product";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {

        Product product = new Product();
        Optional<Product> optionalProduct = productService.findById(id);
        if (optionalProduct.isPresent()) {
            product = optionalProduct.get();
        }

        List<Category> categoryList = categoryService.findAll();
        int selectedCategory = product.getCategory().getId();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("selectedCategory", selectedCategory);

        model.addAttribute("product", product);

        return "admin/products/edit";
    }

    @PostMapping("/update")
    public String update(Product product,
                         @RequestParam("img") MultipartFile file,
                         @RequestParam("img2") MultipartFile file2,
                         @RequestParam("img3") MultipartFile file3,
                         @RequestParam(name = "checkoffer", required = false) boolean checkoffer) throws IOException {

        Product p = new Product();
        p = productService.findById(product.getId()).get();

        //Image1
        if (file.isEmpty()) {
            product.setImage1(p.getImage1());
        } else {
            if (!p.getImage1().equals("default.jpg")) {
                uploadFileService.deleteImage(p.getImage1());
            }
            String nameImage = uploadFileService.saveImage(file);
            product.setImage1(nameImage);
        }

        //Image2
        if (file2.isEmpty()) {
            product.setImage2(p.getImage2());
        } else {
            if (!p.getImage2().equals("default.jpg")) {
                uploadFileService.deleteImage(p.getImage2());
            }
            String nameImage = uploadFileService.saveImage2(file2);
            product.setImage2(nameImage);
        }

        //Image3
        if (file3.isEmpty()) {
            product.setImage3(p.getImage3());
        } else {
            if (!p.getImage3().equals("default.jpg")) {
                uploadFileService.deleteImage(p.getImage3());
            }
            String nameImage = uploadFileService.saveImage3(file3);
            product.setImage3(nameImage);
        }

        //Checkbox offers
        if(checkoffer){
            product.setOffer("Yes");
        } else {
            product.setOffer("No");
        }

        productService.update(product);
        return "redirect:/product";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        Product product = new Product();
        product = productService.findById(id).get();


        if (!product.getImage1().equals("default.jpg")) {
            uploadFileService.deleteImage(product.getImage1());
        }

        if (!product.getImage2().equals("default.jpg")) {
            uploadFileService.deleteImage(product.getImage2());
        }

        if (!product.getImage3().equals("default.jpg")) {
            uploadFileService.deleteImage(product.getImage3());
        }

        productService.delete(id);
        return "redirect:/product";
    }

}
