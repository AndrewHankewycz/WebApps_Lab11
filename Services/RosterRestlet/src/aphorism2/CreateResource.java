package aphorism2;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Student;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.data.Status;
import org.restlet.data.MediaType;
import org.restlet.data.Form;

public class CreateResource extends ServerResource {
    public CreateResource() { }

    @Post
    public Representation create(Representation data) {
	Status status = null;
	String msg = null;

	// Extract the data from the POST body.
	Form form = new Form(data);
        String psuid = form.getFirstValue("psuid");
        String firstname = form.getFirstValue("firstname");
        String lastname = form.getFirstValue("lastname");
        String team = form.getFirstValue("team");

        Student student = null;

        try{
            if ((psuid != null && !psuid.equals("")) &&
                    (firstname != null && !firstname.equals("")) &&
                    (lastname != null && !lastname.equals("")) &&
                    (team != null && !team.equals(""))) {
                int teamNum = Integer.parseInt(team);
                student = new Student(firstname, lastname, psuid, teamNum);

                Students.getInstance().addStudent(student);
            }else if((team != null && !team.equals("") &&
                    psuid == null && firstname == null && lastname == null)){
                int teamNum = Integer.parseInt(team);

                Students.getInstance().addTeam(teamNum);
                msg += "you've added a team\n";
            }else{
                msg = "variable is wrong";
            }

            ArrayList<Student> students = Students.getInstance().getList();
            for(int i = 0; i < students.size(); i++){
                msg += students.get(i).getFirstname() + "\n";
            }
            status = Status.SUCCESS_OK;
        }catch (NumberFormatException ex){
            msg += "Invalid request.\n";
	    status = Status.CLIENT_ERROR_BAD_REQUEST;
        }

	setStatus(status);
	return new StringRepresentation(msg, MediaType.TEXT_PLAIN);
    }
}
