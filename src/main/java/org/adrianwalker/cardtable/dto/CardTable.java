package org.adrianwalker.cardtable.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public final class CardTable implements Serializable {

  private UUID id;
  private Date created;

  public CardTable() {
  }

  public UUID getId() {
    return id;
  }

  public CardTable setId(final UUID id) {
    this.id = id;
    return this;
  }

  public Date getCreated() {
    return created;
  }

  public CardTable setCreated(final Date created) {
    this.created = created;
    return this;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 17 * hash + Objects.hashCode(this.id);
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

    return Objects.equals(this.id, ((CardTable) obj).id);
  }

  @Override
  public String toString() {
    return "CardTable{" + "id=" + id + ", created=" + created + '}';
  }
}
