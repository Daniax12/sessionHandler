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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author aram
 */
public class MySessionHandler {
    private String sessionID;
    
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
    public void writeSessionData(HashMap<String, Object> sessionData) throws Exception{ 
        if(this.getSessionID() != null){
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                // Create a database connection
                connection = Database.dbConnect();
                int time = (int) (System.currentTimeMillis() / 1000);
                Gson gson = new Gson();

            // Convert the HashMap to JSON
                String json = gson.toJson(sessionData);

                // Define the SQL query for inserting data into the 'sessions' table
                String sql = "INSERT INTO sessions (id, access, data) VALUES (?, ?, ?) ON CONFLICT (id) DO UPDATE SET access = EXCLUDED.access, data = EXCLUDED.data";

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
    public MySessionHandler(){}

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


}
