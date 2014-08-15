<html>
<head>
    <script type="application/javascript" src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>

    <!--
      This example code is based on the BLOG entry at
        http://new-bamboo.co.uk/blog/2010/02/10/json-event-based-convention-websockets
    -->
    <script type="application/javascript" src="js/ws_event_dispatcher.js"></script>
</head>
<body>
    <noscript>
        <h2 style="color: #ff0000">
            Seems your browser doesn't support Javascript!
            Websockets rely on Javascript being enabled. Please enable Javascript and reload this page!
        </h2>
    </noscript>

    <h2>Data</h2>
    <table id="table1">
        <thead>
            <td style="text-decoration: blue; font: bold">id</td>
        </thead>
        <tbody>
        </tbody>
    </table>

    <script type="application/javascript">
        server = new ServerEventsDispatcher("ws://localhost:8080/websockets-example/websocket/mediaservice");

        server.bind('replace', function(eventData){
            console.log("* replacing " + eventData.length + " elements");

            // Refer to http://stackoverflow.com/a/13130467 for details on how to
            // update, add, and remove entries based on received data
            var tr = d3.select("#table1 tbody").selectAll("tr").data(eventData);

            // update

            // enter
            tr.enter().append("tr").attr("id", function(d) { return d.id });

            // update + enter
            tr.text(function(d) { return d.id });

            // exit
            tr.exit().remove();
        });

        server.bind('open', function() {
            // Connection open, so we can now send some initial data to the server
            server.send("greetings", "nice to meet you Server");
        });
    </script>
</body>
</html>
