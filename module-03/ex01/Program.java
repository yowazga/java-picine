/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/11/09 12:35:45 by yowazga           #+#    #+#             */
/*   Updated: 2025/02/26 16:14:49 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class Program {
	
	public Integer parsAnswer(String number) throws IllegalArgumentException,  NumberFormatException {
		String[] table = number.split("=");
		if (table.length != 2 || !table[0].equals("--count"))
		throw new IllegalArgumentException("invalid argument");
		return Integer.parseInt(table[1]);
	}
	public static void main(String[] args) throws InterruptedException
	{
		
		if (args.length != 1) {
			System.err.println("wronge number of arguments.");
			System.exit(1);
		}
		try
		{
			LockObject lock = new LockObject();
			lock.setNumberOfInswers(new Program().parsAnswer(args[0]));
			
			Hen thread1 = new Hen(lock);
			Thread hen = new Thread(thread1);
			Egg egg = new Egg(lock);
			
			egg.start();
			hen.start();
			
			hen.join();
			egg.join();
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
