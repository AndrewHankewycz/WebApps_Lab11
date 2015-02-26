package Controllers;

import Models.Student;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

@WebServlet(name = "RosterServlet", urlPatterns = {"/roster"})
public class RosterServlet extends HttpServlet {
   private static final String ROSTER_FILE_PATH = "roster.txt";
   private String studentJson;
   
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response){
       try {
           if(studentJson == null) {
               studentJson = new Gson().toJson(getRosterFromFile());
           }
           
           response.setContentType("application/json");    
           PrintWriter out = response.getWriter();  
           out.print(studentJson);
           out.flush();
       } catch(IOException | URISyntaxException ex) {
            System.out.println(ex);
       }
   }

   private ArrayList<Student> getRosterFromFile() throws FileNotFoundException, IOException, URISyntaxException{
       ArrayList<Student> students = new ArrayList<>();
        
       URL url = getClass().getResource(ROSTER_FILE_PATH);
       File f = new File(url.toURI());
       BufferedReader br = new BufferedReader(new FileReader(f));
       
       String line;
       
       String lastName = "";
       String firstName = "";
       String id = "";
       int teamId = -1;
       
       int count = 0;
       while((line = br.readLine()) != null){
           if(line.equals("")) {
               count = 0;
               students.add(new Student(firstName, lastName, id, teamId));
               continue;
           }
           
           if(count == 0){
               lastName = line;
           }else if(count == 1){
               firstName = line;
           }else if(count == 2){
               id = line;
           }else if(count == 3 && !line.equals("")){
               try {
                   teamId = Integer.parseInt(line);
               } catch(Exception e) {
                   System.out.println("Cannot parse: " + line);
               }
           }
           
           count++;
       }
       
       br.close();
       
       return students;
   }
}
