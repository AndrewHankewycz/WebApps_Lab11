package models;

import java.io.IOException;
import org.restlet.ext.xml.DomRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Student {
    private String firstname;
    private String lastname;
    private String psuid;
    private int team;

    public Student(String fName, String lName, String id, int teamId) {
        this.firstname = fName;
        this.lastname = lName;
        this.psuid = id;
        this.team = teamId;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPsuid() {
        return psuid;
    }

    public int getTeam() {
        return team;
    }

    public String toPlain(){
        String s = psuid + "\t" + firstname + "\t" + lastname + "\t" + team;
        return s;
    }

    public Element toXML(DomRepresentation dom) throws IOException{
        Document doc = dom.getDocument();
        Element studentElement = doc.createElement("student");
        Element idElement = doc.createElement("psuId");
        idElement.appendChild(doc.createTextNode(psuid));
        Element fNameElement = doc.createElement("firstName");
        fNameElement.appendChild(doc.createTextNode(firstname));
        Element lNameElement = doc.createElement("lastName");
        lNameElement.appendChild(doc.createTextNode(lastname));
        Element teamElement = doc.createElement("team");
        teamElement.appendChild(doc.createTextNode(Integer.toString(team)));

        studentElement.appendChild(idElement);
        studentElement.appendChild(fNameElement);
        studentElement.appendChild(lNameElement);
        studentElement.appendChild(teamElement);
        return studentElement;
    }
}
