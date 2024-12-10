package com.flm.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class database {
	static Connection con=null;
	public Connection getConnection() {
		if(con==null)
		{
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		 con =DriverManager.getConnection("jdbc:mysql://localhost/practice","root","root");
		 
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		}
	return con;
	}
	
}
