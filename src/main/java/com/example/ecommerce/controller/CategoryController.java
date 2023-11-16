package com.example.ecommerce.controller;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.service.ICategoryService;
import com.example.ecommerce.service.UploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final ICategoryService categoryService;
    private final UploadFileService uploadFileService;

    public CategoryController(ICategoryService categoryService, UploadFileService uploadFileService) {
        this.categoryService = categoryService;
        this.uploadFileService = uploadFileService;
    }

    @GetMapping("")
    public String list(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        int pageSize = 10;

        Page<Category> categories = categoryService.getCategories(page, pageSize);
        model.addAttribute("categories", categories);
        model.addAttribute("categoriesNumber", categories.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categories.getTotalPages());

        return "admin/categories/categories";
    }

    @GetMapping("/create")
    public String create() {
        return "admin/categories/create";
    }

    @PostMapping("/save")
    public String save(Category category, @RequestParam("img")MultipartFile file) throws IOException {

        if(category.getId() == null){
            String nameImage = uploadFileService.saveImage(file);
            category.setImage(nameImage);
        }

        categoryService.save(category);
        return "redirect:/category";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {

        Category category = new Category();
        Optional<Category> optionalCategory = categoryService.findById(id);
        if (optionalCategory.isPresent()) {
            category = optionalCategory.get();
        }

        model.addAttribute("category", category);
        return "admin/categories/edit";
    }

    @PostMapping("/update")
    public String update(Category category, @RequestParam("img") MultipartFile file) throws IOException {

        Category c = new Category();
        c = categoryService.findById(category.getId()).get();

        if(file.isEmpty()){
            category.setImage(c.getImage());
        } else{
            if(!c.getImage().equals("default.jpg")){
                uploadFileService.deleteImage(c.getImage());
            }
            String nameImage = uploadFileService.saveImage(file);
            category.setImage(nameImage);
        }

        categoryService.update(category);
        return "redirect:/category";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        Category c = new Category();
        c = categoryService.findById(id).get();

        if(!c.getImage().equals("default.jpg")){
            uploadFileService.deleteImage(c.getImage());
        }

        categoryService.delete(id);
        return "redirect:/category";
    }

}
