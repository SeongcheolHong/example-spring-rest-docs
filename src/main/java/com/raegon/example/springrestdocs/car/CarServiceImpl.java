package com.raegon.example.springrestdocs.car;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class CarServiceImpl implements CarService {

    private HashMap<Integer, Car> map = new HashMap<>();

    @Override
    public void create(Car car) {
        log.info("create {}", car);
        map.put(car.getId(), car);
    }

    @Override
    public Car read(Integer id) {
        log.info("read {}", id);
        return map.get(id);
    }

    @Override
    public void update(Integer id, Car car) {
        delete(id);
        create(car);
    }

    @Override
    public void delete(Integer id) {
        log.info("delete {}", id);
        map.remove(id);
    }


}
