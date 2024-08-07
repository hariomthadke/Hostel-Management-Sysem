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
@WebServlet("/userleaveapplication")
public class UserLeaveApplication extends HttpServlet {

    // Handles GET requests
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Get the PrintWriter to write the response
        PrintWriter pw = res.getWriter();
        
        // Set the content type of the response
        res.setContentType("text/html");
        
        // Print a message to the response
        pw.println("user leave application");
        
        // Get parameters from the request and process them
        String regno = req.getParameter("regno").toUpperCase(); // Get registration number and convert to uppercase
        String arrivalDate = req.getParameter("arrival_date");  // Get arrival date
        String departDate = req.getParameter("departure_date"); // Get departure date
        String reason = req.getParameter("reason");             // Get reason for leave
        
        // Print the parameters to the response
        pw.println(regno);
        pw.println(arrivalDate);
        pw.println(departDate);
        pw.println(reason);

        try {
            // Load the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            // Establish connection to the database
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "proj", "hk");
            
            // Prepare SQL statement to insert a leave application into the LEAVEAPPLICATION table
            PreparedStatement ps = con.prepareStatement("INSERT INTO LEAVEAPPLICATION VALUES(?,TO_DATE(?,'YYYY-MM-DD'),TO_DATE(?,'YYYY-MM-DD'),?)");
            
            // Set parameters for the prepared statement
            ps.setString(1, regno);
            ps.setString(2, arrivalDate);
            ps.setString(3, departDate);
            ps.setString(4, reason);
            
            // Execute the update
            int count = ps.executeUpdate();
            
            // If the leave application is successfully inserted, redirect to success.html
            if (count == 1) {
                res.sendRedirect("success.html");
            }
            
            // Print the update count to the response
            pw.print(count);
            
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
