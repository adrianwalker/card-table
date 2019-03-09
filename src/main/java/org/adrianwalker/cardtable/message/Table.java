package org.adrianwalker.cardtable.message;

import java.io.Serializable;
import java.util.UUID;

public final class Table implements Serializable {

  private UUID cardTableId;

  public Table() {
  }

  public UUID getCardTableId() {
    return cardTableId;
  }

  public Table setCardTableId(final UUID cardTableId) {
    this.cardTableId = cardTableId;
    return this;
  }

  @Override
  public String toString() {
    return "cardTableId=" + cardTableId;
  }
}
