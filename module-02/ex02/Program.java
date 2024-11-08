/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/10/17 12:10:42 by yowazga           #+#    #+#             */
/*   Updated: 2024/11/07 22:08:16 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.nio.file.*;
import java.util.*;

public class Program {

	private Path currentDirectory;
	private Map<String, Command> commands;

	public Program(String starDir) {
		this.currentDirectory = Paths.get(starDir);
		if (!currentDirectory.isAbsolute()) {
			throw new IllegalArgumentException(starDir + " must be absolute.");
		} else if (!currentDirectory.toFile().exists()) {
			throw new IllegalArgumentException(starDir + " does not exist.");
		} else if (!currentDirectory.toFile().isDirectory()) {
			throw new IllegalArgumentException(starDir + " is not a directory.");
		}
		commands = new HashMap<>();
		commands.put("ls", new Ls());
		commands.put("cd", new Cd());
		commands.put("mv", new Mv());
	}

	public void start() {
		System.out.println(currentDirectory.normalize());
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("$> ");
			try {
				String input;
				if (scanner.hasNextLine())
					input = scanner.nextLine().trim();
				else
					input = "exit";

				if (input.isEmpty())
					continue ;
				if (input.equals("exit"))
					break ;
				String[] args = input.split("\\s+");
				String commandName = args[0];
				Command command = commands.get(commandName);
				if (command == null)
					throw new UnknownCommand(commandName);
					
				command.runCommands(currentDirectory, Arrays.copyOfRange(args, 1, args.length));
				if (commandName.equals("cd")) {
					currentDirectory = ((Cd ) command).getCurentPath();
				}
			}
			catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			if (args.length != 1) {
				throw new IllegalArgumentException("use: --curent-folder='path-startdir'");
			}
			else {
				String[] input = args[0].split("=");
				if (input.length != 2 || !input[0].equals("--current-folder")) {
					throw new IllegalArgumentException("use: --current-folder=path-startdir");
				}
				new Program(input[1]).start();
			}
		}
		catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	
	}

	class UnknownCommand extends RuntimeException {
		public UnknownCommand(String command) {
			super(command + ": unknown command.");
		}
	}
}