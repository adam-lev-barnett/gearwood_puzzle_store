package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.dtos.ProductSummaryDto;
import edu.barnett.gearwood_puzzle_store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

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
    public String getProductCatalog(Model model, Authentication auth) {

        List<ProductSummaryDto> products =
                auth.getAuthorities()
                .stream()
                .anyMatch(
                        a -> a.getAuthority().equals("ROLE_ADMIN")) ?
                        productService.getAll() :
                        productService.getAllActive();

        model.addAttribute(products);

        return "ProductCatalog";
    }

    /** Individual product detail page*/
    @GetMapping("/{productCode}")
    public String getProduct(Model model, @PathVariable String productCode) {
        ProductSummaryDto product = productService.getByProductCode(productCode);
        model.addAttribute(product);

        return "ProductDetails";
    }

}
