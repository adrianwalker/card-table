package org.adrianwalker.cardtable.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public final class CardTableCard implements Serializable {

  private UUID id;
  private String value;
  private String frontImage;
  private String backImage;
  private int xPosition;
  private int yPosition;
  private int zPosition;
  private boolean faceDown;
  private UUID playerId;

  public CardTableCard() {
  }

  public UUID getId() {
    return id;
  }

  public CardTableCard setId(final UUID id) {
    this.id = id;
    return this;
  }

  public String getValue() {
    return value;
  }

  public CardTableCard setValue(final String value) {
    this.value = value;
    return this;
  }

  public String getFrontImage() {
    return frontImage;
  }

  public CardTableCard setFrontImage(final String frontImage) {
    this.frontImage = frontImage;
    return this;
  }

  public String getBackImage() {
    return backImage;
  }

  public CardTableCard setBackImage(final String backImage) {
    this.backImage = backImage;
    return this;
  }

  public int getXPosition() {
    return xPosition;
  }

  public CardTableCard setXPosition(final int xPosition) {
    this.xPosition = xPosition;
    return this;
  }

  public int getYPosition() {
    return yPosition;
  }

  public CardTableCard setYPosition(final int yPosition) {
    this.yPosition = yPosition;
    return this;
  }

  public int getZPosition() {
    return zPosition;
  }

  public CardTableCard setZPosition(final int zPosition) {
    this.zPosition = zPosition;
    return this;
  }

  public boolean getFaceDown() {
    return faceDown;
  }

  public CardTableCard setFaceDown(final boolean faceDown) {
    this.faceDown = faceDown;
    return this;
  }

  public UUID getPlayerId() {
    return playerId;
  }

  public CardTableCard setPlayerId(final UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.id);
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

    return Objects.equals(this.id, ((CardTableCard) obj).id);
  }

  @Override
  public String toString() {
    return "CardTableCard{" + "id=" + id + ", value=" + value + ", frontImage=" + frontImage + ", backImage=" + backImage + ", xPosition=" + xPosition + ", yPosition=" + yPosition + ", zPosition=" + zPosition + ", faceDown=" + faceDown + ", playerId=" + playerId + '}';
  }
}
