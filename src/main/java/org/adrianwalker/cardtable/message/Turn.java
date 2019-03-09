package org.adrianwalker.cardtable.message;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public final class Turn implements Serializable {

  private List<UUID> cardIds;
  private Boolean faceDown;

  public List<UUID> getCardIds() {
    return cardIds;
  }

  public Turn setCardIds(final List<UUID> cardIds) {
    this.cardIds = cardIds;
    return this;
  }

  public Boolean getFaceDown() {
    return faceDown;
  }

  public Turn setFaceDown(final Boolean faceDown) {
    this.faceDown = faceDown;
    return this;
  }

  @Override
  public String toString() {
    return "cardIds=" + cardIds
      + ", faceDown=" + faceDown;
  }
}
