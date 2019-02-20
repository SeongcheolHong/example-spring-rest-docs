package com.raegon.example.springrestdocs.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/car")
public class CarController {

    private final CarService service;

    @Autowired
    public CarController(CarService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Car get(@PathVariable Integer id) {
        return service.read(id);
    }

    @PostMapping
    public ResponseEntity<Car> create(@RequestBody Car car, UriComponentsBuilder ucb) {
        service.create(car);
        Car result = service.read(car.getId());
        UriComponents uc = ucb.path("/car/{id}").buildAndExpand(car.getId());
        return ResponseEntity.created(uc.toUri()).body(result);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Car> update(@PathVariable("id") Integer id, @RequestBody Car car) {
        service.update(id, car);
        Car result = service.read(car.getId());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
