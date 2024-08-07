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
@WebServlet("/usercomplaint")
public class UserComplaint extends HttpServlet {

    // Handles GET requests
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Get the PrintWriter to write the response
        PrintWriter pw = res.getWriter();
        
        // Set the content type of the response
        res.setContentType("text/html");
        
        // Print a message to the response
        pw.println("user complaint");
        
        // Get parameters from the request and process them
        String roomNo = req.getParameter("room_no").toUpperCase(); // Get room number and convert to uppercase
        String complaintType = req.getParameter("complaint_type"); // Get complaint type
        String description = req.getParameter("description");      // Get description of the complaint
        String date = req.getParameter("assigned_date");           // Get assigned date
        
        // Print the parameters to the response
        pw.println(roomNo);
        pw.println(complaintType);
        pw.println(description);
        pw.println(date);
        
        try {
            // Load the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            // Establish connection to the database
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "proj", "hk");
            
            // Prepare SQL statement to insert a complaint into the COMPLAINT table
            PreparedStatement ps = con.prepareStatement("INSERT INTO COMPLAINT VALUES(?,?,?,SYSDATE)");
            
            // Set parameters for the prepared statement
            ps.setString(1, roomNo);
            ps.setString(2, complaintType);
            ps.setString(3, description);
            
            // Execute the update
            int count = ps.executeUpdate();
            
            // If the complaint is successfully inserted, redirect to success.html
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
