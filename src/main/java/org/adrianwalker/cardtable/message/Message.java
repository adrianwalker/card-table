package org.adrianwalker.cardtable.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Message implements Serializable {

  public enum Modifier {
    PUBLIC, PRIVATE;
  }

  public enum Type {
    CARDS, DECK, DRAG, HIDE, MOVE, REMOVE, SHUFFLE, TABLE, TURN,
    ERROR;
  }

  private static final String SENDER_GROUP = "sender";
  private static final String TYPE_GROUP = "type";
  private static final String MODIFIER_GROUP = "modifier";
  private static final String DATA_GROUP = "data";
  private static final Pattern MESSAGE_PATTERN = Pattern.compile(
    "^\\{"
    + "\"sender\":\"(?<sender>.*?)\","
    + "\"type\":\"(?<type>.*?)\","
    + "\"modifier\":\"(?<modifier>.*?)\","
    + "\"data\":(?<data>.*)"
    + "\\}$");
  private static final ObjectMapper MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

  private UUID sender;
  private Type type;
  private Modifier modifier;
  private Object data;

  public Message(final UUID sender, final Type type, final Modifier modifier, final Object data) {

    this.sender = sender;
    this.type = type;
    this.modifier = modifier;
    this.data = data;
  }

  public String toJson() throws Exception {

    return MAPPER.writeValueAsString(this);
  }

  public static Message parse(final String json) {

    Matcher matcher = MESSAGE_PATTERN.matcher(json);
    if (!matcher.matches()) {
      return null;
    }

    return new Message(
      UUID.fromString(matcher.group(SENDER_GROUP)),
      Type.valueOf(matcher.group(TYPE_GROUP).toUpperCase()),
      Modifier.valueOf(matcher.group(MODIFIER_GROUP).toUpperCase()),
      matcher.group(DATA_GROUP));
  }

  public UUID getSender() {
    return sender;
  }

  public Message setSender(final UUID sender) {
    this.sender = sender;
    return this;
  }

  public Type getType() {
    return type;
  }

  public Message setType(final Type type) {
    this.type = type;
    return this;
  }

  public Modifier getModifier() {
    return modifier;
  }

  public Message setModifier(final Modifier modifer) {
    this.modifier = modifer;
    return this;
  }

  public Object getData() {
    return data;
  }

  public <T> T getDataAs(final Class<T> clazz) throws Exception {

    return MAPPER.readValue((String) data, clazz);
  }

  public Message setData(final Object data) {
    this.data = data;
    return this;
  }
}
