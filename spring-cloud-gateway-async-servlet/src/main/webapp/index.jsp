<html>
<body>
<h2>Hello World!</h2>
</body>

<script type="text/javascript">
    // 依赖 H5
    var sse = new EventSource("sse");
    sse.onmessage = function (ev) {
        console.log("message", ev.data, ev)
    }
    sse.addEventListener("me", function (ev) {
        console.log("me event", ev.data)
        if (ev.data == 3) {
            sse.close();
        }
    })
</script>
</html>
