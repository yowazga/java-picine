/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/05 20:26:01 by yowazga           #+#    #+#             */
/*   Updated: 2025/08/09 15:47:06 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

/**
 * Class Scheduling and Attendance Tracking System
 * 
 * This program manages:
 * 1. Student registration (up to 10 students)
 * 2. Class scheduling (6 hours per day, 30 days)
 * 3. Attendance recording for scheduled classes
 * 4. Visual representation of the schedule and attendance
 * 
 * Usage:
 * - First: Enter student names (max 10 characters, type '.' to finish)
 * - Second: Schedule classes (hour 1-6 and weekday, type '.' to finish)
 * - Third: Record attendance (student, hour, day, HERE/NOT_HERE, type '.' to finish)
 * - Finally: View the complete schedule and attendance graph
 */

import java.util.Scanner;

public class Program {

	// Scanner for user input
	private static Scanner scanner = new Scanner(System.in);

	// Constants
	private static final String[] DAYS_WEEK = {"MO", "TU", "WE", "TH", "FR", "SA", "SU"};
	private static final int MONTHE_DAYS = 30;  // Total days to track
	private static final int BEGIN_MONTH = 1;   // Starting day of the month (1 = Monday)

	// Data structures
	private static String[] students = new String[10];                    // Student names
	private static boolean[][] scheduleClasses = new boolean[MONTHE_DAYS][6];  // Class schedule: [day][hour-1]
	private static int[][][] attendanceRecord = new int[30][6][10];      // Attendance: [day][hour-1][studentIndex]
	private static int studentCount = 0;                                 // Current number of registered students

	/**
	 * Registers students by collecting their names
	 * - Prompts for student names (max 10 characters, no spaces)
	 * - Type '.' to finish registration
	 * - Maximum 10 students allowed
	 */
	private static void getStudentName() {
		
		System.out.println("Enter student names (max 10 char, no spaces, type '.' to finish):");
		while (true) {
			if (scanner.hasNextLine()) {
				String line = scanner.next();
				if (line.length() < 1 || line.length() > 10) {
					System.err.println("Error: name is too long (max 10 characters).");
					scanner.nextLine();
					continue ;
				} else if (line.equals(".")) {
					break ;
				}
				students[studentCount++] = line;
				if (studentCount > 9) {
					System.out.println("Student limit reached.");
					scanner.nextLine();
					break ;
				}
			}
			else {
				break ;
			}
		}
	}

	/**
	 * Schedules classes for the month
	 * - Prompts for hour (1-6) and weekday (MO, TU, WE, TH, FR, SA, SU)
	 * - Type '.' to finish scheduling
	 * - Validates: no double-booking, max 10 classes per week
	 * - Automatically schedules the class for all weeks in the month
	 */
	private static void getScheduleLessons() {
		
		System.out.println("Schedule classes (hour and weekday, type '.' to finish):");
		while (true) {
			if (scanner.hasNextLine()) {
				if (scanner.hasNextInt()) {
					int hour = scanner.nextInt();
					if (hour < 1 || hour > 6) {
						System.out.println("Error: invalid hour");
						scanner.nextLine();
					} else {
						String day = scanner.next();
						scanner.nextLine();
						boolean isFoun = false;
						
						// Find the weekday in our array
						for (int i = 0; i < DAYS_WEEK.length; i++) {
							if (DAYS_WEEK[i].equals(day)) {
								isFoun = true;
								
								// Calculate the first occurrence of this weekday in the month
								int firstDay = i >= BEGIN_MONTH ? i - BEGIN_MONTH : i + 7 - BEGIN_MONTH;
								
								// Check if class is already scheduled at this time
								if (scheduleClasses[firstDay][hour - 1]) {
									System.err.println("Class already scheduled for this time.");
									break ;
								}
								
								// Count total classes in the week to ensure limit isn't exceeded
								int numberOfClasses = 0; // total must not exceed 10 in week
								for (int j = firstDay; j < firstDay + 7; j++) {
									for (int k = 0; k < scheduleClasses[j].length; k++) {
										if (scheduleClasses[j][k]) {
											numberOfClasses++;
										}
									}
								}
								if (numberOfClasses > 9) {
									System.err.println("Weekly class limit exceeded (max 10).");
									return ;
								}
								
								// Schedule the class for all weeks in the month
								for (int j = firstDay; j < MONTHE_DAYS; j += 7) {
									scheduleClasses[j][hour - 1] = true;
								}
								break ;
							}
						}
						if (!isFoun) {
							System.err.println("Invalid day of the week.");
						}
					}
					continue ;
				} else if (scanner.nextLine().equals(".")) {
					break ;
				} else {
					System.err.println("Error: invalid input");
				}
			} else {
				break ;
			}
		}
	}

	/**
	 * Finds the index of a student in the students array
	 * @param student The name of the student to find
	 * @return Index of the student, or -1 if not found
	 */
	private static int getStudentIndex(String student) {
		for (int i = 0; i < studentCount; i++) {
			if (students[i].equals(student)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Records attendance for scheduled classes
	 * - Prompts for: student name, hour (1-6), day (1-30), status (HERE/NOT_HERE)
	 * - Type '.' to finish recording
	 * - Validates: student exists, class is scheduled, valid inputs
	 * - Records: 1 for HERE, -1 for NOT_HERE, 0 for unrecorded
	 */
	private static void getAttendanceRecording() {
		System.out.println("Record attendance (student, hour, day, status):");
		while (true) {
			if (scanner.hasNextLine()) {
				String student = scanner.next();
				if (student.equals(".")) {
					break ;
				}
				
				// Validate student exists
				int studentIndex = getStudentIndex(student);
				if (studentIndex == -1) {
					System.err.println("Invalid studnet name.");
					scanner.nextLine();
					continue ;
				}
				
				// Get and validate hour
				if (!scanner.hasNextInt()) {
					System.err.println("Invalid class hour");
					scanner.nextLine();
					continue ;
				}
				int hour = scanner.nextInt();
				if (hour < 1 || hour > 6) {
					System.err.println("Invalid class hour.");
					scanner.nextLine();
					continue ;
				}
				
				// Get and validate day
				if (!scanner.hasNextInt()) {
					System.err.println("Invalid day");
					scanner.nextLine();
					continue ;
				}
				int day = scanner.nextInt();
				if (day < 1 || day > 30) {
					System.err.println("Invalid day.");
					scanner.nextLine();
					continue ;
				}
				
				// Check if class is scheduled at this time
				if (!scheduleClasses[day - 1][hour - 1]) {
					System.err.println("NO class scheduled at this time.");
					scanner.nextLine();
					continue ;
				}
				
				// Get and validate attendance status
				if (!scanner.hasNext()) {
					System.err.println("Invalid attendance.");
					scanner.nextLine();
					continue ;
				}
				String attendance = scanner.next();
				if (attendance.equals("HERE")) {
					attendanceRecord[day - 1][hour - 1][studentIndex] = 1;
				} else if (attendance.equals("NOT_HERE")) {
					attendanceRecord[day - 1][hour - 1][studentIndex] = -1;
				}
				else {
					System.err.println("Invalid attendance status.");
					scanner.nextLine();
					continue ;
				}
			} else {
				break ;
			}
		}
	}

	/**
	 * Prints the complete schedule and attendance graph
	 * - First row: Shows scheduled class times with day and date
	 * - Subsequent rows: One row per student showing their attendance
	 * - Attendance values: 1 (HERE), -1 (NOT_HERE), space (unrecorded)
	 */
	private static void printGraph() {
		// Print header row with scheduled class times
		System.out.printf("%10s", " ");
		for (int i = 0; i < scheduleClasses.length; i++) {
			for (int j = 0; j <scheduleClasses[i].length; j++) {
				if (scheduleClasses[i][j]) {
					// Format: "1:00 MO  1|" (hour:00 weekday date|)
					System.out.printf("%1d:00%3s%3d|", j + 1, DAYS_WEEK[(i + BEGIN_MONTH) % 7], i + 1);
				}
			}
		}
		System.out.println();
		
		// Print attendance rows for each student
		for (int s = 0; s < studentCount; s++) {
			System.out.printf("%10s|", students[s]);  // Student name column
			for (int i = 0; i < scheduleClasses.length; i++) {
				for (int j = 0; j < scheduleClasses[i].length; j++) {
					if (scheduleClasses[i][j]) {
						if (attendanceRecord[i][j][s] == 1) {
							System.out.printf("%10s|", "1");        // HERE
						} else if (attendanceRecord[i][j][s] == -1) {
							System.out.printf("%10s|", "-1");      // NOT_HERE
						} else {
							System.out.printf("%10s|", " ");       // Unrecorded
						}
					}
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * Main method - orchestrates the entire program flow
	 * 1. Register students
	 * 2. Schedule classes
	 * 3. Record attendance
	 * 4. Display the final graph
	 */
	public static void main(String[] args) {
		
		getStudentName();
		getScheduleLessons();
		getAttendanceRecording();
		printGraph(); 
			
	}
}