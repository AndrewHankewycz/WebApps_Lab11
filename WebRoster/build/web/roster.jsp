<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Roster</title>
        <style>
table {

    border-collapse: collapse;
}

table, td, th {

    border: 1px solid black;
}

th {

    font-weight: normal;
    text-align: left;
    background-color: #3399FF;

    -webkit-touch-callout: none;
    -webkit-user-select: none;
    -khtml-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
}

td {

    width: 15em;
}

th:hover {

    background-color: #3399CC;
    cursor: pointer;
}

</style>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $.ajax({
                  url: "/WebRoster/roster",
                  type: "GET",
                  success: function(data) {
                      roster = data
                      console.log(data);
                      createTable();
                  }
                });
            });
        </script>
    </head>
    <body>
<script>

var roster = [];

var firstname = "FIRST_NAME";
var lastname = "LAST_NAME";
var id = "PSU_ID";
var team = "Team";
var arrow;
var order = 0;
var lastsort = -1;

function sortTable(sorttype){

    firstname = "FIRST_NAME";
    lastname = "LAST_NAME";
    id = "PSU_ID";
    team = "Team";


    if(lastsort !== sorttype){

        order = 1;
        lastsort = sorttype;

    }else{

        order *= -1;
    }

    if(order === 1){
        arrow = " &#x25B2";
    }else{
        arrow = " &#x25BC";
    }

    var element;

    switch(sorttype){

        case 0:
            element = "lastname";
            lastname += arrow;
            break;
        case 1:
            element = "firstname";
            firstname += arrow;
            break;
        case 2:
            element = "psuid";
            id += arrow;
            break;
        case 3:
            element = "team";
            team += arrow;
            break;
    }


    if(order === 1){
    //beastly bubble sorter
        for(var i = 0; i < (roster.length - 1); i++){

            for(var j = 0; j < (roster.length - i - 1); j++){

                if(roster[j][element] > roster[j + 1][element]){

                    var temp = roster[j];
                    roster[j] = roster[j+1];
                    roster[j+1] = temp;
                }
            }
        }
    }else{

        for(var i = 0; i < (roster.length - 1); i++){

            for(var j = 0; j < (roster.length - i - 1); j++){

                if(roster[j][element] < roster[j + 1][element]){

                    var temp = roster[j];
                    roster[j] = roster[j+1];
                    roster[j+1] = temp;
                }
            }
        }
    }

    var content = document.getElementById("content");
    while (content.hasChildNodes()) {
        content.removeChild(content.firstChild);
    }
    createTable();
}

function createTable(){

    var content = document.getElementById("content");
    document.body.appendChild(content);

    var x = document.createElement("TABLE");
    x.setAttribute("id", "myTable");
    document.getElementById("content").appendChild(x);

    //create a row for the header
    var rh = document.createElement("TR");
    rh.setAttribute("id", "myRH");
    document.getElementById("myTable").appendChild(rh);

    //insert header elements to the header row
    var h = document.createElement("TH");
    h.setAttribute("onclick", "sortTable(0)");
    h.innerHTML = lastname;
    document.getElementById("myRH").appendChild(h);

    h = document.createElement("TH");
    h.setAttribute("onclick", "sortTable(1)");
    h.innerHTML = firstname;
    document.getElementById("myRH").appendChild(h);

    h = document.createElement("TH");
    h.setAttribute("onclick", "sortTable(2)");
    h.innerHTML = id;
    document.getElementById("myRH").appendChild(h);

    h = document.createElement("TH");
    h.setAttribute("onclick", "sortTable(3)");
    h.innerHTML = team;
    document.getElementById("myRH").appendChild(h);

    for(var i = 0; i < roster.length; i++){

        var y = document.createElement("TR");
        y.setAttribute("id", "myTr" + i);
        document.getElementById("myTable").appendChild(y);

        //access elements in i'th object in array and create a column for each
            //append columns to the corresponding i'th row
            var z = document.createElement("TD");
            z.innerHTML = roster[i].lastname;
            document.getElementById("myTr" + i).appendChild(z);

            z = document.createElement("TD");
            z.innerHTML = roster[i].firstname;
            document.getElementById("myTr" + i).appendChild(z);

            z = document.createElement("TD");
            z.innerHTML = roster[i].psuid;
            document.getElementById("myTr" + i).appendChild(z);

            z = document.createElement("TD");
            z.innerHTML = roster[i].team;
            document.getElementById("myTr" + i).appendChild(z);

    }
}

</script>

<div id="content"></div>

</body>
</html>
