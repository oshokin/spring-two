package ru.oshokin.store.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.oshokin.store.entities.DeliveryAddress;
import ru.oshokin.store.entities.OutcomingMessageJS;
import ru.oshokin.store.entities.Order;
import ru.oshokin.store.entities.User;
import ru.oshokin.store.interfaces.IWebSocketMessenger;
import ru.oshokin.store.rabbitmq.RabbitMQAgent;
import ru.oshokin.store.services.*;
import ru.oshokin.store.utils.CommonUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/shop")
public class ShopController {
    private static final int INITIAL_PAGE = 1;
    private static final int PAGE_SIZE = 5;

    private MailService mailService;
    private UserService userService;
    private OrderService orderService;
    private ProductService productService;
    private ShoppingCartService shoppingCartService;
    private DeliveryAddressService deliverAddressService;
    private IWebSocketMessenger wsEndPoint;
    private RabbitMQAgent rabbitMQAgent;

    @Autowired
    public void setControllerWs(IWebSocketMessenger wsEndPoint) {
        this.wsEndPoint = wsEndPoint;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setDeliverAddressService(DeliveryAddressService deliverAddressService) {
        this.deliverAddressService = deliverAddressService;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setRabbitMQAgent(RabbitMQAgent rabbitMQAgent) {
        this.rabbitMQAgent = rabbitMQAgent;
    }

    @GetMapping
    public String shopPage(Model model, @RequestParam Map<String, String> parameters) {
        final int defaultParametersCount = 2;
        Map<String, Object> parsedParameters = new HashMap<>(parameters.size() + defaultParametersCount);

        //дефолтные параметры
        parsedParameters.put("page", CommonUtils.getIntegerOrDefault(parameters.get("page"), INITIAL_PAGE));
        parsedParameters.put("size", PAGE_SIZE);

        //пользовательские параметры
        parsedParameters.put("word", parameters.get("word"));
        parsedParameters.put("min", CommonUtils.castDouble(parameters.get("min")));
        parsedParameters.put("max", CommonUtils.castDouble(parameters.get("max")));

        model.addAttribute("products", productService.applyFilter(parsedParameters));

        return "shop-page";
    }

    //С каждым добавлением еще одного параметра в метод,
    //1 маленький Стив Макконнелл (вместе с Робертом Мартином) тихо плачет в углу :)
    //@GetMapping
    //public String shopPage(Model model,
    //					   @RequestParam(value = "page") Optional<Integer> page,
    //					   @RequestParam(value = "word", required = false) String word,
    //					   @RequestParam(value = "min", required = false) Double min,
    //					   @RequestParam(value = "max", required = false) Double max) {
    //	final int currentPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
    //
    //	Specification<Product> spec = Specification.where(null);
    //	StringBuilder filters = new StringBuilder();
    //	if (word != null) {
    //		spec = spec.and(ProductSpecs.titleContains(word));
    //		filters.append("&word=" + word);
    //	}
    //	if (min != null) {
    //		spec = spec.and(ProductSpecs.priceGreaterThanOrEq(min));
    //		filters.append("&min=" + min);
    //	}
    //	if (max != null) {
    //		spec = spec.and(ProductSpecs.priceLesserThanOrEq(max));
    //		filters.append("&max=" + max);
    //	}
    //
    //	Page<Product> products = productsService.getProductsWithPagingAndFiltering(currentPage, PAGE_SIZE, spec);
    //
    //	model.addAttribute("products", products.getContent());
    //	model.addAttribute("page", currentPage);
    //	model.addAttribute("totalPage", products.getTotalPages());
    //
    //	model.addAttribute("filters", filters.toString());
    //
    //	model.addAttribute("min", min);
    //	model.addAttribute("max", max);
    //	model.addAttribute("word", word);
    //	return "shop-page";
    //}

    @GetMapping("/cart/add/{id}")
    public String addProductToCart(Model model, @PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        shoppingCartService.addToCart(httpServletRequest.getSession(), id);
        String finalCount = String.valueOf(shoppingCartService.getQuantityProduct(httpServletRequest.getSession()));
        sendWebSocketMessage(String.format("Товаров в корзинке: %s", finalCount));
        String referrer = httpServletRequest.getHeader("referer");
        return "redirect:" + referrer;
    }

    @Async
    private void sendWebSocketMessage(String message) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        wsEndPoint.sendMessage("/topic/messages", new OutcomingMessageJS(message));
    }

    @GetMapping("/order/fill")
    public String orderFill(Model model, HttpServletRequest httpServletRequest, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName());
        Order order = orderService.makeOrder(shoppingCartService.getCurrentCart(httpServletRequest.getSession()), user);
        List<DeliveryAddress> deliveryAddresses = deliverAddressService.getUserAddresses(user.getId());
        model.addAttribute("order", order);
        model.addAttribute("deliveryAddresses", deliveryAddresses);
        return "order-filler";
    }

    @PostMapping("/order/confirm")
    public String orderConfirm(Model model, HttpServletRequest httpServletRequest, @ModelAttribute(name = "order") Order orderFromFrontend, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName());
        Order order = orderService.makeOrder(shoppingCartService.getCurrentCart(httpServletRequest.getSession()), user);
        order.setDeliveryAddress(orderFromFrontend.getDeliveryAddress());
        order.setPhoneNumber(orderFromFrontend.getPhoneNumber());
        order.setDeliveryDate(LocalDateTime.now().plusDays(7));
        order.setDeliveryPrice(0.0);
        order = orderService.saveOrder(order);
        model.addAttribute("order", order);
        showCartMessages();
        return "order-filler";
    }

    @GetMapping("/order/result/{id}")
    public String orderConfirm(Model model, @PathVariable(name = "id") Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        // todo ждем до оплаты, проверка безопасности и проблема с повторной отправкой письма сделать одноразовый вход
        User user = userService.findByUserName(principal.getName());
        Order confirmedOrder = orderService.findById(id);
        if (!user.getId().equals(confirmedOrder.getUser().getId())) {
            return "redirect:/";
        }
        mailService.sendOrderMail(confirmedOrder);
        model.addAttribute("order", confirmedOrder);
        return "order-result";
    }

    @Async
    private void showCartMessages() {
        try {
            while (rabbitMQAgent.hasNext()) {
                String message = rabbitMQAgent.getMessage();
                log.info(message);
            }
        } catch (IOException e) {
            log.error("We've just killed RabbitMQ: {}", e.getMessage());
        }
    }

}
