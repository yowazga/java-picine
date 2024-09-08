/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/05 20:26:01 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/08 20:45:52 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.util.Scanner;

public class Program {

	private static Scanner scanner = new Scanner(System.in);

	private static final String[] DAYS_WEEK = {"MO", "TU", "WE", "TH", "FR", "SA", "SU"};
	private static final int MONTHE_DAYS = 30;
	private static final int BEGIN_MONTH = 1;

	private static String[] students = new String[10];
	private static boolean[][] scheduleClasses = new boolean[MONTHE_DAYS][6];
	private static int[][][] attendanceRecord = new int[30][6][10];
	private static int studentCount = 0;

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
						for (int i = 0; i < DAYS_WEEK.length; i++) {
							if (DAYS_WEEK[i].equals(day)) {
								isFoun = true;
								int firstDay = i >= BEGIN_MONTH ? i - BEGIN_MONTH : i + 7 - BEGIN_MONTH;
								if (scheduleClasses[firstDay][hour - 1]) {
									System.err.println("Class already scheduled for this time.");
									break ;
								}
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

	private static int getStudentIndex(String student) {
		for (int i = 0; i < studentCount; i++) {
			if (students[i].equals(student)) {
				return i;
			}
		}
		return -1;
	}

	private static void getAttendanceRecording() {
		System.out.println("Record attendance (student, hour, day, status):");
		while (true) {
			if (scanner.hasNextLine()) {
				String student = scanner.next();
				if (student.equals(".")) {
					break ;
				}
				int studentIndex = getStudentIndex(student);
				if (studentIndex == -1) {
					System.err.println("Invalid studnet name.");
					scanner.nextLine();
					continue ;
				}
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
				if (!scheduleClasses[day - 1][hour - 1]) {
					System.err.println("NO class scheduled at this time.");
					scanner.nextLine();
					continue ;
				}
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

	private static void printGraph() {
		System.out.printf("%10s", " ");
		for (int i = 0; i < scheduleClasses.length; i++) {
			for (int j = 0; j <scheduleClasses[i].length; j++) {
				if (scheduleClasses[i][j]) {
					System.out.printf("%1d:00%3s%3d|", j + 1, DAYS_WEEK[(i + BEGIN_MONTH) % 7], i + 1);
				}
			}
		}
		System.out.println();
		for (int s = 0; s < studentCount; s++) {
			System.out.printf("%10s|", students[s]);
			for (int i = 0; i < scheduleClasses.length; i++) {
				for (int j = 0; j < scheduleClasses[i].length; j++) {
					if (scheduleClasses[i][j]) {
						if (attendanceRecord[i][j][s] == 1) {
							System.out.printf("%10s|", "1");
						} else if (attendanceRecord[i][j][s] == -1) {
							System.out.printf("%10s|", "-1");
						} else {
							System.out.printf("%10s|", " ");
						}
					}
				}
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		
		getStudentName();
		getScheduleLessons();
		getAttendanceRecording();
		printGraph();
			
	}
}