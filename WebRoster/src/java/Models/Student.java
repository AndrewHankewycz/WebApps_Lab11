package Models;

public class Student {
    private String firstName;
    private String lastName;
    private String id;
    private int teamId;
    
    public Student(String fName, String lName, String id, int teamId){
        this.firstName = fName;
        this.lastName = lName;
        this.id = id;
        this.teamId = teamId;
    }
}
