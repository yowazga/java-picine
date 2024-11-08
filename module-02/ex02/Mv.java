/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Mv.java                                            :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/11/07 20:30:37 by yowazga           #+#    #+#             */
/*   Updated: 2024/11/08 16:04:47 by yowazga          ###   ########.fr       */
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
		if (args.length < 2)
			throw new NeedMoreArguments("mv");
			
		Path firsPath = curentDirectory.resolve(args[0]).normalize();
		Path secondPath = curentDirectory.resolve(args[1]).normalize();
		
		if (!firsPath.toFile().exists())
			throw new NoSuchFileOrDirectory("mv", firsPath.toString());
		if (!firsPath.toFile().canWrite())
			throw new AccessDenid("mv", firsPath.toString());

		if (Files.isDirectory(secondPath)) {
			
			Path destinationPath = secondPath.resolve(firsPath.getFileName());
			if (!secondPath.toFile().exists())
				throw new NoSuchFileOrDirectory("mv", secondPath.toString());
			if (!secondPath.toFile().canWrite())
				throw new AccessDenid("mv", secondPath.toString());
				
			Files.move(firsPath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
		} else {
			
			Files.move(firsPath, secondPath, StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
}