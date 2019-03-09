package org.adrianwalker.cardtable.message;

import java.util.UUID;

public final class Cards {

  private UUID cardTableId;

  public Cards() {
  }

  public UUID getCardTableId() {
    return cardTableId;
  }

  public Cards setCardTableId(final UUID cardTableId) {
    this.cardTableId = cardTableId;
    return this;
  }

  @Override
  public String toString() {
    return "cardTableId=" + cardTableId;
  }
}
