package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jdbc.DBUtility;
import model.Student;

public class CrudDao {

    private Connection dbConnection;
    private PreparedStatement pStmt;

    public CrudDao() {
        dbConnection = DBUtility.getConnection();
    }

    public void addStudent(Student student) {
        String insertQuery = "INSERT INTO STUDENT(PSUID, FIRSTNAME, "
                + "LASTNAME, TEAM) VALUES (?,?,?,?)";
   
        try {
            pStmt = dbConnection.prepareStatement(insertQuery);
            pStmt.setString(1, student.getPsuid());
            pStmt.setString(2, student.getFirstname());
            pStmt.setString(3, student.getLastname());
            pStmt.setInt(4, student.getTeam());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void deleteStudent(String userId) {
        String deleteQuery = "DELETE FROM STUDENT WHERE PSUID = ?";
        try {
            pStmt = dbConnection.prepareStatement(deleteQuery);
            pStmt.setString(1, userId);
            pStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void updateStudent(Student student) {
        String updateQuery = "UPDATE STUDENT SET FIRSTNAME = ?, "
                + "LASTNAME = ?, TEAM = ? WHERE PSUID = ?";
        try {
            pStmt = dbConnection.prepareStatement(updateQuery);
            pStmt.setString(1, student.getFirstname());
            pStmt.setString(2, student.getLastname());
            pStmt.setInt(3, student.getTeam());
            pStmt.setString(4, student.getPsuid());
            pStmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<Student>();

        String query = "SELECT * FROM STUDENT ORDER BY PSUID";
        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Student student = new Student(rs.getString("FIRSTNAME"), rs.getString("LASTNAME"),
                        rs.getString("PSUID"), rs.getInt("TEAM"));

                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return students;
    }
    
    public List<Student> getStudents(int sindex, int size, String sorting) {
        List<Student> students = new ArrayList<Student>();
        if (sorting==null) sorting = "PSUID ASC";
        //String query = "SELECT * FROM STUDENT ORDER BY "+ sorting;
        String query = "SELECT * FROM ("+ 
            "SELECT ROW_NUMBER() OVER() AS rownum, STUDENT.* "+ 
            "FROM STUDENT ORDER BY "+ sorting+
            ") AS tmp "+ 
            "WHERE rownum >"+ sindex+" AND rownum <= "+(sindex+size); 
            //System.out.println(query);
        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Student student = new Student(rs.getString("FIRSTNAME"), rs.getString("LASTNAME"),
                        rs.getString("PSUID"), rs.getInt("TEAM"));
                
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return students;
    }    
    public int getTotalRecordCount() {
        int rowCount=0;
        // get the number of rows from the result set
        String query = "SELECT COUNT(*) FROM STUDENT";
        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            rowCount = rs.getInt(1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return rowCount;
    }    
}
