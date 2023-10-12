/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.MySessionHandler;

/**
 *
 * @author aram
 */
@WebServlet(name = "ServletSession", urlPatterns = {"/ServletSession"})
public class ServletSession extends HttpServlet {

    private MySessionHandler mySessionHandler = new MySessionHandler();
    private HashMap<String, Object> sessionData;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String mySessionId = mySessionHandler.getSessionIdFromCookies(request);
        
        if(mySessionId == null){
            mySessionHandler.createNewSession();
            
            Cookie sessionCookie = new Cookie("sessionId", mySessionHandler.getSessionID());
            response.addCookie(sessionCookie);
        } 
        sessionData = mySessionHandler.readSessionData();

        // Afaka jerena eto ny data
        // Afaka manampy data anaty session data 
        sessionData.put("name", "JhonDoe");
        
        try {
            mySessionHandler.writeSessionData(sessionData);
            out.println("Hello");
        } catch (Exception e) {
             out.println("Error with the following " + e);
        }
        
        
        
        
        
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet ServletSession</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet ServletSession at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
