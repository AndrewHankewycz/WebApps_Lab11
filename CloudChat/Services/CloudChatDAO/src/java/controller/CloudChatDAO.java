package controller;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CrudDAO;
import exceptions.RoomAlreadyExistsException;
import exceptions.UserAlreadyExistsException;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import model.User;
import model.Message;
import model.Room;
import util.Crypto;
import com.google.gson.Gson;

@WebServlet(urlPatterns = {"/"})
public class CloudChatDAO extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CrudDAO dao;
    private Gson gson = new Gson();

    public CloudChatDAO() {
        dao = new CrudDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
   
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        PrintWriter out = null;
        if(action == null)
            return;
        
        switch (action) {
            case "register":
                String username = request.getParameter("username");
                String password = request.getParameter("password");

                String salt = UUID.randomUUID().toString();
                password = Crypto.sha1(password + salt);

                User user = new User(username, password, salt, new Time(System.currentTimeMillis()));
                int newUserId = -1;
                
                try {
                    newUserId = dao.addUser(user);
                } catch (UserAlreadyExistsException ex) {
                    //TODO: Send error that the user already exists
                    Logger.getLogger(CloudChatDAO.class.getName()).log(Level.SEVERE, null, ex);
                }

                out = response.getWriter();  
                out.print(newUserId);
                out.flush();
                
                break;
            case "login":
                // returns USER_ID on successful login
                // -1 on failed login
                String loginUsername = request.getParameter("username");
                String loginPassword = request.getParameter("password");

                int userId = -1;
                userId = dao.loginUser(loginUsername, loginPassword);
                    
                out = response.getWriter();  
                out.print(userId);
                out.flush();
              
                break;

            case "message":
                String msgText = request.getParameter("message");
                String msgUserId = request.getParameter("userId");
                String msgRoomId = request.getParameter("roomId");

                int roomIdInt;
                int userIdInt = roomIdInt = -1;

                try {
                    userIdInt = Integer.parseInt(msgUserId);
                    roomIdInt = Integer.parseInt(msgRoomId);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing userId or roomId in action 'message': " + e);
                    return;
                }

                Message message = new Message(msgText, userIdInt, roomIdInt, new Time(System.currentTimeMillis()));

                int messageId = dao.addMessage(message);
                
                out = response.getWriter();
                out.print(messageId);
                out.flush();
                
                break;
                
            case "createRoom":
                String topic = request.getParameter("topic");                
                
                Room room = new Room(topic, new Time(System.currentTimeMillis()));
                
                int newRoomId = -1;
                
                try {
                    newRoomId = dao.addRoom(room);
                } catch (RoomAlreadyExistsException ex) {
                    //TODO: Send error that the user already exists
                    Logger.getLogger(CloudChatDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                out = response.getWriter();
                out.print(newRoomId);
                out.flush();
                
                break;
                
            case "searchRoom":
                String searchTopic = request.getParameter("topic");
                
                int searchRoomId = dao.searchRoom(searchTopic);
                                
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setContentType("application/json"); 
                
                if(searchRoomId != -1){
                    out = response.getWriter();  
                    out.print(searchRoomId);
                    out.flush();
                }else{
                    out = response.getWriter();  
                    out.print("-1");
                    out.flush();
                }
                break;
                
            case "getRooms":
                Room[] rooms = dao.getAllRooms();
                
                if(rooms != null){
                    out = response.getWriter();  
                    out.print(gson.toJson(rooms));
                    out.flush();
                }else{
                    out = response.getWriter();  
                    out.print("-1");
                    out.flush();
                }
                
                break;
                
            case "browseTopic":
                String topic2 = request.getParameter("topic");
                
                Message[] messages = dao.searchTopic(topic2);
                                
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setContentType("application/json"); 
                
                if(messages != null){
                    out = response.getWriter();  
                    out.print(gson.toJson(messages));
                    out.flush();
                }else{
                    out = response.getWriter();  
                    out.print("-1");
                    out.flush();
                }
                break;
            default:
                System.out.println("CloudChatDAO cannot process action: " + action);
                out = response.getWriter();  
                out.print("-1");
                out.flush();
                break;
        }
    }
}
