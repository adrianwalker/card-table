package org.adrianwalker.cardtable.dto;

import java.io.Serializable;
import java.util.Objects;

public final class Pack implements Serializable {

  private long id;
  private String name;
  private String backImage;

  public Pack() {
  }

  public long getId() {
    return id;
  }

  public Pack setId(final long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Pack setName(final String name) {
    this.name = name;
    return this;
  }

  public String getBackImage() {
    return backImage;
  }

  public Pack setBackImage(final String backImage) {
    this.backImage = backImage;
    return this;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 29 * hash + Objects.hashCode(this.id);
    return hash;
  }

  @Override
  public boolean equals(final Object obj) {

    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    return Objects.equals(this.id, ((Pack) obj).id);
  }
}
