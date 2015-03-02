package Controllers;

import Models.Student;
import Util.HttpServletRequestUtil;
import com.google.gson.Gson;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import javax.xml.ws.http.HTTPException;

@WebServlet(name = "RosterCRUDController", urlPatterns = {"/rosterREST"})
public class RosterCRUDController extends HttpServlet {

    /**
     * File path that contains the student roster data
     */
    private static final String ROSTER_FILE_PATH = "roster.txt";
    /**
     * Map the student's Id to the Student object. This will result in faster
     * lookup times as all student Id's are unique.
     */
    private Map<String, Student> students = new HashMap<>();

    /**
     * Returns a student JSON string. If no Id is specified as as query string,
     * then it will return all students in the map converted to JSON.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String key = request.getParameter("psuid");

        fetchStudentsIfEmpty();
        sendJsonResponse(response, request.getParameterMap().containsKey("psuid") ? 
                mapToArray(students.get(key)) : mapToArray(students));
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String[] requestKeys = {"psuid", "firstname", "lastname", "team"};
        if (!HttpServletRequestUtil.requestContainsKeys(request, requestKeys)) {
            throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
        }

        String psuid = request.getParameter("psuid");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        int team = -1;

        try {
            team = Integer.parseInt(request.getParameter("team"));
        } catch (NumberFormatException e) {
            //Cannot convert team to integer
            throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
        }

        fetchStudentsIfEmpty();

        students.put(psuid, new Student(firstname, lastname, psuid, team));
        sendJsonResponse(response);
    }

    public void fetchStudentsIfEmpty() {
        if (students.isEmpty()) {
            try {
                fetchRosterFromFile();
            } catch (FileNotFoundException | URISyntaxException ex) {
                Logger.getLogger(RosterCRUDController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(RosterCRUDController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private ArrayList<Student> mapToArray(Map<String, Student> map){
        ArrayList<Student> studentsArray = new ArrayList();
        for(Student entry : students.values()) {
            studentsArray.add(entry);
        }
        return studentsArray;
    }
    
    private ArrayList<Student> mapToArray(Student student){
        ArrayList<Student> list = new ArrayList();
        list.add(student);
        return list;
    }

    /**
     * Prints a JSON converted object in the client's response
     *
     * @param response
     * @param toJson
     * @throws IOException
     */
    private void sendJsonResponse(HttpServletResponse response, Object toJson) throws IOException {
        response.setContentType("application/json");
//        ArrayList<Student> studentsArray = new ArrayList();
        PrintWriter out = response.getWriter();
//        for(Student entry : students.values()) {
//            studentsArray.add(entry);
//        }
        out.print(new Gson().toJson(toJson));
        out.flush();
    }

    /**
     * Sends JSON response with student map
     *
     * @param response
     * @throws IOException
     */
    private void sendJsonResponse(HttpServletResponse response) throws IOException {
        this.sendJsonResponse(response, students);
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        fetchStudentsIfEmpty();
        
        if (request.getParameterMap().containsKey("psuid")) {
            String key = request.getParameter("psuid");
            students.remove(key);
        } else if (request.getParameterMap().containsKey("team")) {
            int team = -1;

            try {
                team = Integer.parseInt(request.getParameter("team"));
            } catch (NumberFormatException e) {
                //Cannot convert team to integer
                throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
            }
            
            //Iterate through the students map and remove all student entries
            //that are in the team that we're interested in deleting
            for(Iterator<Map.Entry<String, Student>> it = students.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, Student> entry = it.next();
                if(entry.getValue().getTeam() == team) {
                    it.remove();
                }
            }
        }

        sendJsonResponse(response);
    }

    /**
     * Reads the roster from a file at path ROSTER_FILE_PATH. Creates a new key
     * value pair in the students HashMap for each student read in from the
     * file.
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @throws URISyntaxException
     */
    private void fetchRosterFromFile() throws FileNotFoundException, IOException, URISyntaxException {
        URL url = getClass().getResource(ROSTER_FILE_PATH);
        File f = new File(url.toURI());
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
                    System.out.println("Cannot parse: " + line);
                }
            }

            count++;
        }

        br.close();
    }

    // Method Not Allowed
    @Override
    public void doTrace(HttpServletRequest request, HttpServletResponse response) {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    // Method Not Allowed
    @Override
    public void doHead(HttpServletRequest request, HttpServletResponse response) {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    // Method Not Allowed
    @Override
    public void doOptions(HttpServletRequest request, HttpServletResponse response) {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
