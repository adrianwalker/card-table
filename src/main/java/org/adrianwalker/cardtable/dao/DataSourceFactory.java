package org.adrianwalker.cardtable.dao;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DataSourceFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceFactory.class);

  private DataSourceFactory() {
  }

  public static DataSource getDataSource(final String name) {

    LOGGER.debug("name = {}", name);

    try {

      InitialContext ctx = new InitialContext();
      return (DataSource) ctx.lookup(name);

    } catch (final NamingException ne) {

      LOGGER.error(ne.getMessage(), ne);
      throw new RuntimeException(ne);
    }
  }
}
