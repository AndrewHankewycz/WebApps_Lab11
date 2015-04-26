/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Time;

/**
 *
 * @author andrew
 */
public class Message {
    private String message;
    private int userId;
    private int roomId;
    private Time createdTime;
    
    public Message(String message, int userId, int roomId, Time createdTime) {
        this.message = message;
        this.userId = userId;
        this.roomId = roomId;
        this.createdTime = createdTime;
    }
    
    public String getMessage() {
        return message;
    }
    
    public int getUserId() {
        return userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public Time getCreatedTime() {
        return createdTime;
    }
}
