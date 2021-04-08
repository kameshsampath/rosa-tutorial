package dev.kameshs.rosa;

import java.util.Objects;

public class Fruit {

  public String name;
  public String season;

  public Fruit() {
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Fruit)) {
      return false;
    }

    Fruit other = (Fruit) obj;

    return Objects.equals(other.name, this.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
  }

  @Override
  public String toString() {
    return "Fruit{" +
      "name='" + name + '\'' +
      ", season='" + season + '\'' +
      '}';
  }
}
