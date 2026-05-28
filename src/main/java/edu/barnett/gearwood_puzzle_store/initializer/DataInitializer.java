package edu.barnett.gearwood_puzzle_store.initializer;

import edu.barnett.gearwood_puzzle_store.entities.Manufacturer;
import edu.barnett.gearwood_puzzle_store.entities.Product;
import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;
import edu.barnett.gearwood_puzzle_store.repositories.ManufacturerRepository;
import edu.barnett.gearwood_puzzle_store.repositories.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer {

    private final ManufacturerRepository manufacturerRepo;
    private final ProductRepository productRepo;

    public DataInitializer(ManufacturerRepository manufacturerRepo, ProductRepository productRepo) {
        this.manufacturerRepo = manufacturerRepo;
        this.productRepo = productRepo;
    }

    @PostConstruct
    public void init() {
        if (manufacturerRepo.count() != 0 || productRepo.count() != 0) {
            System.out.println("Data already present - not executing initializer");
            return;
        }
        System.out.println("Initializing product data");

        Manufacturer timbertech  = manufacturerRepo.save(new Manufacturer("TimberTech Models",   "123 Maple St, Denver, CO 80202",        "contact@timbertechmodels.com", "303-555-0141"));
        Manufacturer ironwood    = manufacturerRepo.save(new Manufacturer("Ironwood Creations",   "456 Oak Ave, Chicago, IL 60607",         "info@ironwoodcreations.com",   "312-555-0182"));
        Manufacturer nordiccraft = manufacturerRepo.save(new Manufacturer("NordicCraft",          "789 Fjord Rd, Seattle, WA 98101",        "support@nordiccraft.com",      "206-555-0193"));
        Manufacturer orbital     = manufacturerRepo.save(new Manufacturer("Orbital Labs",         "101 Space Park Dr, Houston, TX 77058",   "hello@orbitallabs.com",        "713-555-0114"));
        Manufacturer arcane      = manufacturerRepo.save(new Manufacturer("ArcaneWorks",          "202 Mystic Ln, Salem, MA 01970",         "sales@arcaneworks.com",        "978-555-0155"));
        Manufacturer skyline     = manufacturerRepo.save(new Manufacturer("Skyline Structures",   "303 Tower Blvd, New York, NY 10001",     "info@skylinestructures.com",   "212-555-0166"));
        Manufacturer luma        = manufacturerRepo.save(new Manufacturer("LumaScenes",           "404 Light St, San Diego, CA 92101",      "contact@lumascenes.com",       "619-555-0177"));

        productRepo.saveAll(List.of(
            new Product("P1001", "Luminous Globe 3D Wooden Puzzle", timbertech, 180, Difficulty.MEDIUM, Category.MECHANICAL,
                new BigDecimal("59.99"),
                "Rotating wooden globe with gears and warm LED lighting for display.",
                "A beautifully engineered wooden globe featuring a visible gear system that powers smooth rotation. The integrated LED lighting adds a warm ambient glow, making it both a mechanical marvel and a decorative centerpiece for any room.",
                true, null),

            new Product("P1002", "Steam Engine Mechanical 3D Wooden Puzzle", ironwood, 320, Difficulty.HARD, Category.MECHANICAL,
                new BigDecimal("64.99"),
                "Detailed steam engine model with moving pistons and crankshaft parts.",
                "This detailed wooden model recreates the power of a vintage steam locomotive with fully visible pistons and crankshaft movement. Designed for advanced builders, it offers a rewarding assembly experience and a striking mechanical display.",
                true, null),

            new Product("P1003", "Pendulum Clock Wooden Mechanical Puzzle", skyline, 210, Difficulty.MEDIUM, Category.MECHANICAL,
                new BigDecimal("54.99"),
                "Functional wooden clock featuring exposed gears and moving parts.",
                "A precision-crafted wooden clock that showcases its inner workings through exposed gears. Combining functionality with craftsmanship, this model provides both a practical timepiece and an engaging mechanical build.",
                true, null),

            new Product("P1004", "Skyrail Marble Run", timbertech, 290, Difficulty.MEDIUM, Category.MECHANICAL,
                new BigDecimal("49.99"),
                "Hand-cranked marble run with continuous motion and looping tracks.",
                "An interactive marble run powered by a hand-crank mechanism that continuously cycles marbles through a dynamic track. This model highlights motion and engineering principles while providing an engaging hands-on experience.",
                true, null),

            new Product("P1005", "Velocity Racer Car", ironwood, 160, Difficulty.EASY, Category.MECHANICAL,
                new BigDecimal("39.99"),
                "Rubber-band powered wooden race car built for speed and simplicity.",
                "A sleek wooden race car powered by a rubber-band motor, designed for quick assembly and fun performance. Its lightweight structure and smooth motion make it ideal for beginners and younger builders.",
                true, null),

            new Product("P1006", "Nordic Dragon Longship", nordiccraft, 240, Difficulty.MEDIUM, Category.HISTORICAL,
                new BigDecimal("59.99"),
                "Viking-inspired longship with engraved wooden panels and detail.",
                "Inspired by traditional Viking longships, this model features intricate engravings and a bold silhouette. It offers a balance of historical design and satisfying construction, resulting in a display-worthy centerpiece.",
                true, null),

            new Product("P1007", "Seahaven Barque", nordiccraft, 300, Difficulty.HARD, Category.NAUTICAL,
                new BigDecimal("64.99"),
                "Detailed sailing ship with layered decks and intricate rigging work.",
                "A highly detailed sailing vessel complete with layered decks and intricate rigging. This challenging build rewards patience with a stunning nautical model ideal for collectors and enthusiasts.",
                true, null),

            new Product("P1008", "Sailing Ship Model 3D Wooden Puzzle", ironwood, 280, Difficulty.HARD, Category.HISTORICAL,
                new BigDecimal("69.99"),
                "Large wooden warship with multi-deck design and ornate details.",
                "This grand war galleon features multiple decks, detailed cannons, and ornate woodwork. Designed for experienced builders, it captures the scale and elegance of historic naval vessels.",
                true, null),

            new Product("P1009", "Harbor Tugboat", timbertech, 150, Difficulty.EASY, Category.NAUTICAL,
                new BigDecimal("42.99"),
                "Compact tugboat model with simple build and charming design details.",
                "A compact and charming tugboat model that emphasizes simplicity and detail. Its approachable build makes it perfect for beginners while still delivering an attractive finished piece.",
                true, null),

            new Product("P1010", "Orbital Explorer Shuttle", orbital, 410, Difficulty.HARD, Category.SCI_FI,
                new BigDecimal("74.99"),
                "Futuristic space shuttle with display stand and moving components.",
                "A futuristic shuttle model with a detailed structure and a themed launch stand. This advanced build highlights engineering complexity and is ideal for space and STEM enthusiasts.",
                true, null),

            new Product("P1011", "Galactic Rover", orbital, 230, Difficulty.MEDIUM, Category.SCI_FI,
                new BigDecimal("57.99"),
                "Mars-style rover model with articulated wheels and rugged frame.",
                "Designed after planetary exploration vehicles, this rover features articulated wheels and a rugged frame. It offers an engaging build and a dynamic finished model suitable for display.",
                true, null),

            new Product("P1012", "Dragonfire Siege Engine", arcane, 260, Difficulty.MEDIUM, Category.FANTASY,
                new BigDecimal("52.99"),
                "Medieval-style ballista with working firing mechanism included.",
                "A fantasy-inspired siege engine with a working firing mechanism that launches small projectiles. This model blends mechanical action with imaginative design for a fun and interactive experience.",
                true, null),

            new Product("P1013", "Arcane Codex Box", arcane, 190, Difficulty.MEDIUM, Category.FANTASY,
                new BigDecimal("48.99"),
                "Puzzle box with hidden locking system and secret compartments inside.",
                "A mysterious wooden box featuring a hidden locking mechanism that must be solved to open. Combining puzzle-solving with craftsmanship, it serves as both a brain teaser and a functional storage item.",
                true, null),

            new Product("P1014", "Grand Windmill Tower", skyline, 220, Difficulty.MEDIUM, Category.ARCHITECTURE,
                new BigDecimal("51.99"),
                "Rustic windmill tower with rotating blades and gear-driven motion.",
                "A rustic windmill model featuring rotating blades powered by a simple gear system. Its architectural design and motion elements make it both decorative and mechanically interesting.",
                true, null),

            new Product("P1015", "Parisian Clock Tower", skyline, 260, Difficulty.HARD, Category.ARCHITECTURE,
                new BigDecimal("58.99"),
                "European-style clock tower with ornate wooden architectural detail.",
                "An elegant clock tower inspired by classic European architecture, featuring detailed facades and structural elements. This model offers a challenging build with a refined final appearance.",
                true, null),

            new Product("P1016", "Skyline Observation Tower", skyline, 200, Difficulty.MEDIUM, Category.ARCHITECTURE,
                new BigDecimal("49.99"),
                "Modern observation tower with layered design and structural detail.",
                "A sleek observation tower with a modern, layered construction style. This model emphasizes symmetry and structure, making it a visually striking addition to any collection.",
                true, null),

            new Product("P1017", "Melody Music Box", luma, 140, Difficulty.EASY, Category.DECORATIVE,
                new BigDecimal("44.99"),
                "Hand-cranked music box with visible internal moving mechanism.",
                "A decorative music box with a visible internal mechanism that plays a tune when cranked. This simple yet elegant model combines motion, sound, and visual appeal.",
                true, null),

            new Product("P1018", "Mechanical Butterfly Display", luma, 120, Difficulty.EASY, Category.DECORATIVE,
                new BigDecimal("39.99"),
                "Decorative butterfly model with flapping wings powered by motion.",
                "A delicate butterfly model that brings motion to life with flapping wings powered by a simple mechanism. Its lightweight design and artistic style make it an eye-catching display piece.",
                true, null),

            new Product("P1019", "Vintage Camera Model", luma, 180, Difficulty.MEDIUM, Category.DECORATIVE,
                new BigDecimal("46.99"),
                "Retro camera replica with moving lens and mechanical components.",
                "A nostalgic camera replica featuring a movable lens and mechanical components. This model blends vintage aesthetics with interactive features for a unique building experience.",
                true, null),

            new Product("P1020", "Illuminated Book Nook Diorama", luma, 220, Difficulty.MEDIUM, Category.DECORATIVE,
                new BigDecimal("55.99"),
                "Bookshelf insert diorama with lighting and detailed miniature scene.",
                "A miniature diorama designed to fit between books, complete with built-in lighting and intricate details. This model creates a cozy, immersive scene that enhances any bookshelf display.",
                true, null),

            new Product("P1021", "Atlas Airship", orbital, 275, Difficulty.HARD, Category.SCI_FI,
                new BigDecimal("67.99"),
                "Retro-futuristic airship with propellers, fins, and display-worthy detail.",
                "The Atlas Airship blends steampunk and science-fiction design with layered wooden panels, rotating propellers, and a suspended display frame. It delivers a challenging build and a distinctive silhouette that stands out in any puzzle collection.",
                true, null),

            new Product("P1022", "Summit Cable Car", skyline, 170, Difficulty.EASY, Category.ARCHITECTURE,
                new BigDecimal("43.99"),
                "Mountain cable car model with clean lines and a scenic display base.",
                "This charming cable car kit features a compact build, supporting frame, and a scenic station base inspired by alpine travel. Its approachable assembly makes it a great entry point while still producing a polished display piece.",
                true, null),

            new Product("P1023", "Runekeeper Treasure Chest", arcane, 205, Difficulty.MEDIUM, Category.FANTASY,
                new BigDecimal("50.99"),
                "Locking treasure chest puzzle with engraved panels and hidden storage.",
                "Built around a secret-opening mechanism, this fantasy chest combines decorative rune engravings with functional storage space. It offers a satisfying mix of puzzle-solving, assembly, and finished display appeal.",
                true, null),

            new Product("P1024", "Midnight Planetarium", luma, 195, Difficulty.MEDIUM, Category.DECORATIVE,
                new BigDecimal("56.99"),
                "Tabletop planetarium with star cutouts and a soft internal glow effect.",
                "Designed as a decorative astronomy-inspired model, the Midnight Planetarium uses layered wooden shells and light openings to create a warm celestial display. It works especially well as ambient decor on a desk, shelf, or nightstand.",
                true, null),

            new Product("P1025", "Coastal Lighthouse", timbertech, 185, Difficulty.MEDIUM, Category.NAUTICAL,
                new BigDecimal("47.99"),
                "Classic lighthouse display with spiral structure and seaside character.",
                "This lighthouse model captures the charm of coastal architecture with a tiered tower, window detailing, and a textured base. The finished build makes a clean nautical accent while offering a balanced mid-level assembly challenge.",
                true, null),

            new Product("P1026", "Siege Ram Cart", ironwood, 245, Difficulty.MEDIUM, Category.HISTORICAL,
                new BigDecimal("53.99"),
                "Rolling siege ram cart with armored frame and medieval battlefield style.",
                "Inspired by historic siege equipment, this model features a wheeled chassis, reinforced body panels, and a suspended battering ram. The finished piece delivers both motion-focused detail and a strong medieval visual theme.",
                true, null),

            new Product("P1027", "Polar Research Submarine", orbital, 310, Difficulty.HARD, Category.SCI_FI,
                new BigDecimal("68.99"),
                "Exploration submarine model with fins, propeller, and layered hull design.",
                "The Polar Research Submarine presents a deep-sea exploration theme with a segmented hull, stabilizing fins, and mechanical propeller details. It is designed for builders who enjoy complex shapes and a more technical final display.",
                true, null),

            new Product("P1028", "Valewood Carousel", luma, 250, Difficulty.MEDIUM, Category.DECORATIVE,
                new BigDecimal("58.99"),
                "Decorative carousel with rotating platform and ornate festival styling.",
                "This carousel kit combines graceful structure with motion, using a geared base to rotate the central platform after assembly. Its layered roof, carved details, and whimsical presentation make it one of the most display-friendly models in the catalog.",
                true, null),

            new Product("P1029", "Longbow Ballista Tower", arcane, 270, Difficulty.HARD, Category.FANTASY,
                new BigDecimal("61.99"),
                "Fantasy defense tower with ballista platform and fortified woodwork.",
                "Featuring a raised firing platform, defensive railings, and castle-inspired construction, this kit expands the fantasy lineup with a more architectural build. It offers a detailed silhouette and a strong sense of world-building once completed.",
                true, null),

            new Product("P1030", "Heritage Tram", nordiccraft, 215, Difficulty.MEDIUM, Category.HISTORICAL,
                new BigDecimal("52.49"),
                "Vintage tramcar model with window detail and classic city transit style.",
                "The Heritage Tram brings old-world street transit to life with a carefully shaped carriage body, detailed window framing, and a nostalgic urban design. It provides a satisfying medium-difficulty build and a distinctive finished profile.",
                true, null)
        ));
    }
}
