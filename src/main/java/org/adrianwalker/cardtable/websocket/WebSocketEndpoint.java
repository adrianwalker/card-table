package org.adrianwalker.cardtable.websocket;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.adrianwalker.cardtable.dto.CardTableCard;
import org.adrianwalker.cardtable.message.Deck;
import org.adrianwalker.cardtable.message.Drag;
import org.adrianwalker.cardtable.message.Hide;
import org.adrianwalker.cardtable.message.Remove;
import org.adrianwalker.cardtable.message.Message;
import static org.adrianwalker.cardtable.message.Message.Modifier.PRIVATE;
import static org.adrianwalker.cardtable.message.Message.Modifier.PUBLIC;
import org.adrianwalker.cardtable.message.Move;
import org.adrianwalker.cardtable.message.Shuffle;
import org.adrianwalker.cardtable.message.Table;
import org.adrianwalker.cardtable.message.Cards;
import static org.adrianwalker.cardtable.message.Message.Type.CARDS;
import static org.adrianwalker.cardtable.message.Message.Type.DECK;
import static org.adrianwalker.cardtable.message.Message.Type.DRAG;
import static org.adrianwalker.cardtable.message.Message.Type.ERROR;
import static org.adrianwalker.cardtable.message.Message.Type.HIDE;
import static org.adrianwalker.cardtable.message.Message.Type.REMOVE;
import static org.adrianwalker.cardtable.message.Message.Type.SHUFFLE;
import static org.adrianwalker.cardtable.message.Message.Type.TABLE;
import static org.adrianwalker.cardtable.message.Message.Type.TURN;
import org.adrianwalker.cardtable.message.Turn;
import org.adrianwalker.cardtable.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint(value = "/websocket", configurator = WebSocketEndpointConfigurator.class)
public final class WebSocketEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEndpoint.class);
  private static final UUID SYSTEM_SENDER = UUID.fromString("99999999-9999-9999-9999-999999999999");

  private final Service service;

  public WebSocketEndpoint(final Service service) {

    LOGGER.debug("service = {}", service);

    this.service = service;
  }

  @OnOpen
  public void onOpen(final Session session) {

    LOGGER.debug("session = {}", session);
  }

  @OnClose
  public void onClose(final Session session) {

    LOGGER.debug("session = {}", session);
  }

  @OnMessage
  public String onMessage(final Session session, final String requestJson) throws Exception {

    LOGGER.debug("requestJson = {}", requestJson);
    Message message = Message.parse(requestJson);

    if (null == message) {
      return new Message(SYSTEM_SENDER, ERROR, PUBLIC, "invalid message format").toJson();
    }

    String responseJson = null;
    String broadcastJson = null;

    switch (message.getType()) {

      case TABLE:
        responseJson = onTable(message);
        break;

      case CARDS:
        responseJson = onCards(message);
        break;

      case DECK:
        responseJson = onDeck(message);
        broadcastJson = responseJson;
        break;

      case SHUFFLE:
        responseJson = onShuffle(message);

        if (message.getModifier() == PUBLIC) {
          broadcastJson = responseJson;
        }

        break;

      case TURN:
        responseJson = onTurn(message);

        if (message.getModifier() == PUBLIC) {
          broadcastJson = responseJson;
        }

        break;

      case REMOVE:
        onRemove(message);
        responseJson = requestJson;
        broadcastJson = responseJson;
        break;

      case MOVE:
        broadcastJson = onMove(message);
        break;

      case DRAG:

        broadcastJson = onDrag(message);
        break;

      default:
        responseJson = onError();
        break;
    }

    if (null != broadcastJson) {
      LOGGER.debug("broadcastJson = {}", broadcastJson);
      broadcast(session, broadcastJson);
    }

    LOGGER.debug("responseJson = {}", responseJson);

    return responseJson;
  }

  private static String onError() throws Exception {

    return new Message(SYSTEM_SENDER, ERROR, PUBLIC, "unknown message type").toJson();
  }

  private String onDrag(final Message message) throws Exception {

    Drag drag = message.getDataAs(Drag.class);

    Message.Type type;
    Object data;

    if (message.getModifier() == PRIVATE) {
      type = HIDE;
      data = new Hide().setCardIds(drag.getCardIds());
    } else {
      type = DRAG;
      data = drag;
    }

    return new Message(
      message.getSender(),
      type,
      PUBLIC,
      data).toJson();
  }

  private String onMove(final Message message) throws Exception {

    Move move = message.getDataAs(Move.class);

    if (message.getModifier() == PRIVATE) {

      service.moveCards(move, message.getSender());

      return new Message(
        message.getSender(),
        REMOVE,
        PUBLIC,
        new Remove().setCardIds(move.getCardIds())).toJson();

    } else if (message.getModifier() == PUBLIC) {

      service.moveCards(move, null);

      List<CardTableCard> cards = service.getCardTableCards(move, message.getSender());

      return new Message(
        message.getSender(),
        CARDS,
        PUBLIC,
        cards).toJson();

    } else {

      return null;
    }
  }

  private void onRemove(final Message message) throws Exception {

    service.removeCards(message.getDataAs(Remove.class));
  }

  private String onTurn(final Message message) throws Exception {

    return new Message(
      message.getSender(),
      TURN,
      PUBLIC,
      service.turnCards(message.getDataAs(Turn.class), message.getSender())).toJson();
  }

  private String onShuffle(final Message message) throws Exception {

    return new Message(
      message.getSender(),
      SHUFFLE,
      PUBLIC,
      service.shuffleCards(message.getDataAs(Shuffle.class), message.getSender())).toJson();
  }

  private String onDeck(final Message message) throws Exception {

    return new Message(
      message.getSender(),
      DECK,
      PUBLIC,
      service.createDeck(message.getDataAs(Deck.class), message.getSender())).toJson();
  }

  private String onCards(final Message message) throws Exception {

    return new Message(
      message.getSender(),
      CARDS,
      PUBLIC,
      service.getCardTableCards(message.getDataAs(Cards.class), message.getSender())).toJson();
  }

  private String onTable(final Message message) throws Exception {

    return new Message(
      message.getSender(),
      TABLE,
      PUBLIC,
      service.getCardTable(message.getDataAs(Table.class))).toJson();
  }

  @OnError
  public void onError(final Throwable t) {

    LOGGER.error("WebSocket error", t);
  }

  private static void broadcast(final Session session, final String json) {

    LOGGER.debug("session = {}, json = {}", session, json);

    session.getOpenSessions()
      .stream()
      .filter(openSession -> !openSession.equals(session))
      .forEach(peerSession -> {
        try {
          peerSession.getBasicRemote().sendText(json);
        } catch (final IOException ioe) {
          LOGGER.error("Error broadcasting to peer session", ioe);
        }
      });
  }
}
