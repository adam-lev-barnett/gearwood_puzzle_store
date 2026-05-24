package edu.barnett.gearwood_puzzle_store.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderData order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceUponPurchase;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotal;

    public OrderItem() {}

    public OrderItem(OrderData order, Product product, String productName,
                     BigDecimal unitPriceUponPurchase, Integer quantity, BigDecimal lineTotal) {
        this.order = order;
        this.product = product;
        this.productName = productName;
        this.unitPriceUponPurchase = unitPriceUponPurchase;
        this.quantity = quantity;
        this.lineTotal = lineTotal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OrderData getOrder() { return order; }
    public void setOrder(OrderData order) { this.order = order; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getUnitPriceUponPurchase() { return unitPriceUponPurchase; }
    public void setUnitPriceUponPurchase(BigDecimal unitPriceUponPurchase) { this.unitPriceUponPurchase = unitPriceUponPurchase; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getLineTotal() { return lineTotal; }
    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
}
