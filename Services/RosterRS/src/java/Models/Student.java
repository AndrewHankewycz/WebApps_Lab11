package Models;

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
}
