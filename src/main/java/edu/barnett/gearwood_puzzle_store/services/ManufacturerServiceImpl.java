package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.ManufacturerDto;
import edu.barnett.gearwood_puzzle_store.repositories.ManufacturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepo;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepo) {
        this.manufacturerRepo = manufacturerRepo;
    }

    @Override
    public List<ManufacturerDto> getAllManufacturers() {
        return manufacturerRepo.findAll()
                .stream()
                .map(ManufacturerDto::new)
                .toList();
    }
}
