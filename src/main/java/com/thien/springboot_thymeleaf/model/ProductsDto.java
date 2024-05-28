package com.thien.springboot_thymeleaf.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class ProductsDto {

    @NotEmpty(message = "Name is required!")
//    @Min(value = 0, message = "Name must be at least 3 characters!")
    private String name;
    @NotEmpty(message = "Category is required!")
//    @Min(value = 3, message = "Category must be at least 3 characters!")
    private String category;
    @NotEmpty(message = "Brand is required!")
//    @Min(value = 3, message = "Brand must be at least 3 characters!")
    private String brand;
    @Min(0)
    private double price;
    @Size(min = 10, message = "The description must be at least 10 characters!")
    @Size(max = 200, message = "The description cannot exceed 200 characters!")
    private String description;
    private MultipartFile imageFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
