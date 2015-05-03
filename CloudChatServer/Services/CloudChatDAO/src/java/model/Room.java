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
public class Room {
    private String topic;
    private Time createdTime;
    private int id;
    
    public Room(String topic, Time createdTime) {
        this.topic = topic;
        this.createdTime = createdTime;
        this.id = -1;
    }
    
    public Room(String topic, Time createdTime, int roomId){
        this.topic = topic;
        this.createdTime = createdTime;
        this.id = roomId;
    }
    
    public String getTopic() {
        return topic;
    }

    public Time getCreatedTime() {
        return createdTime;
    }
    
    public int getId(){
        return id;
    }
}
