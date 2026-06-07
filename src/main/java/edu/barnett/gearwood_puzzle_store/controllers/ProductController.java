package edu.barnett.gearwood_puzzle_store.controllers;

import edu.barnett.gearwood_puzzle_store.dtos.ProductAdminDto;
import edu.barnett.gearwood_puzzle_store.dtos.ProductCreateRequestDto;
import edu.barnett.gearwood_puzzle_store.dtos.ProductSummaryDto;
import edu.barnett.gearwood_puzzle_store.entities.Product;
import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;
import edu.barnett.gearwood_puzzle_store.services.ManufacturerService;
import edu.barnett.gearwood_puzzle_store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ManufacturerService manufacturerService;

    @Autowired
    public ProductController(ProductService productService, ManufacturerService manufacturerService) {
        this.productService = productService;
        this.manufacturerService = manufacturerService;
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

        // If all of the fields are empty, there's no search/filtering
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

    // ~~~~~~~ Admin: create ~~~~~~~~~~~~~~

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String getCreateProduct(Model model) {
        addFormOptions(model);
        return "productCreate";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String createProduct(@ModelAttribute ProductCreateRequestDto newProduct,
                                RedirectAttributes redirect,
                                Model model) {
        try {
            productService.createProduct(newProduct);
            redirect.addFlashAttribute("message", "Product created successfully");
            return "redirect:/products/" + newProduct.productCode();
        } catch (Exception e) {
            // Re-render the form (not a redirect) so the error + dropdown options are present.
            model.addAttribute("error", e.getMessage());
            addFormOptions(model);
            return "productCreate";
        }
    }

    // ~~~~~~~ Admin: edit ~~~~~~~~~~~~~~

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{productCode}/edit")
    public String getEditProduct(Model model, @PathVariable String productCode) {
        model.addAttribute("product", productService.getByProductCodeAsAdmin(productCode));
        addFormOptions(model);
        return "productEdit";
    }

    // POST, not PUT: HTML forms can only GET/POST, and Spring Boot's hidden-method filter is off by default.
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{productCode}/edit")
    public String editProduct(@PathVariable String productCode,
                              @ModelAttribute ProductCreateRequestDto updatedProduct,
                              RedirectAttributes redirect) {
        try {
            productService.updateProduct(updatedProduct);
            redirect.addFlashAttribute("message", "Product updated successfully");
            return "redirect:/products/" + updatedProduct.productCode();
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/products/" + productCode;
        }
    }

    // ~~~~~~~ Admin: delete ~~~~~~~~~~~~~~

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{productCode}/delete")
    public String deleteProduct(@PathVariable String productCode, RedirectAttributes redirect) {
        try {
            boolean deleted = productService.delete(productCode);
            redirect.addFlashAttribute("message", deleted
                    ? "Product deleted."
                    : "Product is in a cart or past order, so it was deactivated instead of deleted.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/products/catalog";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{productCode}/toggleActivation")
    public String toggleActivation(@PathVariable String productCode, RedirectAttributes redirect) {
        ProductAdminDto product = productService.getByProductCodeAsAdmin(productCode);
        if (product.summary().isActive()) {
            productService.deactivate(productCode);
            redirect.addFlashAttribute("message", "Product deactivated successfully");
        } else {
            productService.activate(productCode);
            redirect.addFlashAttribute("message", "Product activated successfully");
        }
        return "redirect:/products/{productCode}";
    }

    /** Shared option sources for the create/edit forms (manufacturer dropdown + enum selects). */
    private void addFormOptions(Model model) {
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());
        model.addAttribute("categories", Category.values());
        model.addAttribute("difficulties", Difficulty.values());
    }

}
