package com.flm.project;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class read {
	
	
	String file="C:\\Users\\HP\\Desktop\\sai.txt";
	void fileread() throws FileNotFoundException
	{
		BufferedReader bf=new BufferedReader(new FileReader(file));
		String line;
		try {
			while((line=bf.readLine())!=null)
			{
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
