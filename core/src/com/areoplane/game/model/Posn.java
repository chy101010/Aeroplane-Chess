package com.areoplane.game.model;

import java.util.Objects;

public class Posn {
  private final int x;
  private final int y;

  public Posn(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Posn)) {
      return false;
    }
    Posn temp = (Posn) obj;
    return this.x == temp.getX() && this.y == temp.getY();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
  }
}
