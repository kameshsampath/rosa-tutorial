package dev.kameshs.rosa;

import io.quarkus.panache.common.Sort;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FruitSyncService{

  public List<Fruit> findAll() throws Exception {
    return Fruit.listAll(Sort.by("season"));
  }

  public void add(Fruit fruit) throws Exception {
    fruit.persist();
  }

  public Fruit get(String name) throws Exception {
    return Fruit.find("name",name).firstResult();
  }

  public Fruit getBySeason(String season) throws Exception {
    return Fruit.find("season",season).firstResult();
  }

  public void delete(String name) throws Exception {
     Fruit.delete("name",name);
  }
}
