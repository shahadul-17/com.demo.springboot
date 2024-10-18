package com.demo.springboot.animal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v{version}/animal")
public class AnimalController {

    @Autowired
    private AnimalService service;

    @PostMapping
    public ResponseEntity<AnimalDto> createAnimal(
            @PathVariable String version,
            @RequestBody AnimalDto animal
    ) {
        final var savedAnimal = service.createAnimal(animal);

        return ResponseEntity.status(HttpStatus.OK).body(savedAnimal);
    }

}
