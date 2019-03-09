package org.adrianwalker.cardtable.message;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public final class Drag implements Serializable {

  private List<UUID> cardIds;
  private Integer xPosition;
  private Integer yPosition;

  public List<UUID> getCardIds() {
    return cardIds;
  }

  public Drag setCardIds(final List<UUID> cardIds) {
    this.cardIds = cardIds;
    return this;
  }

  public Integer getxPosition() {
    return xPosition;
  }

  public Drag setxPosition(final Integer xPosition) {
    this.xPosition = xPosition;
    return this;
  }

  public Integer getyPosition() {
    return yPosition;
  }

  public Drag setyPosition(final Integer yPosition) {
    this.yPosition = yPosition;
    return this;
  }

  @Override
  public String toString() {
    return "cardIds=" + cardIds
      + ", xPosition=" + xPosition
      + ", yPosition=" + yPosition;
  }
}
