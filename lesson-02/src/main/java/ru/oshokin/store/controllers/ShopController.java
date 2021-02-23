package ru.oshokin.store.controllers;

import ru.oshokin.store.entities.Product;
import ru.oshokin.store.services.ProductsService;
import ru.oshokin.store.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private static final int INITIAL_PAGE = 1;
    private static final int PAGE_SIZE = 5;

    private ProductsService productsService;

    @Autowired
    public void setProductsService(ProductsService productsService) {
        this.productsService = productsService;
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

        model.addAllAttributes(productsService.applyFilter(parsedParameters));

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

    @GetMapping("/product_info/{id}")
    public String productPage(Model model, @PathVariable(value = "id") Long id) {
        Product product = productsService.findById(id);
        model.addAttribute("product", product);
        return "product-page";
    }
}
