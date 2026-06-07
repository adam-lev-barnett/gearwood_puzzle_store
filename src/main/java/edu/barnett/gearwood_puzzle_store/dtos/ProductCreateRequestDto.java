package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.enums.Category;
import edu.barnett.gearwood_puzzle_store.enums.Difficulty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductCreateRequestDto(String productCode,
                                      String name,
                                      // Manufacturer NAME chosen from the dropdown; the service resolves it
                                      // to a Manufacturer entity. Keeps the DTO free of entity references.
                                      String manufacturer,
                                      Integer numberOfPieces,
                                      Difficulty difficulty,
                                      Category category,
                                      BigDecimal price,
                                      String shortDescription,
                                      String longDescription,
                                      LocalDate acquiredDate
                                      // active, featured, and primaryImgSource are intentionally NOT here:
                                      // the admin forms don't manage them (active is toggled via activate/
                                      // deactivate, featured is curated, images aren't uploaded). Binding them
                                      // here would clobber those values to false/null on every edit.
                                      ) { }
