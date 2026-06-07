package edu.barnett.gearwood_puzzle_store.enums;

public enum Category {
    MECHANICAL("Mechanical"),
    HISTORICAL("Historical"),
    NAUTICAL("Nautical"),
    SCI_FI("Sci-fi"),
    FANTASY("Fantasy"),
    ARCHITECTURE("Architecture"),
    DECORATIVE("Decorative");

    // To display categories cleanly to users for UI
    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
