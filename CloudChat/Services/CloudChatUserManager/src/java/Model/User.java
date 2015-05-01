/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Time;

/**
 *
 * @author andrew
 */
public class User {
    private int userId;
    private String username;
    
    public User(int id, String username) {
        this.userId = id;
        this.username = username;
    }
    
    public int getUserId(){
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
}
