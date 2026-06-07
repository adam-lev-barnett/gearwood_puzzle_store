package edu.barnett.gearwood_puzzle_store.entities;

import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productCode;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @Column(nullable = false)
    private Integer numberOfPieces;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private BigDecimal price;

    @Column
    private String shortDescription;

    @Column
    private String longDescription;

    @Column(nullable = false)
    private boolean active;

    @Column
    private String primaryImgSource;

    @Column
    private Boolean featured;

    @Column(nullable = false)
    private LocalDate acquiredDate;

    public Product() {}

    /** Default acquiredDate to the current date if it wasn't set explicitly. */
    @PrePersist
    private void onCreate() {
        if (acquiredDate == null) {
            acquiredDate = LocalDate.now();
        }
    }

    public Product(String productCode, String name, Manufacturer manufacturer, Integer numberOfPieces,
                   Difficulty difficulty, Category category, BigDecimal price,
                   String shortDescription, String longDescription, boolean active, String primaryImgSource, boolean featured) {
        this.productCode = productCode;
        this.name = name;
        this.manufacturer = manufacturer;
        this.numberOfPieces = numberOfPieces;
        this.difficulty = difficulty;
        this.category = category;
        this.price = price;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.active = active;
        this.primaryImgSource = primaryImgSource;
        this.featured = featured;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Manufacturer getManufacturer() { return manufacturer; }
    public void setManufacturer(Manufacturer manufacturer) { this.manufacturer = manufacturer; }

    public Integer getNumberOfPieces() { return numberOfPieces; }
    public void setNumberOfPieces(Integer numberOfPieces) { this.numberOfPieces = numberOfPieces; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getLongDescription() { return longDescription; }
    public void setLongDescription(String longDescription) { this.longDescription = longDescription; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getPrimaryImage() {
        return this.primaryImgSource;
    }
    public void setPrimaryImage(String imageSource) {
        this.primaryImgSource = imageSource;
    }

    // Column is nullable (older rows may predate this field), so treat null as "not featured".
    public boolean isFeatured() { return Boolean.TRUE.equals(this.featured); }
    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public LocalDate getAcquiredDate() { return acquiredDate; }
    public void setAcquiredDate(LocalDate acquiredDate) { this.acquiredDate = acquiredDate; }


}
