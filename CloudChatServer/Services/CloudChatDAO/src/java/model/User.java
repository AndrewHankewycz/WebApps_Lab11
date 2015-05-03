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
public class User {
    private String username;
    private String password;
    private String salt;
    private Time createdTime;
    
    public User(String username, String password, String salt, Time createdTime) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.createdTime = createdTime;
    }
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public String getSalt() {
        return salt;
    }

    public Time getCreatedTime() {
        return createdTime;
    }
}
