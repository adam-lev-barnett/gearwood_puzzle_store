package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.dtos.ProductSummaryDto;
import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;
import edu.barnett.gearwood_puzzle_store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /** Admins see the full product list; Everyone else only sees active products*/
    @GetMapping("/catalog")
    public String getProductCatalog(Model model, Authentication auth,
                                    @RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Category category,
                                    @RequestParam(required = false) Difficulty difficulty,
                                    @RequestParam(required = false) BigDecimal minPrice,
                                    @RequestParam(required = false) BigDecimal maxPrice) {

        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isSearch = (keyword != null && !keyword.isBlank())
                || category != null || difficulty != null
                || minPrice != null || maxPrice != null;

        List<ProductSummaryDto> products;
        if (isSearch) {
            products = productService.search(keyword, category, difficulty, minPrice, maxPrice);
            if (!isAdmin) {
                products = products.stream().filter(ProductSummaryDto::isActive).toList();
            }
        } else {
            products = isAdmin ? productService.getAll() : productService.getAllActive();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", Category.values());
        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedDifficulty", difficulty);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "productCatalog";
    }

    /** Individual product detail page*/
    @GetMapping("/{productCode}")
    public String getProduct(Model model, @PathVariable String productCode) {
        ProductSummaryDto product = productService.getByProductCode(productCode);
        model.addAttribute("product", product);

        return "productDetails";
    }

}
