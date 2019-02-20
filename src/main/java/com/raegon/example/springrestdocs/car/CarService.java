package com.raegon.example.springrestdocs.car;

public interface CarService {

    void create(Car car);

    Car read(Integer id);

    void update(Integer id, Car car);

    void delete(Integer id);

}
