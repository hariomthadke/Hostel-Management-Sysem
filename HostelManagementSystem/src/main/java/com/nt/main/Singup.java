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
@WebServlet("/signup")
public class Singup extends HttpServlet {

    // Handles GET requests
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Prepare to write response
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        // Retrieve form data from the request
        String fName = req.getParameter("firstName");
        String lName = req.getParameter("lastName");
        String regNo = req.getParameter("regNo").toUpperCase();
        String branch = req.getParameter("branch");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String username = req.getParameter("username").toUpperCase();
        String password = req.getParameter("password");
        String role = "user"; // Default role

        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish connection to the database
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "proj", "hk");

            // Prepare SQL statements to insert data into USER_STUDENTS and USERS tables
            PreparedStatement ps = con.prepareStatement("INSERT INTO USER_STUDENTS VALUES(?,?,?,?,?,?)");
            PreparedStatement ps1 = con.prepareStatement("INSERT INTO USERS VALUES(?,?,?)");

            // Set parameters for USER_STUDENTS table
            ps.setString(1, regNo);
            ps.setString(2, fName);
            ps.setString(3, lName);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setString(6, branch);

            // Set parameters for USERS table
            ps1.setString(1, username);
            ps1.setString(2, password);
            ps1.setString(3, role);

            // Execute the insert statements
            int count = ps.executeUpdate();
            int count1 = ps1.executeUpdate();

            // Check if the inserts were successful
            if (count == 1 && count1 == 1) {
                // Redirect to index.html on success
                res.sendRedirect("index.html");
            } else {
                // Display error message if registration failed
                pw.println("<h1>Registration is not done</h1>");
            }

        } catch (Exception e) {
            // Handle exceptions and display error page
            e.printStackTrace();
            pw.println("InvalidCredentials.html");
            // res.sendRedirect("InvalidCredentials.html"); // Uncomment if redirection is preferred
        }
    }

    // Handles POST requests by calling the doGet method
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Delegate the handling of POST requests to the doGet method
        doGet(req, res);
    }
}
