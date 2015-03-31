package aphorism2;

import com.google.gson.Gson;
import java.util.ArrayList;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import models.Student;

public class JsonAllStudents extends ServerResource {
    public JsonAllStudents() { }

    @Get
    public String toJson() {
        ArrayList<Student> students = Students.getInstance().getList();
        Gson gson = new Gson();

	       return gson.toJson(students);
    }
}
