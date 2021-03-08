package ru.oshokin.store;

import org.junit.*;
import static org.junit.Assert.assertEquals;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import ru.oshokin.store.entities.Product;
import ru.oshokin.store.entities.OrderItem;
import ru.oshokin.store.mq.DummyMQService;
import ru.oshokin.store.services.ShoppingCartService;

import javax.servlet.http.HttpSession;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShoppingCartTests {

    HttpSession session;
    ShoppingCartService cartService;
    Product product;

    @Before
    public void initialize() {
        session = new MockHttpSession();
        cartService = new ShoppingCartService();
        cartService.setMessageQueuingService(new DummyMQService());

        product = new Product();
        product.setId(0L);
        product.setTitle("Мыло дегтярное \"Пушистые ягодицы\"");
        product.setPrice(666);
    }

    @Test
    public void productAdditionTest() {
        cartService.addToCart(session, product);
        OrderItem firstItem = cartService.getCurrentCart(session).getItems().get(0);

        assertEquals((Long) 1L, firstItem.getQuantity());
        assertEquals((Long) 0L, firstItem.getId());
        assertEquals("PREPARE URANUS, THE SOAP IS IN THE WAY!", "Мыло дегтярное \"Пушистые ягодицы\"", firstItem.getProduct().getTitle());
    }

    @Test
    public void productDeletionTest() {
        cartService.addToCart(session, product);
        assertEquals(1, cartService.getQuantityProduct(session));

        cartService.removeFromCart(session, product);
        assertEquals(0, cartService.getQuantityProduct(session));
    }

}