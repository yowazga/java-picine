/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Cd.java                                            :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/11/07 17:51:40 by yowazga           #+#    #+#             */
/*   Updated: 2024/11/07 18:54:07 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.io.IOException;
import java.nio.file.Path;

public class Cd implements Command {
	
	private Path curentPath;

	public Path getCurentPath() {
		return this.curentPath;
	}

	@Override
	public void runCommands(Path curentDirectory, String[] args) throws IOException {

		String newDir;
		if (args.length > 1) {
			throw new TooManyArguments("cd");
		} else if (args.length == 0) {
			newDir = System.getProperty("user.home");
		} else {
			if (args[0].equals("~"))
				newDir = System.getProperty("user.home");
			else
				newDir = args[0];
		}
		
		Path newPath = curentDirectory.resolve(newDir).normalize();

		if (!newPath.toFile().exists())
			throw new NoSuchFileOrDirectory("cd", newDir);
		if (!newPath.toFile().isDirectory())
			throw new NotDir("cd", newDir);

		this.curentPath = newPath;

		System.err.println(this.curentPath.normalize());
	}
}