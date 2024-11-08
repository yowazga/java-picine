/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/10/14 09:32:58 by yowazga           #+#    #+#             */
/*   Updated: 2024/11/08 16:04:38 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.io.FileWriter;
import java.util.Scanner;

public class Program {

	private static String SIGNATURES_FILE = "signatures.txt";
	private static String RESULT_FILE = "result.txt";
	private static Scanner scanner = new Scanner(System.in);

	public static String readPath() {
		String path = "";
		
		System.out.print("-> ");
		if (scanner.hasNextLine()) {
			path = scanner.nextLine().trim().split("\\s+")[0];
		} else {
			path = "42";
		}
		return path;
	}

	public static void main(String[] args) {
		
		try {
			SignatureAnalyze analyze = new FileSignatureAnalyzer(SIGNATURES_FILE);
			FileWriter writer = new FileWriter(RESULT_FILE);

			while (true) {
				final String FILE_PATH = readPath();
				if (FILE_PATH.equals("42"))
					break;
				String fileType = analyze.analyze(FILE_PATH);
				if (!fileType.equals("UNDEFINED")) {
					writer.write(fileType + "\n");
					writer.flush();
					fileType = "PROCESSED";
				}
				System.err.println(fileType);
			}
			writer.close();
		}
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		finally {
			scanner.close();
		}
	}
}