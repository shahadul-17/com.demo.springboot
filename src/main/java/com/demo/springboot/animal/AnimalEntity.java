package com.demo.springboot.animal;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash
public class AnimalEntity {

    @Id
    @Column(name = "id")
    private String animalId;
    private String name;
    private String color;

    public void setName(String name) {
        this.name = name;

        setAnimalId(this.name + "_" + getColor());
    }

    public void setColor(String color) {
        this.color = color;

        setAnimalId(getName() + "_" + this.color);
    }
}
