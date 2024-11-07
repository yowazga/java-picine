/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Ls.java                                            :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/11/05 15:26:04 by yowazga           #+#    #+#             */
/*   Updated: 2024/11/07 20:24:17 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Ls
 */
public class Ls implements Command {

	@Override
	public void runCommands(Path curentDirectory, String[] args) throws IOException {
		
		if (args.length == 0) {
			args = new  String[] { "." };
		}

		for (String arg : args) {
			Path path = curentDirectory.resolve(arg);
			if (!path.toFile().exists()) {
				throw new Command.NoSuchFileOrDirectory("ls", arg);
			}

			if (!path.toFile().canRead()) {
				throw new Command.AccessDenid("ls", arg);
			}

			if (path.toFile().isFile()) {
				listFile(path);
			} else {
				listDirectory(path);
			}
		}
	}

	private void listDirectory(Path path) throws IOException {
		
		Files.list(path).forEach(p -> {
				listFile(p);
		});
	}

	private void listFile(Path path) {
		
		if (!path.getFileName().toString().startsWith("."))
			System.out.println(path.getFileName() + " " + sizeFile(path.toFile().length()));
	}

	private String sizeFile(long size) {

		if (size < 1024) 
			return size + " B";
		else if (size < (1024 * 1024))
			return size / 1024 + " KB";
		else if (size < (1024 * 1024 * 1024))
			return size / (1024 * 1024) + " MB";
		else
			return size / (1024 * 1024 * 1024) + " GB";
	}

}