/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author aram
 */
public class MySessionHandler {
    private String sessionID;
    private HashMap<String, Object> sessionData;
    
     /*
    DECONNECTION OF THE SESSION
    */
    public void destroy() throws Exception{
        if(this.getSessionID() != null){
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                // Create a database connection
                connection = Database.dbConnect();

                // Define the SQL query for inserting data into the 'sessions' table
                String sql = "delete from sessions where id = ?";
                // Create a prepared statement with the SQL query
                preparedStatement = connection.prepareStatement(sql);
                 preparedStatement.setString(1, this.getSessionID());
                // Execute the query to insert data
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                // Handle database connection and query errors
                e.printStackTrace();
            } finally {
                // Close the resources
                try {
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    /*
    DELETE ALL COOKIES AFTER 30 MIN
    */
    public static void cleanSessions() throws Exception{
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Create a database connection
            connection = Database.dbConnect();

            // Define the SQL query for inserting data into the 'sessions' table
            String sql = "delete from sessions where datesessions < now() - interval '30 minutes'";

            // Create a prepared statement with the SQL query
            preparedStatement = connection.prepareStatement(sql);
            // Execute the query to insert data
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            // Handle database connection and query errors
            e.printStackTrace();
        } finally {
            // Close the resources
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /*
    Add Item session
    */
    public void addSessionItem(String key, Object value){
        if(this.getSessionData() == null) this.setSessionData(new HashMap<String, Object>());
        this.getSessionData().put(key, value);
    }
    
     /*
     * Get the SessionData
     * If the SessionID is null -> Null
     */
    public HashMap<String, Object> readSessionData(){
        HashMap<String, Object> dataMap = new HashMap<>();
        if(this.getSessionID() == null) return dataMap;
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.dbConnect();
            String sql = "SELECT data FROM sessions WHERE id = ?";

            // Create a prepared statement with the SQL query
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.getSessionID());

            // Execute the query and retrieve the 'data' text
            resultSet = preparedStatement.executeQuery();

            String data = null;
            if (resultSet.next()) {
                data = resultSet.getString("data");

                // Use Gson to parse the JSON String
                Gson gson = new Gson();
                Type type = new TypeToken<HashMap<String, Object>>() {}.getType();
                dataMap = gson.fromJson(data, type);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return dataMap;
        }
    }
    
     /*
     * STORING THE SESSIONID & SESSION DATA in DATABASE
     */
    public void writeSessionData() throws Exception{ 
        if(this.getSessionID() != null){
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                // Create a database connection
                connection = Database.dbConnect();
                int time = (int) (System.currentTimeMillis() / 1000);
                Gson gson = new Gson();

            // Convert the HashMap to JSON
                String json = gson.toJson(this.getSessionData());

                // Define the SQL query for inserting data into the 'sessions' table
                String sql = "INSERT INTO sessions (id, access, data, dateSessions) VALUES (?, ?, ?, NOW()) ON CONFLICT (id) DO UPDATE SET access = EXCLUDED.access, data = EXCLUDED.data";

                // Create a prepared statement with the SQL query
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, this.getSessionID());
                preparedStatement.setInt(2, time);
                preparedStatement.setString(3, json);

                // Execute the query to insert data
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                // Handle database connection and query errors
                e.printStackTrace();
            } finally {
                // Close the resources
                try {
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /*
     * Creating new Session or Setting it
     */
    public void createNewSession(){
        String temp = "session_" + System.currentTimeMillis();
        this.setSessionID(temp);
    }

    
    
    /*
    Getting the cookies
    */
    
    public String getSessionIdFromCookies(HttpServletRequest request) {
        String myCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName())) {
                    myCookie = cookie.getValue();
                }
            }
        }
        this.setSessionID(myCookie);
        return myCookie;
    }
    
    // Constructiors
    public MySessionHandler() throws Exception{
        try {
            cleanSessions();
        } catch (Exception e) {
            throw new Exception("Error on constructing the SessionHAndler and cleaning session data");
        }
    }

    public MySessionHandler(String sessionID) {
        this.setSessionID(sessionID);
    }

    // Getters and Setters
    public String getSessionID(){
        return this.sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public HashMap<String, Object> getSessionData() {
        return sessionData;
    }

    public void setSessionData(HashMap<String, Object> sessionData) {
        this.sessionData = sessionData;
    }
    
    


}
