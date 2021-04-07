package dev.kameshs.rosa.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Fruit
 */
@Entity
public class Fruit {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  public String name;
  public String season;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSeason() {
    return season;
  }

  public void setSeason(String season) {
    this.season = season;
  }

  public String toString() {
    return String.format("Fruit[id=%d, name='%s', season='%s']", id, name, season);
  }

}