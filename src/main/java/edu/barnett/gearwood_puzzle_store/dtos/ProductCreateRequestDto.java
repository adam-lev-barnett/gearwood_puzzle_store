package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductCreateRequestDto(String productCode,
                                      String name,
                                      // Manufacturer name chosen from dropdown to simplify product creation so admin doesn't need to create a new manufacturer if one doesn't exist - out of project scope
                                      String manufacturer,
                                      Integer numberOfPieces,
                                      Difficulty difficulty,
                                      Category category,
                                      BigDecimal price,
                                      String shortDescription,
                                      String longDescription,
                                      LocalDate acquiredDate
                                      ) { }
