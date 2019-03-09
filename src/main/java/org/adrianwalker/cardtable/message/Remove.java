package org.adrianwalker.cardtable.message;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public final class Remove implements Serializable {

  private List<UUID> cardIds;

  public List<UUID> getCardIds() {
    return cardIds;
  }

  public Remove setCardIds(final List<UUID> cardIds) {
    this.cardIds = cardIds;
    return this;
  }

  @Override
  public String toString() {
    return "cardIds=" + cardIds;
  }
}
