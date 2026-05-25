package edu.barnett.gearwood_puzzle_store.entities;

import edu.barnett.gearwood_puzzle_store.exceptions.BadParameterException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // This needs to stay at 0 so that we can set a conditional that, when the quantity reaches 0, it's removed from the cart
    @Min(1)
    @Column(nullable = false)
    private Integer quantity;

    public CartItem() {}

    public CartItem(User user, Product product, Integer quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) {
        if (quantity <= 0) throw new BadParameterException("Quantity must be greater than zero");
        this.quantity = quantity;
    }
}
