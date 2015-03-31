package aphorism2;

import java.io.File;
import javax.servlet.ServletContext;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.routing.Router;
import org.restlet.data.Status;
import org.restlet.data.MediaType;

public class AdagesApplication extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {
	// To illlustrate the different API possibilities, implement the
	// DELETE operation as an anonymous Restlet class. For the
	// remaining operations, follow Restlet best practices and
	// implement each as a Java class.

      ServletContext servlet = (ServletContext) this.getContext().getAttributes().get("org.restlet.ext.servlet.ServletContext");
      String rosterPath = servlet.getRealPath("/roster.txt");
      File configFile = new File(rosterPath);
      Students.setPath(rosterPath);


	// Create the routing table.
	Router router = new Router(getContext());
	router.attach("/json",            JsonAllStudents.class);
	router.attach("/create",          CreateResource.class);
	router.attach("/delete",          DeleteStudent.class);
  router.attach("/search",          JsonSearchStudents.class);


        return router;
    }

    private String badRequest(String msg) {
	Status error = new Status(Status.CLIENT_ERROR_BAD_REQUEST, msg);
	return error.toString();
    }

}
