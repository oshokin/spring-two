package ru.oshokin.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.oshokin.store.entities.OutcomingMessageJS;
import ru.oshokin.store.entities.Product;
import ru.oshokin.store.entities.ProductImage;
import ru.oshokin.store.interfaces.IWebSocketMessenger;
import ru.oshokin.store.services.CategoryService;
import ru.oshokin.store.services.ImageSaverService;
import ru.oshokin.store.services.ProductService;

import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;
    private CategoryService categoryService;
    private ImageSaverService imageSaverService;
    private IWebSocketMessenger wsEndPoint;

    @Autowired
    public void setWsEndPoint(IWebSocketMessenger wsEndPoint) {
        this.wsEndPoint = wsEndPoint;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setImageSaverService(ImageSaverService imageSaverService) {
        this.imageSaverService = imageSaverService;
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable(name = "id") Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            product = new Product();
            product.setId(0L);
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/edit-product";
    }

    @PostMapping("/edit")
    public String processProductAddForm(@Valid @ModelAttribute("product") Product product, BindingResult theBindingResult, Model model, @RequestParam("file") MultipartFile file) {
        if (product.getId() == 0 && productService.isProductWithTitleExists(product.getTitle())) {
            theBindingResult.addError(new ObjectError("product.title", "Товар с таким названием уже существует")); // todo не отображает сообщение
        }

        if (theBindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "edit-product";
        }

        if (!file.isEmpty()) {
            String pathToSavedImage = imageSaverService.saveFile(file);
            ProductImage productImage = new ProductImage();
            productImage.setPath(pathToSavedImage);
            productImage.setProduct(product);
            product.addImage(productImage);
        }

        productService.saveProduct(product);
        sendWebSocketMessage(String.format("%s добавлен. Хвала Господу!", product.getTitle()));
        return "redirect:/shop";
    }

    @Async
    private void sendWebSocketMessage(String message) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        wsEndPoint.sendMessage("/topic/messages", new OutcomingMessageJS(message));
    }

}
