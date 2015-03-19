<%-- 
    Document   : roster
    Created on : Feb 28, 2015, 10:03:27 PM
    Author     : andrew
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Roster</title>
        <link href="./CSS/roster.css" rel="stylesheet" type="text/css"/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $.ajax({
                  url: "/RosterMVC/rosterREST",
                  type: "GET",
                  success: function(data) {
                      roster = data;
                      createTable();
                  }
                });
            });
            
            function searchById(){
                var id = $('#psuidField').val().toUpperCase();
                $.ajax({
                    url: "/RosterMVC/rosterREST",
                    type: "GET",
                    data: {
                        psuid: id
                    },
                    success: function(data){
                        roster = data;
                        createTable();
                    }
                });
            }
            
            function getAll(){
                $.ajax({
                    url: "/RosterRS/rosterRS/students",
                    type: "GET",
                    success: function(data){
                        roster = data;
                        createTable();
                    }
                });
            }
            
            function addTeam(){
                var team = $('#teamField').val();
                
                if(!(team == null || team == "")){
                    var teamExp = new RegExp("[0-9]+");
                    
                    // check that the user entered a number for the team, alert otherwise
                    if(teamExp.test(team)){
                        $.ajax({
                            url: "/RosterRS/rosterRS/create/team",
                            type: "POST",
                            data: {
                                team: team
                            },
                            success: function(data){
                                //getAll();
                            }
                        });
                    }else{
                        alert("Input must be an integer");
                    }
                    
                }else{
                    alert("Team field must be complete");
                }
            }
            
            function addStudent(){
                var id = $('#psuidField').val();
                var fName = $('#firstnameField').val();
                var lName = $('#lastnameField').val();
                var team = $('#teamField').val();
                
                if(!(id == null || id == "") &&
                    !(fName == null || fName == "") &&
                    !(lName == null || lName == "") &&
                    !(team == null || team == "")){
                    
                    var teamExp = new RegExp("[0-9]+");
                    
                    // check that the user entered a number for the team, alert otherwise
                    if(teamExp.test(team)){
                        $.ajax({
                            url: "/RosterRS/rosterRS/create/student",
                            type: "POST",
                            data: {
                                psuid: id,
                                firstname: fName,
                                lastname: lName,
                                team: team
                            },
                            success: function(data){
                                getAll();
                            }
                        });
                    }else{
                        alert("'Team' must be a number");
                    }
                }else{
                    alert("All fields must be complete");
                }
            }
            
            function deleteStudent(){
                var id = $('#psuidField').val();
                var team = $('#teamField').val();
                var params;
                if(!(id == null || id == "")){
                    params = "?psuid=" + id;
                    
                    $.ajax({
                        url: "/RosterRS/rosterRS/delete/student/" +id,
                        type: "DELETE",
                        success: function(data){
                            getAll();
                        }
                    });
                }else if(!(team == null || team == "")){
                    $.ajax({
                        url: "/RosterRS/rosterRS/delete/team/" + team,
                        type: "DELETE",
                        success: function(data){
                            getAll();
                        }   
                    });
                }else{
                    alert("One of the fields must be complete");
                }
                
            }
            
            function clearTable(){
                $('#content').html("");
            }
            
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

                for(var i = 0; i < (roster.length - 1); i++){
                    for(var j = 0; j < (roster.length - i - 1); j++){
                        if(order === 1) {
                            if(roster[j][element] > roster[j + 1][element]){
                                swap(i, j);
                            }
                        } else {
                            if(roster[j][element] < roster[j + 1][element]) {
                                swap(i, j);
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

            function swap(i, j) {
                var temp = roster[j];
                roster[j] = roster[j+1];
                roster[j+1] = temp;
            }

            function createTable(){
                clearTable();   // if there is a table from a previous request
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

                for(var i = 0; i < roster.length; i++) {
                    if(typeof(roster[i]) === 'undefined' || roster[i] === null)
                        continue;
                    
                    var y = document.createElement("TR");
                    y.setAttribute("id", "myTr" + i);
                    document.getElementById("myTable").appendChild(y);

                    //access elements in i'th object in array and create a column for each
                    //append columns to the corresponding i'th row
                    var tableAttributes = ["lastname", "firstname", "psuid", "team"];
                    
                    for(var j = 0; j < tableAttributes.length; j++) {
                        var temp = document.createElement("TD");
                        temp.innerHTML = roster[i][tableAttributes[j]];
                        document.getElementById("myTr" + i).appendChild(temp);
                    }
                }
            }

            function showFieldsBlock(){
                $('#formFieldGroup').show(100);
                $('#psuidField').val("");
                $('#firstnameField').val("");
                $('#lastnameField').val("");
                $('#teamField').val("");
                $('#psuidField').attr('disabled', false);
                $('#teamField').attr('disabled', false);
            }

            function hideFieldsBlock(){
                $('#formFieldGroup').hide(100);
            }

            function showAddStudentFields(){
                showFieldsBlock();
                disableDeleteKeyHandlers();
                $('#idGroup').show(100);
                $('#fNameGroup').show(100);
                $('#lNameGroup').show(100);
                $('#teamGroup').show(100);
                $('#searchButton').hide(100);
                $('#addTeamButton').hide(100);
                $('#addStudentButton').show(100);
                $('#deleteButton').hide(100);
            }

            function showSearchFields(){
                showFieldsBlock();
                disableDeleteKeyHandlers();
                $('#idGroup').show(100);
                $('#fNameGroup').hide(100);
                $('#lNameGroup').hide(100);
                $('#teamGroup').hide(100);
                $('#searchButton').show(100);
                $('#addTeamButton').hide(100);
                $('#addStudentButton').hide(100);
                $('#deleteButton').hide(100);
            }
            
            function showAddTeamFields(){
                showFieldsBlock();
                disableDeleteKeyHandlers();
                $('#idGroup').hide(100);
                $('#fNameGroup').hide(100);
                $('#lNameGroup').hide(100);
                $('#teamGroup').show(100);
                $('#searchButton').hide(100);
                $('#addTeamButton').show(100);
                $('#addStudentButton').hide(100);
                $('#deleteButton').hide(100);
            }
            
            function enableDeleteKeyHandlers(){
                // if the user types in the team field, disable input in the id field
                $('#teamField').on('keydown', function(){
                    if($(this).val() != ""){
                        $('#psuidField').attr('disabled', true);
                    }else{
                        $('#psuidField').attr('disabled', false);
                    }
                });
                // if the user types in the id field, disable the team field
                $('#psuidField').on('keydown', function(){
                    if($(this).val() != ""){
                        $('#teamField').attr('disabled', true);
                    }else{
                        $('#teamField').attr('disabled', false);
                    }
                });
            }
            
            function disableDeleteKeyHandlers(){
                // if the user types in the team field, disable input in the id field
                $('#teamField').off('keydown');
                // if the user types in the id field, disable the team field
                $('#psuidField').off('keydown');
            }

            function showDeleteFields(){
                showFieldsBlock();
                enableDeleteKeyHandlers();
                
                $('#idGroup').show(100);
                $('#fNameGroup').hide(100);
                $('#lNameGroup').hide(100);
                $('#teamGroup').show(100); 
                $('#searchButton').hide(100);
                $('#addTeamButton').hide(100);
                $('#addStudentButton').hide(100);
                $('#deleteButton').show(100);
        }
        </script>
    </head>

<body>
<fieldset>
    <legend>Navigation</legend>
    </br>
    <button onClick="hideFieldsBlock(), getAll()" >View All</button>
    <button onClick="showSearchFields()" >Search</button>
    <button onClick="showAddTeamFields()" >Add Team</button>
    <button onClick="showAddStudentFields()" >Add</button>
    <button onClick="showDeleteFields()" >Delete</button>
  </fieldset>
    <div style="margin: 10px; display: none" id="formFieldGroup" >
        <div id="idGroup" >
            PSU ID:
            <input type="text" id="psuidField" />
        </div>
        <div id="fNameGroup" >
            First Name:
            <input type="text" id="firstnameField" />
        </div>
        <div id="lNameGroup" >
            Last name:
            <input type="text" id="lastnameField" />
        </div>
        <div id="teamGroup" >
            Team #:
            <input type="text" id="teamField" />
        </div>
        <input type="submit" value="Search" id="searchButton" onClick="searchById()"/>
        <input type="submit" value="Add" id="addTeamButton" onClick="addTeam()" />
        <input type="submit" value="Add" id="addStudentButton" onClick="addStudent()"/>
        <input type="submit" value="Delete" id="deleteButton" onClick="deleteStudent()"/>
    </div>
<div id="content"></div>

</body>
</html>

