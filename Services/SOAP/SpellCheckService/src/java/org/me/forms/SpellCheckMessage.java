/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.forms;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrew
 */
public class SpellCheckMessage {
    ArrayList<String> mistakes = new ArrayList();
    ArrayList<List<String>> suggestions = new ArrayList();
    
    public ArrayList<String> getMisspelledWords(){
        return mistakes;
    }
    
    public void setMisspelledWords(ArrayList<String> misspelled){
        this.mistakes = misspelled;
    }
    
    public ArrayList<List<String>> getSuggestions(){
        return suggestions;
    }
    
    public void setSuggestions(ArrayList<List<String>> suggestions){
        this.suggestions = suggestions;
    }
}
