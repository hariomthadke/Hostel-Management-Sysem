package com.nt.main;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Define the URL pattern for this servlet
@WebServlet("/feedback")
public class Feedback extends HttpServlet {
    
    // Handles GET requests
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Prepare to write response
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");
        
        // Redirect the user to the "underWork.html" page
        res.sendRedirect("underWork.html");
    }

    // Handles POST requests by calling the doGet method
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Delegate the handling of POST requests to the doGet method
        doGet(req, res);
    }
}
