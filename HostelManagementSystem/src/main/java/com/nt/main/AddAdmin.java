package com.nt.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Annotation to map this servlet to the URL pattern /addadmin
@WebServlet("/addadmin")
public class AddAdmin extends HttpServlet {
    
    // Handle GET requests
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Get the PrintWriter to write response data
        PrintWriter pw = res.getWriter();
        // Set the content type of the response
        res.setContentType("text/html");

        // Retrieve parameters from the request
        String username = req.getParameter("username").toUpperCase();
        String password = req.getParameter("password");
        String role = req.getParameter("role");

        try {
            // Print the received parameters to the console for debugging
            System.out.println(username);
            System.out.println(password);
            System.out.println(role);

            // Load the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Establish a connection to the database
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "proj", "hk");

            // Prepare an SQL statement to insert a new admin
            PreparedStatement ps = con.prepareStatement("INSERT INTO USERS VALUES(?,?,?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);

            // Execute the update and get the number of affected rows
            int count = ps.executeUpdate();
            
            // If one row was inserted, redirect to setting.html
            if (count == 1) {
                res.sendRedirect("setting.html");
            } else {
                // Otherwise, print an error message
                pw.println("<h1>Admin is not added</h1>");
            }
        } catch (Exception e) {
            // Print stack trace in case of an exception
            e.printStackTrace();
        }
    }

    // Handle POST requests by calling doGet method
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Delegate handling of POST requests to the doGet method
        doGet(req, res);
    }
}
