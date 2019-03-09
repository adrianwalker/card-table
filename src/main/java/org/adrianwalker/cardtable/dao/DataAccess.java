package org.adrianwalker.cardtable.dao;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.adrianwalker.cardtable.dto.CardTable;
import org.adrianwalker.cardtable.dto.CardTableCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DataAccess {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataAccess.class);

  private final DataSource dataSource;

  private static final String INSERT_CARD_TABLE = readFile("sql/insert-card-table.sql");
  private static final String SELECT_CARD_TABLE = readFile("sql/select-card-table.sql");
  private static final String INSERT_CARD_TABLE_CARDS = readFile("sql/insert-card-table-cards.sql");
  private static final String SELECT_CARD_TABLE_CARDS_BY_CARD_TABLE_ID = readFile("sql/select-card-table-cards-by-card-table-id.sql");
  private static final String SELECT_CARD_TABLE_CARDS_BY_CARD_IDS = readFile("sql/select-card-table-cards-by-card-ids.sql");
  private static final String UPDATE_CARD_TABLE_CARD_POSITION = readFile("sql/update-card-table-card-position.sql");
  private static final String UPDATE_CARD_TABLE_CARD_FACE_DOWN = readFile("sql/update-card-table-card-face-down.sql");
  private static final String UPDATE_CARD_TABLE_CARD_PLAYER_ID = readFile("sql/update-card-table-card-player-id.sql");
  private static final String DELETE_CARD_TABLE_CARD = readFile("sql/delete-card-table-card.sql");

  public DataAccess(final DataSource dataSource) {

    this.dataSource = dataSource;
  }

  private static String readFile(final String fileName) {

    LOGGER.debug("fileName = {}", fileName);

    InputStream in = DataAccess.class.getClassLoader().getResourceAsStream(fileName);
    BufferedInputStream bis = new BufferedInputStream(in);

    byte[] bytes;
    try {

      bytes = bis.readAllBytes();

    } catch (final IOException e) {

      LOGGER.error("Error reading file", e);
      throw new RuntimeException(e);
    }

    return new String(bytes);
  }

  public UUID createCardTable() throws SQLException {

    UUID cardTableId;

    try ( Connection connection = getConnection();  PreparedStatement ps = prepareStatement(
      connection, INSERT_CARD_TABLE, "id")) {

      try {
        ps.executeUpdate();
      } catch (final SQLException sqle) {
        connection.rollback();
        throw sqle;
      }

      connection.commit();

      try ( ResultSet resultSet = ps.getGeneratedKeys()) {
        resultSet.next();
        cardTableId = (UUID) resultSet.getObject(1);
      }
    }

    return cardTableId;
  }

  public CardTable readCardTable(final UUID cardTableId) throws SQLException {

    LOGGER.debug("cardTableId = {}", cardTableId);

    CardTable cardTable = null;

    try ( Connection connection = getConnection();  PreparedStatement ps = prepareStatement(
      connection, SELECT_CARD_TABLE)) {

      ps.setObject(1, cardTableId);

      try ( ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          cardTable = new CardTable()
            .setId((UUID) resultSet.getObject(1))
            .setCreated(resultSet.getDate(2));
        }
      }
    }

    return cardTable;
  }

  public List<UUID> createCardTableCards(
    final UUID cardTableId, final long packId,
    final int xPosition, final int yPosition) throws SQLException {

    LOGGER.debug(
      "cardTableId = {}, xPosition = {}, yPosition = {}, packId = {}",
      cardTableId, xPosition, yPosition, packId);

    List<UUID> cardTableCardIds = new ArrayList<>();

    try ( Connection connection = getConnection();  PreparedStatement ps = prepareStatement(
      connection, INSERT_CARD_TABLE_CARDS, "id")) {

      ps.setObject(1, cardTableId);
      ps.setInt(2, xPosition);
      ps.setInt(3, yPosition);
      ps.setLong(4, packId);

      try {
        ps.executeUpdate();
      } catch (final SQLException sqle) {
        connection.rollback();
        throw sqle;
      }

      connection.commit();

      try ( ResultSet resultSet = ps.getGeneratedKeys()) {
        while (resultSet.next()) {
          cardTableCardIds.add((UUID) resultSet.getObject(1));
        }
      }
    }

    return cardTableCardIds;
  }

  public List<CardTableCard> readCardTableCards(
    final UUID cardTableId) throws SQLException {

    LOGGER.debug("cardTableId = {}", cardTableId);

    List<CardTableCard> cardTableCards = new ArrayList<>();

    try ( Connection connection = getConnection();  PreparedStatement ps = prepareStatement(
      connection, SELECT_CARD_TABLE_CARDS_BY_CARD_TABLE_ID)) {

      ps.setObject(1, cardTableId);

      try ( ResultSet resultSet = ps.executeQuery()) {
        while (resultSet.next()) {
          cardTableCards.add(new CardTableCard()
            .setId((UUID) resultSet.getObject(1))
            .setValue(resultSet.getString(2))
            .setFrontImage(resultSet.getString(3))
            .setBackImage(resultSet.getString(4))
            .setXPosition(resultSet.getInt(5))
            .setYPosition(resultSet.getInt(6))
            .setZPosition(resultSet.getInt(7))
            .setFaceDown(resultSet.getBoolean(8))
            .setPlayerId((UUID) resultSet.getObject(9)));
        }
      }
    }

    return cardTableCards;
  }

  public List<CardTableCard> readCardTableCards(
    final List<UUID> cardTableCardIds) throws SQLException {

    LOGGER.debug("cardTableIds = {}", cardTableCardIds);

    List<CardTableCard> cardTableCards = new ArrayList<>();

    try ( Connection connection = getConnection();  PreparedStatement ps = prepareStatement(
      connection, SELECT_CARD_TABLE_CARDS_BY_CARD_IDS)) {

      ps.setArray(1, connection.createArrayOf(
        "uuid", cardTableCardIds.toArray()));

      try ( ResultSet resultSet = ps.executeQuery()) {
        while (resultSet.next()) {
          cardTableCards.add(new CardTableCard()
            .setId((UUID) resultSet.getObject(1))
            .setValue(resultSet.getString(2))
            .setFrontImage(resultSet.getString(3))
            .setBackImage(resultSet.getString(4))
            .setXPosition(resultSet.getInt(5))
            .setYPosition(resultSet.getInt(6))
            .setZPosition(resultSet.getInt(7))
            .setFaceDown(resultSet.getBoolean(8))
            .setPlayerId((UUID) resultSet.getObject(9)));
        }
      }
    }

    return cardTableCards;
  }

  public void updateCardTableCardsPosition(
    final List<UUID> cardTableCardIds,
    final int xPosition, final int yPosition, final int zPosition) throws SQLException {

    LOGGER.debug("cardTableIds = {}, "
      + "xPosition = {}, yPosition = {}, zPosition = {}",
      cardTableCardIds, xPosition, yPosition, zPosition);

    try ( Connection connection = getConnection();  PreparedStatement ps = prepareStatement(
      connection, UPDATE_CARD_TABLE_CARD_POSITION)) {

      for (int index = 0; index < cardTableCardIds.size(); index++) {
        ps.setInt(1, xPosition);
        ps.setInt(2, yPosition);
        ps.setInt(3, zPosition + index);
        ps.setObject(4, cardTableCardIds.get(index));

        try {
          ps.addBatch();
        } catch (final SQLException sqle) {
          connection.rollback();
          throw sqle;
        }
      }

      ps.executeBatch();
      connection.commit();
    }
  }

  public void updateCardTableCardsFaceDown(
    final List<UUID> cardTableCardIds, final boolean faceDown)
    throws SQLException {

    LOGGER.debug("cardTableIds = {}, faceDown = {}",
      cardTableCardIds, faceDown);

    try ( Connection connection = getConnection();  PreparedStatement ps = prepareStatement(
      connection, UPDATE_CARD_TABLE_CARD_FACE_DOWN)) {

      for (UUID cardTableCardId : cardTableCardIds) {
        ps.setBoolean(1, faceDown);
        ps.setObject(2, cardTableCardId);

        try {
          ps.addBatch();
        } catch (final SQLException sqle) {
          connection.rollback();
          throw sqle;
        }
      }

      ps.executeBatch();
      connection.commit();
    }
  }

  public void updateCardTableCardsPlayer(
    final List<UUID> cardTableCardIds, final UUID playerId)
    throws SQLException {

    LOGGER.debug("cardTableIds = {}, playerId = {}",
      cardTableCardIds, playerId);

    try ( Connection connection = getConnection();  PreparedStatement ps = prepareStatement(
      connection, UPDATE_CARD_TABLE_CARD_PLAYER_ID)) {

      for (UUID cardTableCardId : cardTableCardIds) {
        ps.setObject(1, playerId);
        ps.setObject(2, cardTableCardId);

        try {
          ps.addBatch();
        } catch (final SQLException sqle) {
          connection.rollback();
          throw sqle;
        }
      }

      ps.executeBatch();
      connection.commit();
    }
  }

  public void deleteCardTableCards(
    final List<UUID> cardTableCardIds) throws SQLException {

    LOGGER.debug("cardTableIds = {}", cardTableCardIds);

    try ( Connection connection = getConnection();  PreparedStatement ps = prepareStatement(
      connection, DELETE_CARD_TABLE_CARD)) {

      for (UUID cardTableCardId : cardTableCardIds) {

        ps.setObject(1, cardTableCardId);
        try {
          ps.addBatch();
        } catch (final SQLException sqle) {
          connection.rollback();
          throw sqle;

        }
      }

      ps.executeBatch();
      connection.commit();
    }
  }

  private Connection getConnection() throws SQLException {

    Connection connection = dataSource.getConnection();
    connection.setAutoCommit(false);

    return connection;
  }

  private PreparedStatement prepareStatement(
    final Connection connection, final String sql,
    final String... generatedKeys) throws SQLException {

    LOGGER.debug("sql = {}", sql);

    return connection.prepareStatement(sql, generatedKeys);
  }
}
