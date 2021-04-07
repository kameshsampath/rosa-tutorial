package dev.kameshs.rosa;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;

@Entity
public class Fruit extends PanacheEntity {

  public String name;
  public String season;

  public Fruit() {
  }

  @Override
  public String toString() {
    return "Fruit{" +
      "name='" + name + '\'' +
      ", season='" + season + '\'' +
      '}';
  }
}
