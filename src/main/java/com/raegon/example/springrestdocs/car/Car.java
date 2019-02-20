package com.raegon.example.springrestdocs.car;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class Car {

    public int id;

    @NotNull
    @Size(min = 1, max = 100)
    public String name;

    public String company;

    @Builder
    private Car(int id, String name, String company) {
        this.id = id;
        this.name = name;
        this.company = company;
    }

}
