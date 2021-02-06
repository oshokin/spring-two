package ru.oshokin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.oshokin.entities.Product;
import ru.oshokin.services.ProductService;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping("/list")
    public String showProductsList(Model model) {
        List<Product> allProduct = productService.getAllProducts();
        model.addAttribute("productsList", allProduct);
        return "products-list";
    }

}
