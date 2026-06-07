package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.ManufacturerDto;

import java.util.List;

public interface ManufacturerService {
    /** All manufacturers, used to populate the admin product-form dropdown. */
    List<ManufacturerDto> getAllManufacturers();
}
