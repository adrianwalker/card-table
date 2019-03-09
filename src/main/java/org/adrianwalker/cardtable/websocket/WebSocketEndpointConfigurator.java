package org.adrianwalker.cardtable.websocket;

import javax.websocket.server.ServerEndpointConfig;
import org.adrianwalker.cardtable.dao.DataAccess;
import org.adrianwalker.cardtable.dao.DataSourceFactory;
import org.adrianwalker.cardtable.service.Service;

public final class WebSocketEndpointConfigurator extends ServerEndpointConfig.Configurator {

  private static final String DATASOURCE_NAME = "java:comp/env/jdbc/cardtable";
  private static final WebSocketEndpoint END_POINT = new WebSocketEndpoint(
    new Service(
      new DataAccess(
        DataSourceFactory.getDataSource(DATASOURCE_NAME))));

  @Override
  public <T> T getEndpointInstance(final Class<T> endpointClass) throws InstantiationException {
    return (T) END_POINT;
  }
}
