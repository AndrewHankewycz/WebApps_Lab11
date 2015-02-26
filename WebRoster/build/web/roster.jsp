<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $.ajax({
                  url: "/WebRoster/roster",
                  type: "GET",
                  success: function(data) {
                      console.log(data);
                  }
                });
            });
        </script>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
