package model;

public class Student {
    private String firstname;
    private String lastname;
    private String psuid;
    private int team;
    
    public Student(){
        this.firstname = "";
        this.lastname = "";
        this.psuid = "";
        this.team = -1;
    }

    public Student(String fName, String lName, String id, int teamId) {
        this.firstname = fName;
        this.lastname = lName;
        this.psuid = id;
        this.team = teamId;
    }

    public String getFirstname() {
        return firstname;
    }
    
    public void setFirstname(String fname){
        this.firstname = fname;
    }

    public String getLastname() {
        return lastname;
    }
    
    public void setLastname(String lname){
        this.lastname = lname;
    }

    public String getPsuid() {
        return psuid;
    }
    
    public void setPsuid(String id){
        this.psuid = id;
    }

    public int getTeam() {
        return team;
    }
    
    public void setTeam(int team){
        this.team = team;
    }
}
