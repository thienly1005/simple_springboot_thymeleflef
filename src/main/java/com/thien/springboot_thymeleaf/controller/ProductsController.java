package com.thien.springboot_thymeleaf.controller;

import com.thien.springboot_thymeleaf.model.Product;
import com.thien.springboot_thymeleaf.model.ProductsDto;
import com.thien.springboot_thymeleaf.services.ProductsRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductsRepository productsRepository;

    @GetMapping({"", "/"})
    public String showProductList(Model model){
        // sort giam dan theo id
//        List<Product> products = productsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<Product> products = productsRepository.findAll();
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping({"/create"})
    public String showCreateProduct(Model model){
        ProductsDto productsDto = new ProductsDto();
        model.addAttribute("productsDto", productsDto);
        return "products/CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute ProductsDto productsDto,
    BindingResult result){
        if(productsDto.getImageFile().isEmpty()){
            result.addError(new FieldError("productsDto", "imageFile", "The image file is required!"));
        }

        if(result.hasErrors()) return "products/CreateProduct";

        // save new image file
        MultipartFile newImage = productsDto.getImageFile();
        Date createdAt = new Date();
        String storageImageFile = createdAt.getTime() + '_' + newImage.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = newImage.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageImageFile),
                        StandardCopyOption.REPLACE_EXISTING);
            }

        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }

        Product product = new Product();
        product.setName(productsDto.getName());
        product.setBrand(productsDto.getBrand());
        product.setCategory(productsDto.getCategory());
        product.setPrice(productsDto.getPrice());
        product.setCreatedAt(createdAt);
        product.setDescription(productsDto.getDescription());
        product.setImageFileName(storageImageFile);

        productsRepository.save(product);

        return "redirect:/products";
    }

    @GetMapping({"/edit"})
    public String showEditPage(Model model, @RequestParam int id){
        try {
            Product product = productsRepository.findById(id).get();
            model.addAttribute("product", product);

            ProductsDto productsDto = new ProductsDto();
            productsDto.setName(product.getName());
            productsDto.setBrand(product.getBrand());
            productsDto.setCategory(productsDto.getCategory());
            productsDto.setPrice(product.getPrice());
            productsDto.setDescription(productsDto.getDescription());
//            productsDto.setImageFile(product.getImageFileName());
            model.addAttribute("productsDto", productsDto);
        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/products";
        }
        return "products/EditProduct";
    }

    @PostMapping("/edit")
    public String createProduct(Model model, @RequestParam int id, @Valid @ModelAttribute ProductsDto productsDto,
                                BindingResult result){
        try {
            Product product = productsRepository.findById(id).get();
            model.addAttribute("product", product);
            if (result.hasErrors()) return "products/EditProduct";

            if(!productsDto.getImageFile().isEmpty()){
                //delete old image
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());

                try {
                    Files.delete(oldImagePath);
                }catch (Exception e){
                    System.out.println("Exception: " + e.getMessage());
                }

                MultipartFile newImage = productsDto.getImageFile();
                Date createdAt = new Date();
                String storageImageFile = createdAt.getTime() + '_' + newImage.getOriginalFilename();

                try (InputStream inputStream = newImage.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageImageFile),
                            StandardCopyOption.REPLACE_EXISTING);
                }

                product.setImageFileName(storageImageFile);
            }

            product.setName(productsDto.getName());
            product.setBrand(productsDto.getBrand());
            product.setCategory(productsDto.getCategory());
            product.setPrice(product.getPrice());
            product.setDescription(productsDto.getDescription());

            productsRepository.save(product);
        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id){
        try {
            Product product = productsRepository.findById(id).get();

            String uploadDir = "public/images/";
            Path ImageFilePath = Paths.get(uploadDir + product.getImageFileName());

            try {
                Files.delete(ImageFilePath);
            }catch (Exception e){
                System.out.println("Exception: " + e.getMessage());
            }

            productsRepository.delete(product);

        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
        return "redirect:/products";
    }

}
