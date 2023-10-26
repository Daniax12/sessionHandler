/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author aram
 */
public class Database {
    
    /*
    Get the connection
    */
     public static Connection dbConnect() throws Exception{
        String user = "postgres";
        String mdp = "mdpProm15";
        String nameDatabase = "haproxy";
        Connection temp = null;
        try {      

            Class.forName("org.postgresql.Driver");
            temp = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+nameDatabase, user, mdp);
            
            temp.setAutoCommit(false);
            // System.out.println(temp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Connection failed");
        }
        return temp;
    }
}
