/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/11/09 12:35:45 by yowazga           #+#    #+#             */
/*   Updated: 2025/02/25 17:15:22 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class Program {

	private Integer numberOfInswers;

	public Integer getNumberOfInswers(){return this.numberOfInswers;}
	
	public void setNumberOfInswers(Integer number) {
		this.numberOfInswers = number;
	}
	
	private void parsAnswer(String number) throws IllegalArgumentException,  NumberFormatException {
		String[] table = number.split("=");
		if (table.length != 2 || !table[0].equals("--count"))
		throw new IllegalArgumentException("invalid argument");
		setNumberOfInswers(Integer.parseInt(table[1]));
	}
	public static void main(String[] args) throws InterruptedException
	{
		
		if (args.length != 1) {
			System.err.println("wronge number of arguments.");
			System.exit(1);
		}
		try
		{
			Program program = new Program();
			program.parsAnswer(args[0]);
			
			Hen thread1 = new Hen(program.getNumberOfInswers());
			Thread hen = new Thread(thread1);
			Egg egg = new Egg(program.getNumberOfInswers());
			hen.start();
			egg.start();
			hen.join();
			egg.join();
			for (int i = 0; i < program.getNumberOfInswers(); i++)
				System.out.println("Human");
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
