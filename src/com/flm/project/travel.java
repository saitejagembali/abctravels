package com.flm.project;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class travel {
	static boolean valids;
	static Abc obj = new Abc();
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws FileNotFoundException {
		read r=new read();
		r.fileread();
		while (true) { 
			System.out.println("1. SIGN-UP");
			System.out.println("2. LOGIN");
			System.out.println("3. BOOK A TICKET");
			System.out.println("4. RESCHEDULE YOUR JOURNEY");
			System.out.println("5. REACTIVATE YOUR ACCOUNT");
			System.out.println("6. EXIT");
			int num = sc.nextInt();

			switch (num) {
			case 1:
				boolean value = obj.signUp();
				if (value) {
					System.out.println("SUCCESSFULLY SIGNED-UP");
					System.out.println("DO YOU WANT TO LOGIN NOW? (1. YES / 2. NO)");
					int choice=sc.nextInt();
					if(choice==1)
					{
						num=2;
						continue;
					}
				} else {
					System.out.println("SIGN-UP FAILED. PLEASE TRY AGAIN.");
				}
				break;

			case 2:
				valids = obj.login();
				if (valids) {
					System.out.println("LOGIN SUCCESSFUL");
					System.out.println("DO YOU WANT TO BOOK A TICKET NOW? (1. YES / 2. NO)");
					int choice = sc.nextInt();
					if (choice == 1) {
						num = 3; 
						continue; 
					}
				} 
				
				
				break;

			case 3:
				if (valids) 
				{
						obj.book();
						System.out.println("1. CONTINUE BOOKING");
						System.out.println("2. CANCEL BOOKING");

						int a = sc.nextInt();
						if (a == 1) 
						{
							obj.ticket();
						} 
				}
				 else {
					System.out.println("PLEASE LOGIN INTO YOUR ACCOUNT");
					while (!valids) { 
						System.out.println("1. SIGN-UP");
						System.out.println("2. LOGIN");
						System.out.print("PLEASE SELECT AN OPTION: ");
						int choice = sc.nextInt();

						if (choice == 1) { 
							boolean signupSuccess = obj.signUp();
							if (signupSuccess) {
								System.out.println("SUCCESSFULLY SIGNED-UP");
								valids = obj.login(); 
								if (valids) {
									System.out.println("LOGIN SUCCESSFUL");
								} else {
									System.out.println("LOGIN FAILED. PLEASE TRY AGAIN.");
								}
							} else {
								System.out.println("SIGN-UP FAILED. PLEASE TRY AGAIN.");
							}
						} else if (choice == 2) { // Login option
							valids = obj.login();
							if (valids) {
								System.out.println("LOGIN SUCCESSFUL");
							} else {
								System.out.println("INVALID DETAILS. PLEASE TRY AGAIN.");
							}
						} else {
							System.out.println("INVALID OPTION. PLEASE ENTER 1 FOR SIGN-UP OR 2 FOR LOGIN.");
							break;
						}
					}
					
					num = 3; 
					continue; 
				}
				break;
			case 4:
				obj.reschedule();
				break;
				
			case 5:
				obj.activate();
				break;

			case 6:
				System.out.println("----------THANK YOU!----------");
				System.exit(0); 
				break;

			default:
				System.out.println("INVALID OPTION. PLEASE TRY AGAIN.");
				break;
			}
		}
	}
}
