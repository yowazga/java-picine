/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Mv.java                                            :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/11/07 20:30:37 by yowazga           #+#    #+#             */
/*   Updated: 2024/11/07 21:06:55 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Mv implements Command {

	@Override
	public void runCommands(Path curentDirectory, String[] args) throws IOException {
		
		if (args.length > 2)
			throw new TooManyArguments("mv");
		else if (args.length < 2)
			throw new NeedMoreArguments("mv");
		
		Path fromPath = curentDirectory.resolve(args[0]).normalize();
		if (!fromPath.toFile().exists())
			throw new NoSuchFileOrDirectory("mv", args[0]);
			
		if (!fromPath.toFile().canWrite())
			throw new AccessDenid("mv", fromPath.toString());
			
		if (args[1].contains("/")) {
			Path toPath = curentDirectory.resolve(args[1]).normalize();
			fileToMove(fromPath, toPath);
		} else {
			renameFile(fromPath, args[1]);
		}
	}

	private void fileToMove(Path fromPath, Path toPath) throws IOException {
		
		if (!toPath.toFile().exists())
			throw new NoSuchFileOrDirectory("mv", toPath.toString());
		if (!toPath.toFile().isDirectory())
			throw new NotDir("mv", toPath.toString());
		if (!toPath.toFile().canWrite())
			throw new AccessDenid("mv", toPath.toString());

		Path toMove = toPath.resolve(fromPath.getFileName());

		System.out.println(fromPath.normalize());
		System.out.println(toMove.normalize());

		Files.move(fromPath, toMove, StandardCopyOption.REPLACE_EXISTING);
	}

	private void renameFile(Path fromPath, String toPaht) throws IOException {
		
		System.out.println(fromPath.normalize());
		
		Path toMove = fromPath.getParent().resolve(toPaht).normalize();
		
		System.out.println(toMove.normalize());

		Files.move(fromPath, toMove, StandardCopyOption.REPLACE_EXISTING);
	}
	
}