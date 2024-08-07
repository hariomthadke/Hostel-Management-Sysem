package com.nt.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Define the URL pattern for this servlet
@WebServlet("/test")
public class Test extends HttpServlet {

    // Handles GET requests
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        // Set the content type of the response
        res.setContentType("text/html");
        
        // Get the PrintWriter to write the response
        PrintWriter pw = res.getWriter();
        
        // Write a header to the response
        pw.println("<h1> HK product</h1>");

        // Get the username parameter from the request and convert to uppercase
        String username = req.getParameter("userName").toUpperCase();
        
        // Get the password parameter from the request
        String password = req.getParameter("userPassword");
        
        // Print the username and password to the response
        pw.println(username);
        pw.println(password);

        try {
            // Load the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish connection to the database
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "proj", "hk");

            // Prepare SQL statement to check if user exists
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM USERS WHERE USERNAME=? AND PASSWORD=?");
            
            // Set parameters for the prepared statement
            ps.setString(1, username);
            ps.setString(2, password);
            
            // Execute the query
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                rs.next();
                int count = rs.getInt(1);

                if (count == 0) {
                    // Redirect to userNotFound.html if user does not exist
                    res.sendRedirect("userNotFound.html");
                } else {
                    // Prepare SQL statement to get the role of the user
                    PreparedStatement ps1 = con.prepareStatement("SELECT ROLES FROM USERS WHERE USERNAME=? AND PASSWORD=?");
                    
                    // Set parameters for the prepared statement
                    ps1.setString(1, username);
                    ps1.setString(2, password);
                    
                    // Execute the query
                    ResultSet rs1 = ps1.executeQuery();
                    rs1.next();
                    String role = rs1.getString(1);

                    if (role.equals("admin")) {
                        // Redirect to registration page if user is admin
                        res.sendRedirect("/HostelManagementSystem/registration");
                    } else if (role.equals("user")) {
                        // Redirect to user page if user is a regular user
                        res.sendRedirect("/HostelManagementSystem/userpage");
                    }
                }
            }

            // Close the connection
            con.close();
        } catch (Exception e) {
            // Print stack trace in case of an exception
            e.printStackTrace();
        }
    }

    // Handles POST requests by calling the doGet method
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Delegate the handling of POST requests to the doGet method
        doGet(req, res);
    }
}
