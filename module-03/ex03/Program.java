/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/03/05 11:55:44 by yowazga           #+#    #+#             */
/*   Updated: 2025/03/21 00:13:56 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Program {


	private static List<String> getUrls() throws IOException {

		List <String> list = new ArrayList<String>();
		
		File file = new File("files_urls.txt");
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String url;
		while ((url = bufferedReader.readLine()) != null) {
			list.add(url);
		}
		bufferedReader.close();
		
		return list;
	}

	private static int getThreadCount(String threadCount) {

		String[] argument = threadCount.split("=");
		if (!argument[0].equals("--threadsCount"))
			throw new IllegalArgumentException("Wrong Argument.");
		int number = Integer.parseInt(argument[1]);
		if (number < 0)
			throw new NumberFormatException("Number most be positive.");
		
		return number;
	}
	
	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.err.println("Wrong number of arguments");
			System.exit(1);
		}
		try {
			int threadCount = getThreadCount(args[0]);
			List<String> urls = getUrls();
			FileDownloader fileDownloader = new FileDownloader(threadCount, urls);
			fileDownloader.startDownload();
			
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
