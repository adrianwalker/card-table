package org.adrianwalker.cardtable.dto;

import java.io.Serializable;
import java.util.Objects;

public final class Card implements Serializable {

  private long id;
  private String value;
  private String frontImage;

  public Card() {
  }

  public long getId() {
    return id;
  }

  public Card setId(final long id) {
    this.id = id;
    return this;
  }

  public String getValue() {
    return value;
  }

  public Card setValue(final String value) {
    this.value = value;
    return this;
  }

  public String getFrontImage() {
    return frontImage;
  }

  public Card setFrontImage(final String frontImage) {
    this.frontImage = frontImage;
    return this;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 47 * hash + Objects.hashCode(this.id);
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

    return Objects.equals(this.id, ((Card) obj).id);
  }
}
