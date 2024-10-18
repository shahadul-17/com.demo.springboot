package com.demo.springboot.animal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(scopeName = "singleton")
public class AnimalService {

    @Autowired
    private AnimalRepository repository;

    public AnimalDto createAnimal(final AnimalDto animal) {
        final var _animal = AnimalMapper.INSTANCE.fromDto(animal);
        final var savedAnimal = repository.save(_animal);

        return AnimalMapper.INSTANCE.toDto(savedAnimal);
    }

}
