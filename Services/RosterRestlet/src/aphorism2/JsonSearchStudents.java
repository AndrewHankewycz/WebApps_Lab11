package aphorism2;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import models.Student;
import org.restlet.data.Form;

public class JsonSearchStudents extends ServerResource {
    public JsonSearchStudents() { }

    @Get
    public String toJson() {
        ArrayList<Student> students = Students.getInstance().getList();

        Form form = getReference().getQueryAsForm();
        String psuid = form.getFirstValue("psuid");

        ArrayList<Student> studentsList = Students.getInstance().getList();

        Student s = null;

        for(int i = 0; i < studentsList.size(); i ++){
            if(studentsList.get(i).getPsuid().equals(psuid.toUpperCase())){
                s = studentsList.get(i);
            }
        }

        Gson gson = new Gson();
	return gson.toJson(s);
    }
}
