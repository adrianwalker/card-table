<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Card Table</title>
        <link rel="stylesheet" href="cardtable.css">
        <script>
            var playerId = "<%= request.getAttribute("playerId") %>";
            var cardTableId = "<%= request.getAttribute("cardTableId") %>";
        </script>
        <script src="utils.js"></script>
        <script src="cardtable.js"></script>
    </head>
    <body>
        <div id="container">
            <div id="side-nav">
                <div>
                    <img class="side-nav-icon"
                         src="standard/blackback.svg"
                         alt="Standard 52 card deck (Black)"
                         data-packId="1"/>
                </div>
                <div>
                    <img class="side-nav-icon" 
                         src="standard/redback.svg" 
                         alt="Standard 52 card deck (Red)"
                         data-packId="2"/>
                </div>
            </div>
            <div id="game-area">
                <div id="selection-box"></div>
                <div id="pocket-area"></div>
            </div>
        </div>
    </body>
</html>