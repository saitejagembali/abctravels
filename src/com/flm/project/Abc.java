package com.flm.project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.sql.Date;


public class Abc {
	static Scanner sc = new Scanner(System.in);
	database db = new database();
	boolean data = false;
	static String source;
	static String dest;
	static int id;
	static int id1;
	static int cou;
	int seats = 0;
	Map<LocalDate, List<Integer>> check = new HashMap();
	

	public boolean signUp() {
		System.out.println("PLEASE ENTER FIRST NAME");
		String fname = sc.next();
		System.out.println("PLEASE ENTER LAST NAME");
		String lname = sc.next();
		System.out.println("PLEASE ENTER MOBILE NUMBER");
		String num = sc.next();
		System.out.println("PLEASE ENTER GENDER");
		String gender = sc.next();
		System.out.println("PLEASE ENTER EMAILID");
		String email = sc.next();
		System.out.println("PLEASE ENTER PASSWORD");
		String password = sc.next();

		try {
			db.getConnection().setReadOnly(false);
			PreparedStatement pst = db.getConnection().prepareStatement(
					"insert into customer (fname,lname,mobile,gender,email,password) values(?,?,?,?,?,?)");
			pst.setString(1, fname);
			pst.setString(2, lname);
			pst.setString(3, num);
			pst.setString(4, gender);
			pst.setString(5, email);
			pst.setString(6, password);
			pst.executeUpdate();

			System.out.println("data added to database");
			data = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;

	}

	public boolean login() {
		String status;
		boolean valid = false;
		System.out.println("PLEASE ENTER YOUR EMAIL-ID");
		String email = sc.next();
		System.out.println("PLEASE ENTER PASSWORD");
		String pass = sc.next();
		try {
			PreparedStatement pst = db.getConnection()
					.prepareStatement("select * from customer where email=? and password=? ");
			pst.setString(1, email);
			pst.setString(2, pass);
			ResultSet rst = pst.executeQuery();
			if (rst.next()) {
				this.id1 = rst.getInt("id");
				status = rst.getString("accountstatus");
				if (status.equalsIgnoreCase("active")) {
					valid = true;
				} else {
					System.out.println("YOUR ACCOUNT IS LOCKED UNABLE TO LOGIN");

				}
			} else {
				cou++;
				if (cou <= 5) {
					login();
				} else {
					PreparedStatement pst1 = db.getConnection()
							.prepareStatement("update customer set accountstatus='INACTIVE' where email=?");
					pst1.setString(1, email);
					pst1.executeUpdate();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valid;

	}

	public void book() {

		System.out.println("PLEASE ENTER SOURCE");
		this.source = sc.next();
		System.out.println("PLEASE ENTER DESTINATION");
		this.dest = sc.next();
		try {
			PreparedStatement pst = db.getConnection()
					.prepareStatement("select * from routes where source=? and destination=?");
			pst.setString(1, source);
			pst.setString(2, dest);
			ResultSet rst = pst.executeQuery();
			while (rst.next()) {
				this.id = rst.getInt(1);
				String source1 = rst.getString(2);
				String destination = rst.getString(3);
				double price = rst.getDouble(4);
				seats = rst.getInt(5);
				Date date = rst.getDate("date");
				int avls = rst.getInt("seats");
				LocalDate date1 = date.toLocalDate();
				DayOfWeek day = date1.getDayOfWeek();
				if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
					price = price + 200;
				}
				System.out.println("SOURCE - " + source1);
				System.out.println("DESTINATION - " + destination);
				System.out.println("PRICE- " + price);
				System.out.println("AVAILABLE SEATS - " + avls + " AVAILABLE DATES - " + date);
				List<Integer> idas=new ArrayList();
				idas.add(id);
				idas.add(seats);
				check.put(date1, idas);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void ticket() {
		LocalDate date = null;

		int seat = 0;
		try {
			// Prompt the user for the number of seats
			System.out.println("PLEASE ENTER NO OF SEATS REQUIRED");
			seat = sc.nextInt();
			sc.nextLine(); // Consume the leftover newline character
			sc.nextLine();
			// Prompt the user for the date
			System.out.println("PLEASE ENTER THE DATE (yyyy-MM-dd)");
			String input = sc.nextLine(); // Read the date as a string

			// Validate and parse the date

			try {
				date = LocalDate.parse(input); // Parse the string to a LocalDate
				List<Integer> val=check.get(date);
				id=val.get(0);
				seats=val.get(1);
		
				

			} catch (java.time.format.DateTimeParseException e) {
				System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
				return; // Exit the method if the date format is invalid
			}

			// Validate the seat count
			boolean check = true;
			while (check) {
				if (seat <= seats) {
					break;
				} else {
					System.out.println(" 1.PLEASE ENTER SEATS ACCORDING TO AVAILABLE SEATS");
					System.out.println("2.WANT TO CANCEL BOOKING");
					int c = sc.nextInt();
					if (c == 1) {
						seat = sc.nextInt();
						System.out.println(seats);
						System.out.println(seat);
						if (seat <= seats) {
							check = false;
							break;
						}
					} else if (c == 2) {
						return;
					} else {
						System.out.println("PLEASE ENTER VALID OPTION");
					}
				}
			}
			
			// Prepare the SQL query
			String query = "INSERT INTO bookings (cid, rid, seats, date) VALUES (?, ?, ?, ?)";

			try (PreparedStatement pst = db.getConnection().prepareStatement(query)) {
				// Set the values for cid and rid
				pst.setInt(1, id1); // Assuming id1 is the customer ID to be set to 'cid'
				pst.setInt(2, id); // Assuming id is the route ID to be set to 'rid'
				pst.setInt(3, seat); // Set the number of seats
				pst.setDate(4, java.sql.Date.valueOf(date)); // Convert LocalDate to java.sql.Date

				// Execute the query
				int rowsInserted = pst.executeUpdate();
				if (rowsInserted > 0) {
					System.out.println("Booking successful!");
				} else {
					System.out.println("Booking failed. Please try again.");
				}
			}

		} catch (SQLException e) {
			System.out.println("Database error occurred. Please try again later.");
			e.printStackTrace(); // Log the detailed error for debugging
		}
		
		try {
			System.out.println(source);
			System.out.println(dest);
			System.out.println(date);
			PreparedStatement pst1 = db.getConnection()
					.prepareStatement("select id,seats from routes where source=? and destination=? and date=? ");
			pst1.setString(1, source);
			pst1.setString(2, dest);
			pst1.setDate(3, java.sql.Date.valueOf(date));
			ResultSet rst1 = pst1.executeQuery();
			while (rst1.next()) {
				int id = rst1.getInt("id");
				seats = rst1.getInt("seats");
			}
			seats = seats - seat;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			PreparedStatement pst1 = db.getConnection().prepareStatement("update routes set seats=? where id=?");
			pst1.setInt(1, seats);
			pst1.setInt(2, id);
			pst1.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void reschedule() {
		String source1 = null;
		String dest1 = null;
		Date date1 = null;
		System.out.println("PLEASE ENTER BOOKING ID");
		int bid = sc.nextInt();
		try {
			PreparedStatement pst = db.getConnection().prepareStatement("select * from bookings where bid=?");
			pst.setInt(1, bid);
			ResultSet rst = pst.executeQuery();
			if (rst.next()) {
				int bid1 = rst.getInt(1);
				int cid = rst.getInt(2);
				int rid = rst.getInt(3);
				int seats = rst.getInt(4);
				Date date = rst.getDate(5);

				PreparedStatement pst1 = db.getConnection().prepareStatement("select * from routes where id=?");
				pst1.setInt(1, rid);
				ResultSet rst1 = pst1.executeQuery();
				while (rst1.next()) {
					source1 = rst1.getString("source");
					dest1 = rst1.getString("destination");
					date1 = rst1.getDate("date");
					System.out.println("BOOKING DETAILS");
					System.out.println(source1 + " " + dest1 + " " + date1);

				}
				PreparedStatement pst2 = db.getConnection()
						.prepareStatement("select date from routes where source=? and destination=?");
				pst2.setString(1, source1);
				pst2.setString(2, dest1);
				ResultSet rst2 = pst2.executeQuery();
				System.out.println("AVAILABLE DATES FOR YOUR ROUTE");

				while (rst2.next()) {
					Date date2 = rst2.getDate("date");
					System.out.println(date2 + ", ");
				}
				System.out.println("PLEASE ENTER THE NEW DATE FOR RESCHEDULE ");
				String input3 = sc.next();
				LocalDate date3 = LocalDate.parse(input3);
				PreparedStatement pst3 = db.getConnection().prepareStatement("update bookings set date=? where bid=?");
				pst3.setDate(1, java.sql.Date.valueOf(date3));
				pst3.setInt(2, bid);
				pst3.executeUpdate();
				System.out.println("RESCHEDULED SUCCESFULLY");
			} else {
				System.out.println("NO BOOKING AVAILABLE FOR THE ABOVE BOOKING-ID");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void activate() {
		System.out.println("PLEASE ENTER REQUIRED DETAILS");
		System.out.println("ENTER YOUR EMAIL ID");
		String email = sc.next();
		System.out.println("ENTER YOUR PASSWORD");
		String pass = sc.next();
		try {
			PreparedStatement pst3 = db.getConnection()
					.prepareStatement("update customer set accountstatus='ACTIVE' where email=? and password=?");
			pst3.setString(1, email);
			pst3.setString(2, pass);
			int a = pst3.executeUpdate();
			if (a > 0) {
				System.out.println("YOUR ACCOUNT IS ACTIVATED");
			} else {
				System.out.println("INVALID LOGIN DETAILS PLEASE TRY AGAIN");
				activate();
			}
		} catch (SQLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
