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

// Define the URL pattern for this servlet
@WebServlet("/userregistration")
public class UserRegistration extends HttpServlet {

    // Handles GET requests
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Get the PrintWriter to write the response
        PrintWriter pw = res.getWriter();
        
        // Set the content type of the response
        res.setContentType("text/html");
        
        // Print a message to the response
        pw.println("user registration");
        
        // Get parameters from the request and process them
        String regno = req.getParameter("regno").toUpperCase(); // Get registration number and convert to uppercase
        String purpose = req.getParameter("purpose");            // Get purpose
        String year = req.getParameter("year");                  // Get year
        String address = req.getParameter("address");            // Get address
        
        // Print the parameters to the response
        pw.println(regno);
        pw.println(purpose);
        pw.println(year);
        pw.println(address);

        try {
            // Load the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            // Establish connection to the database
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "proj", "hk");
            
            // Prepare SQL statement to insert user registration details into the REGISTRATION table
            PreparedStatement ps = con.prepareStatement("INSERT INTO REGISTRATION VALUES(?,?,?,?)");
            
            // Set parameters for the prepared statement
            ps.setString(1, regno);
            ps.setString(2, year);
            ps.setString(3, address);
            ps.setString(4, purpose);
            
            // Execute the update
            int count = ps.executeUpdate();
            
            // If the registration is successfully inserted, redirect to success.html
            if (count == 1) {
                res.sendRedirect("success.html");
            }
            
            // Close the connection
            con.close();
        } catch (Exception e) {
            // Print stack trace in case of an exception
            e.printStackTrace();
            
            // Redirect to InvalidCredentials.html in case of an exception
            res.sendRedirect("InvalidCredentials.html");
        }
    }

    // Handles POST requests by calling the doGet method
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Delegate the handling of POST requests to the doGet method
        doGet(req, res);
    }
}
