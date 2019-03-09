package org.adrianwalker.cardtable.message;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public final class Shuffle implements Serializable {

  private List<UUID> cardIds;
  private Integer xPosition;
  private Integer yPosition;
  private Integer zPosition;

  public List<UUID> getCardIds() {
    return cardIds;
  }

  public Shuffle setCardIds(final List<UUID> cardIds) {
    this.cardIds = cardIds;
    return this;
  }

  public Integer getxPosition() {
    return xPosition;
  }

  public Shuffle setxPosition(final Integer xPosition) {
    this.xPosition = xPosition;
    return this;
  }

  public Integer getyPosition() {
    return yPosition;
  }

  public Shuffle setyPosition(final Integer yPosition) {
    this.yPosition = yPosition;
    return this;
  }

  public Integer getzPosition() {
    return zPosition;
  }

  public Shuffle setzPosition(final Integer zPosition) {
    this.zPosition = zPosition;
    return this;
  }

  @Override
  public String toString() {
    return "cardIds=" + cardIds
      + ", xPosition=" + xPosition
      + ", yPosition=" + yPosition
      + ", zPosition=" + zPosition;
  }
}
