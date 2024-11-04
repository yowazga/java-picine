/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Command.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/10/19 12:48:50 by yowazga           #+#    #+#             */
/*   Updated: 2024/10/19 13:06:59 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.io.IOException;
import java.nio.file.Path;

public interface Command {

	void runCommands(Path curentDirectory, String args) throws IOException;

	public static class TooManyArguments extends IllegalArgumentException {
		public TooManyArguments(String command) {
			super(command + ": too many arguments");
		}
	}

	public static class NoSuchFileOrDirectory extends IllegalArgumentException {
		public NoSuchFileOrDirectory(String command, String fileOrDir) {
			super(command + )
		}
	}
}