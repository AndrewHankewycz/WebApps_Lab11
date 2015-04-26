/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import jdbc.DBUtility;
import model.User;
import model.Message;
import exceptions.UserAlreadyExistsException;
import java.sql.ResultSet;

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

    public void addUser(User user) throws UserAlreadyExistsException {
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
}
