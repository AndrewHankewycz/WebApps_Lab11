package Navigator;

import UserManagerClients.UserManager;
import UserManagerClients.UserManagerService;
import com.google.gson.Gson;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet(name = "Navigator", urlPatterns = {"/"})
public class CloudChatNavigator extends HttpServlet {
    
    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        UserManagerService service = new UserManagerService();
        UserManager port = service.getUserManagerPort();

        PrintWriter out = null;
        String res = "";
        
        switch(action){
            case "login":
                String loginUsername = request.getParameter("username");
                String loginPassword = request.getParameter("password");

                // soap service returns JSON formatted data ready to send to the client
                res = port.userLogin(loginUsername, loginPassword);
                
                out = response.getWriter();
                out.print(res);
                out.flush();
                
                break;
            case "logout":
                String id = request.getParameter("username");

                try{
                    int userId = Integer.parseInt(id);
                    port.userLogout(userId);

                }catch(NumberFormatException ex){
                    Logger.getLogger(CloudChatNavigator.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "register":
                break;
        }
        
        //sendJsonResponse(response, res);
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
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(toJson));
        out.flush();
    }
}
