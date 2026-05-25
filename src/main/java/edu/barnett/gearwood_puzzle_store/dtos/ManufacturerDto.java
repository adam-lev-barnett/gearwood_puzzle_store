package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.entities.Manufacturer;

public record ManufacturerDto(String name, String address, String contactEmail, String contactPhone) {

    public ManufacturerDto(Manufacturer manufacturer) {
        this(
                manufacturer.getName(),
                manufacturer.getAddress(),
                manufacturer.getContactEmail(),
                manufacturer.getContactPhone()
        );
    }
}
