/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientservlet;

import com.cdyne.ws.Check;
import com.cdyne.ws.DocumentSummary;
import com.cdyne.ws.Words;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import org.me.forms.SpellCheckMessage;

/**
 *
 * @author andrew
 */
@WebServlet(name = "SpellCheckServlet", urlPatterns = {"/SpellCheckServlet"})
public class SpellCheckServlet extends HttpServlet {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/wsf.cdyne.com/SpellChecker/check.asmx.wsdl")
    private Check service;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        
        
        try (PrintWriter out = response.getWriter()) {
            SpellCheckMessage msg = new SpellCheckMessage();
            ArrayList<String> mispelledWords = msg.getMisspelledWords();
            ArrayList<List<String>> suggestions = msg.getSuggestions();
            
            //Get the TextArea from the web page
            String TextArea1 = request.getParameter("report");

            //Initialize WS operation arguments
            java.lang.String bodyText = TextArea1;

            //Process result
            com.cdyne.ws.DocumentSummary doc = checkTextBodyV2(bodyText);

            //From the retrieved document summary,
            //identify the number of wrongly spelled words:

            //From the retrieved document summary,
            //identify the array of wrongly spelled words:
            List allwrongwords = doc.getMisspelledWord();

            //For every array of wrong words (one array per wrong word),
            //identify the wrong word, the number of suggestions, and
            //the array of suggestions. Then display the wrong word and the number of suggestions and
            //then, for the array of suggestions belonging to the current wrong word, display each
            //suggestion:
            for (int i = 0; i < allwrongwords.size(); i++) {
                String onewrongword = ((Words) allwrongwords.get(i)).getWord();
                mispelledWords.add(((Words) allwrongwords.get(i)).getWord());       // push misspelled word into list
                suggestions.add(((Words) allwrongwords.get(i)).getSuggestions());
            }
            
            msg.setMisspelledWords(mispelledWords);
            msg.setSuggestions(suggestions);

            sendJsonResponse(response, msg);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private DocumentSummary checkTextBodyV2(java.lang.String bodyText) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        com.cdyne.ws.CheckSoap port = service.getCheckSoap();
        return port.checkTextBodyV2(bodyText);
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
