"use strict";

var CNAME = "PLAYER_ID";

function getUrl() {

  return "ws://" + window.location.host + "/cardtable/websocket";
}

function getCardTableId() {

  return cardTableId;
}

function getPlayerId() {

  var cookie = getCookie(CNAME);
  if (cookie) {
    playerId = cookie;
  } else {
    setCookie(CNAME, playerId, 9999);
  }

  return playerId;
}

window.addEventListener("load", function () {
  new CardTableApp(getPlayerId(), getCardTableId()).init(getUrl());
});

var Modifier = {
  PUBLIC: "PUBLIC",
  PRIVATE: "PRIVATE"
};

var Type = {
  CARDS: "CARDS",
  DECK: "DECK",
  DRAG: "DRAG",
  HIDE: "HIDE",
  MOVE: "MOVE",
  REMOVE: "REMOVE",
  SHUFFLE: "SHUFFLE",
  TURN: "TURN",
  ERROR: "ERROR"
};

var CssClass = {
  CLICKED: "clicked",
  SELECTED: "selected",
  DRAGGED: "dragged"
};

function CardTableApp(playerId, cardTableId) {

  this.playerId = playerId;
  this.cardTableId = cardTableId;
  this.zPositionMax = 0;

  this.webSocket = null;
  this.gameArea = null;
  this.pocketArea = null;

  this.init = function (url) {

    this.webSocket = new WebSocket(url);

    var self = this;
    this.webSocket.onmessage = function (event) {
      self.messageListener(event);
    };

    var self = this;
    this.webSocket.onopen = function () {
      self.gameArea = self.initGameArea();
      self.pocketArea = self.initPocketArea();
      self.sendCards();
    };
  };

  this.initGameArea = function () {

    var icons = document.getElementsByClassName("side-nav-icon");
    for (var i = 0; i < icons.length; i++) {

      icons[i].addEventListener("dragstart", function (event) {
        self.iconDragStartListener(event);
      });
    }

    var gameArea = document.getElementById("game-area");
    var self = this;

    gameArea.addEventListener("mousemove", function (event) {
      self.gameAreaMouseMoveListener(event);
    });
    gameArea.addEventListener("dragover", function (event) {
      self.gameAreaDragOverListener(event);
    });
    gameArea.addEventListener("drop", function (event) {
      self.gameAreaDropListener(event);
    });
    gameArea.addEventListener("mousedown", function (event) {
      self.gameAreaMouseDownListener(event);
    });
    gameArea.addEventListener("mouseup", function (event) {
      self.gameAreaMouseUpListener(event);
    });

    return gameArea;
  };

  this.initPocketArea = function () {

    return document.getElementById("pocket-area");
  };

  this.messageListener = function (event) {

    var message = JSON.parse(event.data);

    switch (message.type) {

      case Type.DRAG:

        var cards = [];
        for (var i = 0; i < message.data.cardIds.length; i++) {
          var card = document.getElementById(message.data.cardIds[i]);
          if (card) {
            cards.push(card);
          }
        }

        this.dragCards(cards, message.data.xPosition, message.data.yPosition);

        break;

      case Type.REMOVE:

        for (var i = 0; i < message.data.cardIds.length; i++) {
          var card = document.getElementById(message.data.cardIds[i]);
          if (card) {
            this.removeCard(card);
          }
        }

        break;

      case Type.HIDE:

        for (var i = 0; i < message.data.cardIds.length; i++) {
          var card = document.getElementById(message.data.cardIds[i]);
          if (card) {
            this.hideCard(card);
          }
        }

        break;

      case Type.TURN:

        for (var i = 0; i < message.data.length; i++) {
          var card = document.getElementById(message.data[i].id);
          if (card) {
            this.turnCard(card, message.data[i]);
          }
        }

        break;

      case Type.CARDS:
      case Type.DECK:
      case Type.SHUFFLE:

        for (var i = 0; i < message.data.length; i++) {

          var card = document.getElementById(message.data[i].id);
          if (card) {
            this.removeCard(card);
          }

          card = this.createCard(message.data[i]);
          var self = this;

          card.addEventListener("mousedown", function (event) {
            self.cardMouseDownListener(event);
          });
          card.addEventListener("mouseup", function (event) {
            self.cardMouseUpListener(event);
          });

          this.gameArea.appendChild(card);
        }

        break;

      case Type.ERROR:

        console.error("onMessage", event);

        break
    }
  };

  this.iconDragStartListener = function (event) {

    var packId = event.target.attributes["data-packId"].value;
    event.dataTransfer.setData("Text", packId);
  };

  this.gameAreaDragOverListener = function (event) {

    event.preventDefault();
    event.stopPropagation();
  };

  this.gameAreaDropListener = function (event) {

    var packId = event.dataTransfer.getData("Text");

    this.sendDeck(packId, this.pageX(event), this.pageY(event));

    event.preventDefault();
    event.stopPropagation();
  };

  this.gameAreaMouseDownListener = function (event) {

    this.removeClass(CssClass.CLICKED);
    this.removeClass(CssClass.DRAGGED);
    this.removeClass(CssClass.SELECTED);

    this.gameArea.gameAreaMouseDownX = this.pageX(event);
    this.gameArea.gameAreaMouseDownY = this.pageY(event);

    this.resizeSelectionBox(this.pageX(event), this.pageY(event), this.pageX(event), this.pageY(event), false);

    event.preventDefault();
    event.stopPropagation();
  };

  this.gameAreaMouseMoveListener = function (event) {

    this.removeClass(CssClass.CLICKED);

    var draggedCards = document.getElementsByClassName(CssClass.DRAGGED);
    var dragged = draggedCards.length > 0;

    if (dragged) {

      var selectedCards = document.getElementsByClassName(CssClass.SELECTED);
      var selected = selectedCards.length > 0;

      var cards = selected && draggedCards[0].classList.contains(CssClass.SELECTED)
        ? selectedCards
        : draggedCards;

      this.dragCards(
        cards,
        this.pageX(event) - draggedCards[0].cardMouseDownX,
        this.pageY(event) - draggedCards[0].cardMouseDownY);

      this.sendDrag(
        cards,
        this.pageX(event) - draggedCards[0].cardMouseDownX,
        this.pageY(event) - draggedCards[0].cardMouseDownY);

    } else if (this.buttons(event)) {

      this.resizeSelectionBox(
        this.gameArea.gameAreaMouseDownX,
        this.gameArea.gameAreaMouseDownY,
        this.pageX(event), this.pageY(event), false);
    }

    event.preventDefault();
    event.stopPropagation();
  };

  this.gameAreaMouseUpListener = function (event) {

    this.resizeSelectionBox(this.pageX(event), this.pageY(event), this.pageX(event), this.pageY(event), true);

    var minX = Math.min(this.gameArea.gameAreaMouseDownX, this.pageX(event));
    var minY = Math.min(this.gameArea.gameAreaMouseDownY, this.pageY(event));
    var maxX = Math.max(this.gameArea.gameAreaMouseDownX, this.pageX(event));
    var maxY = Math.max(this.gameArea.gameAreaMouseDownY, this.pageY(event));

    var cards = document.getElementsByClassName("card");
    for (var i = 0; i < cards.length; i++) {

      if (this.cardSelected(cards[i], minX, maxX, minY, maxY)) {
        cards[i].classList.add(CssClass.SELECTED);
      }
    }

    event.preventDefault();
    event.stopPropagation();
  };

  this.cardMouseDownListener = function (event) {

    var card = event.target;
    card.classList.add(CssClass.CLICKED);
    card.classList.add(CssClass.DRAGGED);

    var cardPosition = card.getBoundingClientRect();
    card.cardMouseDownX = this.pageX(event) - cardPosition.left;
    card.cardMouseDownY = this.pageY(event) - cardPosition.top;
    this.positionCard(card, cardPosition.left, cardPosition.top, ++this.zPositionMax);

    event.preventDefault();
    event.stopPropagation();
  };

  this.cardMouseUpListener = function (event) {

    var clickedCards = document.getElementsByClassName(CssClass.CLICKED);
    var clicked = clickedCards.length > 0;

    var selectedCards = document.getElementsByClassName(CssClass.SELECTED);
    var selected = selectedCards.length > 0;

    var draggedCards = document.getElementsByClassName(CssClass.DRAGGED);
    var dragged = draggedCards.length > 0;

    if (clicked && selected) {

      this.sendShuffle(
        selectedCards,
        this.pageX(event) - clickedCards[0].cardMouseDownX,
        this.pageY(event) - clickedCards[0].cardMouseDownY);

    } else if (clicked) {

      this.sendTurn(clickedCards[0]);

    } else if (dragged) {

      var cards = selected && draggedCards[0].classList.contains(CssClass.SELECTED)
        ? selectedCards
        : draggedCards;

      if (this.cardOffTable(cards[0])) {

        this.sendRemove(cards);

      } else {

        this.sendMove(
          cards,
          this.pageX(event) - draggedCards[0].cardMouseDownX,
          this.pageY(event) - draggedCards[0].cardMouseDownY);
      }
    }

    if (clicked || selected || dragged) {
      this.removeClass(CssClass.CLICKED);
      this.removeClass(CssClass.SELECTED);
      this.removeClass(CssClass.DRAGGED);

      event.preventDefault();
      event.stopPropagation();
    }
  };

  this.removeClass = function (className) {

    var elements = document.getElementsByClassName(className);
    while (elements.length > 0) {
      elements[0].classList.remove(className);
    }
  };

  this.cardIds = function (cards) {

    var cardIds = [];

    for (var i = 0; i < cards.length; i++) {
      cardIds.push(cards[i].id);
    }

    return cardIds;
  };

  this.createCard = function (element) {

    var card = document.createElement("img");
    card.id = element.id;
    card.faceDown = element.faceDown;
    card.backImage = element.backImage;

    if (element.faceDown) {
      card.src = element.backImage;
    } else {
      card.frontImage = element.frontImage;
      card.src = element.frontImage;
    }

    card.classList.add("card");

    this.positionCard(card, element.xposition, element.yposition, element.zposition);

    return card;
  };

  this.positionCard = function (card, xPosition, yPosition, zPosition) {

    if (this.zPositionMax < zPosition) {
      this.zPositionMax = zPosition;
    }

    card.style.position = "absolute";
    card.style.left = xPosition + "px";
    card.style.top = yPosition + "px";
    card.style.zIndex = zPosition;
  };

  this.turnCard = function (card, element) {

    card.faceDown = element.faceDown;

    if (element.faceDown) {
      card.src = element.backImage;
    } else {
      card.frontImage = element.frontImage;
      card.src = element.frontImage;
    }

    var cardPosition = card.getBoundingClientRect();
    this.positionCard(card, cardPosition.left, cardPosition.top, ++this.zPositionMax);
  };

  this.showCard = function (card) {
    card.style.display = "block";
  };

  this.hideCard = function (card) {
    card.style.display = "none";
  };

  this.dragCards = function (cards, xPosition, yPosition) {

    for (var i = 0; i < cards.length; i++) {
      this.positionCard(cards[i], xPosition, yPosition, ++this.zPositionMax);
      this.showCard(cards[i]);
    }
  };

  this.removeCard = function (card) {

    this.gameArea.removeChild(card);
  };

  this.resizeSelectionBox = function (x1, y1, x2, y2, hidden) {

    var selectionBox = document.getElementById("selection-box");
    if (hidden) {
      selectionBox.style.visibility = "hidden";
      return;
    } else {
      selectionBox.style.visibility = "visible";
    }

    selectionBox.style.left = Math.min(x1, x2) + "px";
    selectionBox.style.top = Math.min(y1, y2) + "px";
    selectionBox.style.width = Math.abs(x2 - x1) + "px";
    selectionBox.style.height = Math.abs(y2 - y1) + "px";
  };

  this.send = function (message) {
    this.webSocket.send(JSON.stringify(message));
  };

  this.sendShuffle = function (cards, xPosition, yPosition) {

    var modifier = this.inPocketArea(cards[0]) ? Modifier.PRIVATE : Modifier.PUBLIC;

    this.send({
      sender: this.playerId,
      type: Type.SHUFFLE,
      modifier: modifier,
      data: {
        cardIds: this.cardIds(cards),
        xPosition: xPosition,
        yPosition: yPosition,
        zPosition: ++this.zPositionMax
      }});
  };

  this.sendTurn = function (card) {

    var modifier = this.inPocketArea(card) ? Modifier.PRIVATE : Modifier.PUBLIC;

    this.send({
      sender: this.playerId,
      type: Type.TURN,
      modifier: modifier,
      data: {
        cardIds: [card.id],
        faceDown: !card.faceDown
      }});
  };

  this.sendRemove = function (cards) {

    this.send({
      sender: this.playerId,
      type: Type.REMOVE,
      modifier: Modifier.PUBLIC,
      data: {
        cardIds: this.cardIds(cards)
      }});
  };

  this.sendMove = function (cards, xPosition, yPosition) {

    var modifier = this.inPocketArea(cards[0]) ? Modifier.PRIVATE : Modifier.PUBLIC;

    this.send({
      sender: this.playerId,
      type: Type.MOVE,
      modifier: modifier,
      data: {
        cardIds: this.cardIds(cards),
        xPosition: xPosition,
        yPosition: yPosition,
        zPosition: ++this.zPositionMax
      }});
  };

  this.sendCards = function () {

    this.send({
      sender: this.playerId,
      type: Type.CARDS,
      modifier: Modifier.PUBLIC,
      data: {
        cardTableId: this.cardTableId
      }});
  };

  this.sendDeck = function (packId, xPosition, yPosition) {

    this.send({
      sender: this.playerId,
      type: Type.DECK,
      modifier: Modifier.PUBLIC,
      data: {
        cardTableId: this.cardTableId,
        packId: packId,
        xPosition: xPosition,
        yPosition: yPosition
      }});
  };

  this.sendDrag = function (cards, xPosition, yPosition) {

    var modifier = this.inPocketArea(cards[0]) ? Modifier.PRIVATE : Modifier.PUBLIC;

    this.send({
      sender: this.playerId,
      type: Type.DRAG,
      modifier: modifier,
      data: {
        cardIds: this.cardIds(cards),
        xPosition: xPosition,
        yPosition: yPosition
      }});
  };

  this.inPocketArea = function (card) {

    var cardPosition = card.getBoundingClientRect();
    var midY = cardPosition.top + cardPosition.height / 2;
    var pocketAreaPosition = this.pocketArea.getBoundingClientRect();

    return midY > pocketAreaPosition.top;
  };

  this.cardOffTable = function (card) {

    var cardPosition = card.getBoundingClientRect();
    var midX = cardPosition.left + cardPosition.width / 2;
    var midY = cardPosition.top + cardPosition.height / 2;
    var gameAreaPosition = this.gameArea.getBoundingClientRect();

    return  midX < 0 || midX > gameAreaPosition.width || midY < 0 || midY > gameAreaPosition.height;
  };

  this.cardSelected = function (card, minX, maxX, minY, maxY) {

    var cardPosition = card.getBoundingClientRect();
    var midX = cardPosition.left + card.width / 2;
    var midY = cardPosition.top + card.height / 2;

    return midX > minX && midX < maxX && midY > minY && midY < maxY;
  };

  this.pageX = function (event) {

    return event.pageX;
  };

  this.pageY = function (event) {

    return event.pageY;
  };

  this.buttons = function (event) {

    return event.buttons;
  };
}
