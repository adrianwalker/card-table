package org.adrianwalker.cardtable.message;

import java.io.Serializable;
import java.util.UUID;

public final class Deck implements Serializable {

  private UUID cardTableId;
  private Long packId;
  private Integer xPosition;
  private Integer yPosition;

  public UUID getCardTableId() {
    return cardTableId;
  }

  public Deck setCardTableId(final UUID cardTableId) {
    this.cardTableId = cardTableId;
    return this;
  }

  public Long getPackId() {
    return packId;
  }

  public Deck setPackId(final Long packId) {
    this.packId = packId;
    return this;
  }

  public Integer getxPosition() {
    return xPosition;
  }

  public Deck setxPosition(final Integer xPosition) {
    this.xPosition = xPosition;
    return this;
  }

  public Integer getyPosition() {
    return yPosition;
  }

  public Deck setyPosition(final Integer yPosition) {
    this.yPosition = yPosition;
    return this;
  }

  @Override
  public String toString() {
    return "cardTableId=" + cardTableId
      + ", packId=" + packId
      + ", xPosition=" + xPosition
      + ", yPosition=" + yPosition;
  }
}
