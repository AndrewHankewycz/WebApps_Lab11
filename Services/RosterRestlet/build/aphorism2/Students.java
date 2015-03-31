package aphorism2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Student;

public class Students {
    private static String ROSTER_FILE_PATH = "";
    private static Students self;
    private static Map<String, Student> students = new HashMap<>();
    private static ArrayList<Integer> teams = new ArrayList<>();
    private static AtomicInteger id;

    static {
	id = new AtomicInteger();
    }

    private Students(){
        fetchStudentsIfEmpty();
    }

    public void addStudent(Student s){
        students.put(s.getPsuid(), s);
    }

    public void addTeam(int team){
        teams.add(team);
    }

    public static Students getInstance(){
        if(self == null){
            self = new Students();
        }
        return self;
    }

    public ArrayList<Student> getList() { return mapToArray(students); }

    private static ArrayList<Student> mapToArray(Map<String, Student> map){
        ArrayList<Student> studentsArray = new ArrayList();
        for(Student entry : students.values()) {
            studentsArray.add(entry);
        }
        return studentsArray;
    }

    public void fetchStudentsIfEmpty() {
        if (students.isEmpty()) {
            try {
                fetchRosterFromFile();
            } catch (FileNotFoundException | URISyntaxException ex) {
              Logger.getLogger (Students.class.getName()).log(Level.SEVERE, ex.toString());
            } catch (IOException ex) {
            }
        }
    }

    public static void setPath(String path){
        ROSTER_FILE_PATH = path;
    }

    private void fetchRosterFromFile() throws FileNotFoundException, IOException, URISyntaxException {
        File f = new File(ROSTER_FILE_PATH);
        BufferedReader br = new BufferedReader(new FileReader(f));

        String line;

        String lastName = "";
        String firstName = "";
        String id = "";
        int teamId = -1;

        int count = 0;

        while ((line = br.readLine()) != null) {
            if (line.equals("")) {
                count = 0;
                if(!teams.contains(teamId)) {
                    teams.add(teamId);
                }

                students.put(id, new Student(firstName, lastName, id, teamId));
                continue;
            }

            if (count == 0) {
                lastName = line;
            } else if (count == 1) {
                firstName = line;
            } else if (count == 2) {
                id = line;
            } else if (count == 3 && !line.equals("")) {
                try {
                    teamId = Integer.parseInt(line);
                } catch (Exception e) {
                    // do nothing
                }
            }

            count++;
        }

        br.close();
    }

    public void removeStudent(String psuid){
        if(students.get(psuid.toUpperCase()) != null) {
            students.remove(psuid);
        }
    }

    public void removeStudent(int team){
        //Iterate through the students map and remove all student entries
        //that are in the team that we're interested in deleting
        for(Iterator<Map.Entry<String, Student>> it = students.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Student> entry = it.next();
            if(entry.getValue().getTeam() == team) {
                it.remove();
            }
        }
    }
}
