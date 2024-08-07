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

// Annotation to map this servlet to the URL pattern /allot
@WebServlet("/allot")
public class AdminAllotRoom extends HttpServlet {

    // Handle GET requests
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Get the PrintWriter to write response data
        PrintWriter pw = res.getWriter();
        // Set the content type of the response
        res.setContentType("text/html");

        // Retrieve parameters from the request
        String regno = req.getParameter("regno").toUpperCase();
        String roomno = req.getParameter("roomno").toUpperCase();

        try {
            // Load the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Establish a connection to the database
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "proj", "hk");

            // Prepare an SQL statement to get the year of registration
            PreparedStatement ps = con.prepareStatement("SELECT YEAR FROM REGISTRATION WHERE REGNO=?");
            ps.setString(1, regno);
            ResultSet rs = ps.executeQuery();
            String year = null;

            // Retrieve the year from the result set
            if (rs.next()) {
                year = rs.getString(1);
            }

            // Prepare an SQL statement to insert the student into the STUDENTS table
            PreparedStatement ps1 = con.prepareStatement("INSERT INTO STUDENTS VALUES(?,?,?)");
            ps1.setString(1, regno);
            ps1.setString(2, roomno);
            ps1.setString(3, year);

            // Debugging prints
            System.out.println(regno);
            System.out.println(roomno);
            System.out.println(year);

            // Execute the insert statement and get the number of affected rows
            int count = ps1.executeUpdate();
            int count1 = 0;

            // If one row was inserted, delete the student from the REGISTRATION table
            if (count == 1) {
                PreparedStatement ps2 = con.prepareStatement("DELETE FROM REGISTRATION WHERE REGNO=?");
                ps2.setString(1, regno);
                count1 = ps2.executeUpdate();
            }

            // If both the insert and delete operations were successful, redirect to allot.html
            if ((count + count1) == 2) {
                res.sendRedirect("allot.html");
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
