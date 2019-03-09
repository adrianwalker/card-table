package org.adrianwalker.cardtable.servlet;

import java.io.IOException;
import static java.lang.String.format;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.adrianwalker.cardtable.dao.DataAccess;
import org.adrianwalker.cardtable.dao.DataSourceFactory;
import org.adrianwalker.cardtable.dto.CardTable;
import org.adrianwalker.cardtable.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/cardtable.html")
public final class CardTableServlet extends HttpServlet {

  private static final Logger LOGGER = LoggerFactory.getLogger(CardTableServlet.class);
  private static final String DATASOURCE_NAME = "java:comp/env/jdbc/cardtable";
  private static final String CARD_TABLE_ID = "cardTableId";
  private static final String PLAYER_ID = "playerId";
  private static final String REDIRECT_URL = "%s?cardTableId=%s";
  private static final String CARD_TABLE_JSP = "WEB-INF/cardtable.jsp";

  private Service service;

  @Override
  public void init() throws ServletException {

    service = new Service(
      new DataAccess(
        DataSourceFactory.getDataSource(DATASOURCE_NAME)));
  }

  @Override
  protected void doGet(
    final HttpServletRequest request,
    final HttpServletResponse response)
    throws ServletException, IOException {

    String cardTableId = request.getParameter(CARD_TABLE_ID);
    LOGGER.debug("cardTableId = {}", cardTableId);

    if (null == cardTableId) {
      CardTable cardTable = createCardTable();
      cardTableId = cardTable.getId().toString();
      String url = format(REDIRECT_URL, request.getRequestURL().toString(), cardTableId);

      LOGGER.debug("redirecting to '{}'", url);
      response.sendRedirect(url);

    } else {

      request.setAttribute(CARD_TABLE_ID, cardTableId);
      request.setAttribute(PLAYER_ID, UUID.randomUUID().toString());

      LOGGER.debug("including jsp '{}'", CARD_TABLE_JSP);
      RequestDispatcher updateDispatcher = request.getRequestDispatcher(CARD_TABLE_JSP);
      updateDispatcher.include(request, response);
    }
  }

  private CardTable createCardTable() throws ServletException {

    CardTable cardTable;

    try {

      cardTable = service.createCardTable();

    } catch (final Exception e) {

      LOGGER.error("Error creating new card table", e);
      throw new ServletException(e);
    }

    return cardTable;
  }
}
