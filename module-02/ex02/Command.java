/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Command.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/10/19 12:48:50 by yowazga           #+#    #+#             */
/*   Updated: 2024/11/08 15:59:08 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.io.IOException;
import java.nio.file.Path;

public interface Command {

	void runCommands(Path curentDirectory, String[] args) throws IOException;

	public static class TooManyArguments extends IllegalArgumentException {
		public TooManyArguments(String command) {
			super(command + ": too many arguments.");
		}
	}

	public static class NeedMoreArguments extends IllegalArgumentException {
		public NeedMoreArguments(String command) {
			super(command + ": needs more arguments.");
		}
	}

	public static class NoSuchFileOrDirectory extends IllegalArgumentException {
		public NoSuchFileOrDirectory(String command, String fileOrDir) {
			super(command + ": " + fileOrDir + ": No SuchFile Or Directory.");
		}
	}

	public static class AccessDenid extends IllegalArgumentException {
		public AccessDenid(String command, String fileOrDir) {
			super(command + ": " + fileOrDir + ": Access denied.");
		}
	}

	public static class NotDir extends IllegalArgumentException {
		public NotDir(String command, String fileOrDir) {
			super(command + ": " + fileOrDir + ": Not a diretory.");
		}
	}

	public static class InvalidArgumments extends IllegalArgumentException {
		public InvalidArgumments(String command, String fileOrDir) {
			super(command + ": " + fileOrDir + " Invalid arguments.");
		}
	}

	public static class FileAlreadyExists extends IllegalArgumentException {
		FileAlreadyExists(String command, String fileOrDir) {
			super(command + ": " + fileOrDir + "file already exists.");
		}
	}
}