import Models.Student;
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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class RosterRS {
    /**
     * File path that contains the student roster data
     */
    private static final String ROSTER_FILE_PATH = "roster.txt";
    /**
     * Map the student's Id to the Student object. This will result in faster
     * lookup times as all student Id's are unique.
     */
    private static Map<String, Student> students = new HashMap<>();
    private static ArrayList<Integer> teams = new ArrayList<>();
    
    @GET    
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/students")
    public Response getStudents() {
        fetchStudentsIfEmpty();
        try {
            return jsonResponse(mapToArray(students));
        } catch (IOException ex) {
            Logger.getLogger(RosterRS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return errorResponse();
    }
    
    @GET    
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/students/{psuid}")
    public Response getStudentsById(@PathParam("psuid") String key) {
        fetchStudentsIfEmpty();
        try {
            return jsonResponse(mapToArray(students.get(key)));
        } catch (IOException ex) {
            Logger.getLogger(RosterRS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return errorResponse();
    }
    
    @POST   
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/create/team")
    public Response createTeam(@FormParam("teamid") int teamId) {
        //Probably should throw an error if the team already exists
        if(!teams.contains(teamId)) {
            teams.add(teamId);
        }
        
        try {
            return jsonResponse();
        } catch (IOException ex) {
            Logger.getLogger(RosterRS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return errorResponse();
    }
    
    @POST   
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/create/student")
    public Response createStudent(
            @FormParam("psuid") String psuid, 
            @FormParam("firstname") String firstname, 
            @FormParam("lastname") String lastname, 
            @FormParam("team") int teamId) {
        
        if(teamId <= 0)
            return errorResponse();

        fetchStudentsIfEmpty();
        //TODO: Check if the psuid already exists
        students.put(psuid, new Student(firstname, lastname, psuid, teamId));
        
        try {
            return jsonResponse();
        } catch (IOException ex) {
            Logger.getLogger(RosterRS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return errorResponse();
    }
    
    public Response errorResponse() {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Error during the request!")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    public void fetchStudentsIfEmpty() {
        if (students.isEmpty()) {
            try {
                fetchRosterFromFile();
            } catch (FileNotFoundException | URISyntaxException ex) {
                Logger.getLogger(RosterRS.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(RosterRS.class.getName()).log(Level.SEVERE, null, ex);
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
    private Response jsonResponse(Object toJson) throws IOException {
        return Response.ok(new Gson().toJson(toJson), "application/json").build();
    }
    
    /**
     * Sends JSON response with student map
     *
     * @param response
     * @throws IOException
     */
    private Response jsonResponse() throws IOException {
        return this.jsonResponse(students);
    }
    
    @DELETE  
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/delete/student/{psuid}")
    public Response deleteStudent(@PathParam("psuid") String psuid) {
        fetchStudentsIfEmpty();
        students.remove(psuid);
        
        try {
            return jsonResponse();
        } catch (IOException ex) {
            Logger.getLogger(RosterRS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return errorResponse();
    }
    
    @DELETE  
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/delete/team/{team: \\d+}")
    public Response deleteTeam(@PathParam("team") int teamId) {
        fetchStudentsIfEmpty();
        
        //Iterate through the students map and remove all student entries
        //that are in the team that we're interested in deleting
        for(Iterator<Map.Entry<String, Student>> it = students.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Student> entry = it.next();
            if(entry.getValue().getTeam() == teamId) {
                it.remove();
            }
        }
        
        try {
            return jsonResponse();
        } catch (IOException ex) {
            Logger.getLogger(RosterRS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return errorResponse();
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
                    System.out.println("Cannot parse: " + line);
                }
            }

            count++;
        }
        
        br.close();
    }
}
