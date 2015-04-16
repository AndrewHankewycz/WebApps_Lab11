package GradeAgents;

import com.google.gson.Gson;
import iGradeClients.IGrade;
import iGradeClients.IGradeService;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import javax.xml.ws.http.HTTPException;

@WebServlet(name = "GradeAgents", urlPatterns = {"/gradeAgent"})
public class GradeAgents extends HttpServlet {
    
    public static final String SEVEN_SCALE = "7-scale";
    public static final String HUNDRED_SCALE = "100-scale";
    public static final String LETTER_SCALE = "letter-grade";

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String[] requestKeys = {"from", "to", "grade"};
        
        if (!HttpServletRequestUtil.requestContainsKeys(request, requestKeys)){
            throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
        }
        
        String fromString = request.getParameter("from");
        String toString = request.getParameter("to");
        String gradeString = request.getParameter("grade");
        String scaledGrade = "";
        
        System.out.println(fromString);
        System.out.println(toString);
        System.out.println(gradeString);
        
        if(fromString != null && toString != null && gradeString != null &&
                !fromString.equals("") && !toString.equals("") && !gradeString.equals("")){
            try{
                IGradeService service = new IGradeService();
                service.setHandlerResolver(new ClientHandlerResolver("curly", "CurlyCurlyCurly"));
                IGrade port = service.getIGradePort();
                
                service.setHandlerResolver(null);
                    
                if(toString.equals(LETTER_SCALE)){
                    System.out.println("CONV. TO LETTER SCALE");
                    scaledGrade = port.toLetterScale(gradeString, fromString);
                }else if(toString.equals(HUNDRED_SCALE)){
                    System.out.println("CONV. TO HUNDRED SCALE");
                    scaledGrade = port.toHundredScale(gradeString, fromString);
                }else if(toString.equals(SEVEN_SCALE)){
                    System.out.println("CONV. TO SEVEN SCALE");
                    scaledGrade = port.toSevenScale(gradeString, fromString);
                }else{
                    // return error
                }
            }catch (NumberFormatException ex){
                throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        sendJsonResponse(response, scaledGrade);
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
