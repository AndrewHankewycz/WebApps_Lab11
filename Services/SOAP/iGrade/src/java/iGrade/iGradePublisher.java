/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iGrade;

import javax.xml.ws.Endpoint;

/**
 *
 * @author andrew
 */
public class iGradePublisher {
    public static void main(String[ ] args) {
        final String url = "http://localhost:8080/GradeAgent";
        System.out.println("Publishing iGrade at endpoint " + url);
        Endpoint.publish(url, new iGrade());
    }
}
