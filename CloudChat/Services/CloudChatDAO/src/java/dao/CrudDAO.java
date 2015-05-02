/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import exceptions.RoomAlreadyExistsException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jdbc.DBUtility;
import model.User;
import model.Message;
import exceptions.UserAlreadyExistsException;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Room;
import util.Crypto;

/**
 *
 * @author andrew
 */
public class CrudDAO {

    private Connection dbConnection;
    private PreparedStatement pStmt;

    public CrudDAO() {
        dbConnection = DBUtility.getConnection();
    }

    public int addUser(User user) throws UserAlreadyExistsException {
        int userId = -1;
        try {
            if (userExists(user)) {
                throw new UserAlreadyExistsException("User " + user.getUsername() + " already exists");
            }

            String insertSql = "INSERT INTO USERS (USERNAME, PASSWORD, SALT, CREATED_TIME) VALUES (?,?,?,?)";
            pStmt = dbConnection.prepareStatement(insertSql);
            pStmt.setString(1, user.getUsername());
            pStmt.setString(2, user.getPassword());
            pStmt.setString(3, user.getSalt());
            pStmt.setTime(4, user.getCreatedTime());
            pStmt.executeUpdate();
            
            String lookUpSql = "SELECT * FROM USERS WHERE USERNAME = ?";
            pStmt = dbConnection.prepareStatement(lookUpSql);
            pStmt.setString(1, user.getUsername());
            ResultSet rset = pStmt.executeQuery();
            if(rset.next()){
                userId = rset.getInt(1);
            }
        } catch (SQLException e) {
            Logger.getLogger(CrudDAO.class.getName()).log(Level.SEVERE, null, userId);
        }
        return userId;
    }
    
    public int loginUser(String username, String password){
        // on success: returns the USER ID of the username and password submitted
        // on failure: returns -1
        int userId = -1;
        
        try {
            String loginSql = "SELECT * FROM USERS WHERE USERNAME = ?";
            pStmt = dbConnection.prepareStatement(loginSql);
            pStmt.setString(1, username);
            
            ResultSet rset = pStmt.executeQuery();
            if(rset.next()){
                String salt = rset.getString(4);
                String correctPswd = rset.getString(3);

                String saltedPswd = Crypto.sha1(password + salt);
                
                if(saltedPswd.equals(correctPswd)){
                    userId = rset.getInt(1);
                }else{
                    System.out.println("incorrect password entered");
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return userId;
    }
    
    public void addRoom(Room room) throws RoomAlreadyExistsException{
        try {
            if (roomExists(room)) {
                throw new RoomAlreadyExistsException("Room " + room.getTopic() + " already exists");
            }

            String sql = "INSERT INTO ROOMS (TOPIC, CREATED_TIME) VALUES (?, ?)";
            pStmt = dbConnection.prepareStatement(sql);
            pStmt.setString(1, room.getTopic());
            pStmt.setTime(2, room.getCreatedTime());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void addMessage(Message msg) {
        String sql = "INSERT INTO MESSAGES (MESSAGE, USER_ID, ROOM_ID, CREATED_TIME) VALUES (?,?,?,?)";
        try {
            pStmt = dbConnection.prepareStatement(sql);
            pStmt.setString(1, msg.getMessage());
            pStmt.setInt(2, msg.getUserId());
            pStmt.setInt(3, msg.getRoomId());
            pStmt.setTime(4, msg.getCreatedTime());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (pStmt != null) {
                try {
                    pStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean userExists(User user) throws SQLException {
        int count = 0;
        PreparedStatement stmt = null;
        ResultSet rset = null;
        try {
            stmt = dbConnection.prepareStatement(
                    "SELECT COUNT(ID) FROM USERS WHERE USERNAME=?");
            stmt.setString(1, user.getUsername());
            rset = stmt.executeQuery();
            if (rset.next()) {
                count = rset.getInt(1);
            }
            return count > 0;
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public boolean roomExists(Room room) throws SQLException {
        int count = 0;
        PreparedStatement stmt = null;
        ResultSet rset = null;
        try {
            stmt = dbConnection.prepareStatement(
                    "SELECT COUNT(TOPIC) FROM ROOMS WHERE TOPIC=?");
            stmt.setString(1, room.getTopic());
            rset = stmt.executeQuery();
            if (rset.next()) {
                count = rset.getInt(1);
            }
            return count > 0;
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public int searchRoom(String topic) {
        int id = -1;
        ResultSet rset = null;
        
        String sql = "SELECT ID FROM ROOMS WHERE TOPIC = ?";
        try {
            pStmt = dbConnection.prepareStatement(sql);
            pStmt.setString(1, topic);
            rset = pStmt.executeQuery();
            if (rset.next()) {
                id = rset.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (pStmt != null) {
                try {
                    pStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return id;
    }
    
    public Room[] getAllRooms(){
        ArrayList<Room> rooms = new ArrayList<>();
        Room[] roomsArray = null;
        
        String sql = "SELECT * FROM ROOMS";
        try {
            pStmt = dbConnection.prepareStatement(sql);
            ResultSet rset = pStmt.executeQuery();
            while(rset.next()) {
                int roomId = rset.getInt(1);
                String roomTopic = rset.getString(2);
                Time roomTimestamp = rset.getTime(3);
                rooms.add(new Room(roomTopic, roomTimestamp, roomId));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (pStmt != null) {
                try {
                    pStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if(rooms.size() > 0){
            roomsArray = new Room[rooms.size()];
            roomsArray = rooms.toArray(roomsArray);
        }
        return roomsArray;
    }
    
    public Message[] searchTopic(String topic) {
        ArrayList<Message> messages = new ArrayList<>();
        Message[] msgs = null;
        
        String sql = "SELECT * FROM MESSAGES WHERE ROOM_ID = (" +
                       "SELECT ID FROM ROOMS WHERE TOPIC = ?)";
        try {
            pStmt = dbConnection.prepareStatement(sql);
            pStmt.setString(1, topic);
            ResultSet rset = pStmt.executeQuery();
            while(rset.next()) {
                int msgUserId = rset.getInt(2);
                String msgText = rset.getString(3);
                Time msgTimestamp = rset.getTime(4);
                int msgRoomId = rset.getInt(5);
                
                messages.add(new Message(msgText, msgUserId, msgRoomId, msgTimestamp));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (pStmt != null) {
                try {
                    pStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if(messages.size() > 0){
            msgs = new Message[messages.size()];
            msgs = messages.toArray(msgs);
        }
        return msgs;
    }
}
