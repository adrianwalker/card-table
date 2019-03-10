package org.adrianwalker.cardtable.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static java.util.stream.Collectors.toList;
import org.adrianwalker.cardtable.dao.DataAccess;
import org.adrianwalker.cardtable.dto.CardTable;
import org.adrianwalker.cardtable.dto.CardTableCard;
import org.adrianwalker.cardtable.message.Deck;
import org.adrianwalker.cardtable.message.Move;
import org.adrianwalker.cardtable.message.Remove;
import org.adrianwalker.cardtable.message.Shuffle;
import org.adrianwalker.cardtable.message.Cards;
import org.adrianwalker.cardtable.message.Turn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Service {

  private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);

  private final DataAccess dataAccess;

  public Service(final DataAccess dataAccess) {

    LOGGER.debug("dataAccess = {}", dataAccess);

    this.dataAccess = dataAccess;
  }

  public CardTable createCardTable() throws Exception {

    LOGGER.debug("");

    return dataAccess.readCardTable(
      dataAccess.createCardTable());
  }

  public List<CardTableCard> createDeck(final Deck deck, final UUID playerId) throws Exception {

    LOGGER.debug("deck = {}, playerId = {}", deck, playerId);

    return getCardTableCards(
      dataAccess.createCardTableCards(
        deck.getCardTableId(), deck.getPackId(),
        deck.getxPosition(), deck.getyPosition()), playerId);
  }

  public List<CardTableCard> getCardTableCards(
    final Move move, final UUID playerId) throws Exception {

    LOGGER.debug("move = {}, playerId = {}", move, playerId);

    return getCardTableCards(move.getCardIds(), playerId);
  }

  public List<CardTableCard> getCardTableCards(
    final Cards cards, final UUID playerId) throws Exception {

    LOGGER.debug("cards = {}, playerId = {}", cards, playerId);

    return getCardTableCards(cards.getCardTableId(), playerId);
  }

  public List<CardTableCard> shuffleCards(
    final Shuffle shuffle, final UUID playerId) throws Exception {

    LOGGER.debug("shuffle = {}, playerId = {}", shuffle, playerId);

    List<UUID> cardIds = shuffle.getCardIds();
    dataAccess.updateCardTableCardsFaceDown(cardIds, true);

    Collections.shuffle(cardIds);

    dataAccess.updateCardTableCardsPosition(
      cardIds,
      shuffle.getxPosition(), shuffle.getyPosition(), shuffle.getzPosition());

    return getCardTableCards(shuffle.getCardIds(), playerId);
  }

  public List<CardTableCard> turnCards(final Turn turn, final UUID playerId) throws Exception {

    LOGGER.debug("turn = {}, playerId = {}", turn, playerId);

    dataAccess.updateCardTableCardsFaceDown(turn.getCardIds(), turn.getFaceDown());

    return getCardTableCards(turn.getCardIds(), playerId);
  }

  public void moveCards(final Move move, final UUID playerId) throws Exception {

    LOGGER.debug("move = {}, playerId = {}", move, playerId);

    dataAccess.updateCardTableCardsPlayer(move.getCardIds(), playerId);

    dataAccess.updateCardTableCardsPosition(
      move.getCardIds(),
      move.getxPosition(), move.getyPosition(), move.getzPosition());
  }

  public void removeCards(final Remove remove) throws Exception {

    LOGGER.debug("remove = {}", remove);

    dataAccess.deleteCardTableCards(remove.getCardIds());
  }

  private List<CardTableCard> getCardTableCards(
    final UUID cardTableId,
    final List<UUID> cardTableCardIds,
    final UUID playerId) throws Exception {

    LOGGER.debug("cardTableId={}, cardTableCardIds = {}, playerId = {}", cardTableId, cardTableCardIds, playerId);

    List<CardTableCard> cardTableCards;

    if (null != cardTableId) {
      cardTableCards = dataAccess.readCardTableCards(cardTableId);
    } else if (null != cardTableCardIds) {
      cardTableCards = dataAccess.readCardTableCards(cardTableCardIds);
    } else {
      throw new IllegalArgumentException("cardTableId and cardTableCardIds are null");
    }

    return filterFaceDownValues(filterPlayerCards(cardTableCards, playerId));
  }

  private List<CardTableCard> getCardTableCards(
    final UUID cardTableId,
    final UUID playerId) throws Exception {

    return getCardTableCards(cardTableId, null, playerId);
  }

  private List<CardTableCard> getCardTableCards(
    final List<UUID> cardTableCardIds,
    final UUID playerId) throws Exception {

    return getCardTableCards(null, cardTableCardIds, playerId);
  }

  private static List<CardTableCard> filterPlayerCards(
    final List<CardTableCard> cardTableCards, final UUID playerId) {

    LOGGER.debug("cardTableCards = {}, playerId = {}", cardTableCards, playerId);

    return cardTableCards.stream()
      .filter(cardTableCard -> null == cardTableCard.getPlayerId()
      || cardTableCard.getPlayerId().equals(playerId)).collect(toList());
  }

  private static List<CardTableCard> filterFaceDownValues(
    final List<CardTableCard> cardTableCards) {

    LOGGER.debug("cardTableCards = {}", cardTableCards);

    cardTableCards.stream()
      .filter(cardTableCard -> cardTableCard.getFaceDown())
      .forEach(cardTableCard -> {
        cardTableCard.setValue(null).setFrontImage(null);
      });

    return cardTableCards;
  }
}
