package dev.kameshs.rosa;

import java.util.ArrayList;
import java.util.List;

import dev.kameshs.rosa.data.Fruit;
import dev.kameshs.rosa.data.FruitRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * FruitController
 */
@RestController
@RequestMapping("/api")
public class FruitController {
    
    @Autowired
    FruitRepository fruitRepo;

    @GetMapping("/all")
    public @ResponseBody List<Fruit> all(){
        List<Fruit> fruits = new ArrayList<>();
        fruitRepo.findAll().forEach(fruits::add);
        return fruits;
    }

    @PostMapping("/add")
    public @ResponseBody Fruit add(@RequestBody Fruit fruit) {
        Fruit f = fruitRepo.save(fruit);
        return f;
    }

}