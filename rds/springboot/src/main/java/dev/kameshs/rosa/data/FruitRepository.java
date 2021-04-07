package dev.kameshs.rosa.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * FruitRepository
 */
public interface FruitRepository extends CrudRepository<Fruit, Long> {

    List<Fruit> findByName(String name);
    List<Fruit> findBySeason(String season);

    Fruit findById(long id);
    
}