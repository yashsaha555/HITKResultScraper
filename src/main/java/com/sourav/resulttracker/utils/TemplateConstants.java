package com.sourav.resulttracker.utils;

import java.util.Date;

public interface TemplateConstants {
	String header = "============================MARKS LIST============================"
			+ "\n\nRoll No.\t\tName\t\t\tGPA\n\n"
			+ "==================================================================\n\n";
	String footer = "============================END===================================";
	String credits = "Developer: Sourav Saha";
	String headings = "Roll No.\t\tName\t\t\tGPA";
	String generationTime = "Generated on: " + new Date();
}
