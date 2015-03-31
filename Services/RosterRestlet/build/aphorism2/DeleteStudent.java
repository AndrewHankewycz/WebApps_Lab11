package aphorism2;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Student;
import org.restlet.data.Form;
import org.restlet.resource.ServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.data.Status;
import org.restlet.data.MediaType;
import org.restlet.resource.Delete;

public class DeleteStudent extends ServerResource {
    public DeleteStudent() { }

    @Delete
    public Representation delete(Representation data) {
        Status status = null;
        String msg = null;

        Form form = getReference().getQueryAsForm();
        String psuid = form.getFirstValue("psuid");
        String team = form.getFirstValue("team");

        ArrayList<Student> studentsList = Students.getInstance().getList();

        if(team != null && !team.equals("")){
            int i = -1;
            i = team.indexOf("?team=");
            if(i > -1){
                i += 6;
                team = team.substring(i);
            }
        }

        if (psuid != null && !psuid.equals("")) {
            Students.getInstance().removeStudent(psuid);
            status = Status.SUCCESS_OK;
        } else if (team != null && !team.equals("")) {
            int teamNum = -1;

            try{
                teamNum = Integer.parseInt(team);
                status = Status.SUCCESS_OK;
            }catch (NumberFormatException ex){
                msg += "NumberFormatException.\n";
                status = Status.CLIENT_ERROR_BAD_REQUEST;
            }

            Students.getInstance().removeStudent(teamNum);
        }

        setStatus(status);
        return new StringRepresentation(msg, MediaType.TEXT_PLAIN);
    }
}
