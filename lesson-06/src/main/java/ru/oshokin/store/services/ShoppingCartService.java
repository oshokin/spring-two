package ru.oshokin.store.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.oshokin.store.entities.OrderItem;
import ru.oshokin.store.entities.Product;
import ru.oshokin.store.mq.MessageQueuingService;
import ru.oshokin.store.mq.RabbitMQService;
import ru.oshokin.store.utils.ShoppingCart;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartService {

    private ProductService productService;
    private MessageQueuingService mqService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setMessageQueuingService(MessageQueuingService mqService) {
        this.mqService = mqService;
    }

    public ShoppingCart getCurrentCart(HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    public void setProductCount(HttpSession session, Long productId, Long quantity) {
        ShoppingCart cart = getCurrentCart(session);
        Product product = productService.getProductById(productId);
        cart.setQuantity(product, quantity);
    }

    public void setProductCount(HttpSession session, Product product, Long quantity) {
        ShoppingCart cart = getCurrentCart(session);
        cart.setQuantity(product, quantity);
    }

    public double getTotalCost(HttpSession session) {
        return getCurrentCart(session).getTotalCost();
    }

    public int getQuantityProduct(HttpSession session) {
        ShoppingCart cart = getCurrentCart(session);
        int count = 0;
        List<OrderItem> items = cart.getItems();
        for (int i = 0; i < cart.getItems().size(); i++) {
            count += items.get(i).getQuantity();
        }
        return count;
    }

    public void resetCart(HttpSession session) {
        session.removeAttribute("cart");
    }

    public void addToCart(HttpSession session, Long productId) {
        Product product = productService.getProductById(productId);
        addToCart(session, product);
    }

    public void addToCart(HttpSession session, Product product) {
        ShoppingCart cart = getCurrentCart(session);
        cart.add(product);
        try {
            mqService.sendMessage(String.format("В корзину добавлен товар \"%s\" за %.2f RUB/шт.", product.getTitle(), product.getPrice()));
        } catch (IOException e) {
            log.error("Uyy, no, ahora tendras que leerlo todo esto: {}", e.getMessage());
        }

    }

    public void removeFromCart(HttpSession session, Long productId) {
        Product product = productService.getProductById(productId);
        removeFromCart(session, product);
    }

    public void removeFromCart(HttpSession session, Product product) {
        ShoppingCart cart = getCurrentCart(session);
        cart.remove(product);
    }

    public void recalculate(HttpSession session) {
        getCurrentCart(session).recalculate();
    }

}
