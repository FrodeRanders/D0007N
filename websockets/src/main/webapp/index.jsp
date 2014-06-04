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

    <h2>Media files:</h2>

    <script type="application/javascript">
        server = new ServerEventsDispatcher("ws://localhost:8080/websockets-example/websocket/mediaservice");

        server.bind('list_update', function(eventData){
            console.log("* got " + eventData.length + " elements");

            // Refer to http://stackoverflow.com/a/13130467 for details on how to
            // update, add, and remove entries based on received data
            var p = d3.select(document.body).selectAll("p").data(eventData);

            // update
            p.attr("class", "update");

            // enter
            p.enter().append("p")
                     .attr("class", "enter")
                     .attr("id", function(d) { return d.id });

            // update + enter
            p.text(function(d) { return d.id });

            // exit
            p.exit().remove();
         });
    </script>

</body>
</html>
