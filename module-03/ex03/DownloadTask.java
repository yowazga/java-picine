/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   DownloadTask.java                                  :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/03/19 14:06:23 by yowazga           #+#    #+#             */
/*   Updated: 2025/03/21 00:30:28 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DownloadTask implements Runnable {

	private int fileNumber;
	private String fileName;
	private URL url;

	private String getFileName(String rul) throws URISyntaxException {

		URI uri = new URI(rul);
		String path = uri.getPath();
		return path.substring(path.lastIndexOf('/') + 1);
	}

	public DownloadTask(int fileNumber, String url) throws IOException, URISyntaxException {
		this.fileNumber = fileNumber;
		this.fileName = getFileName(url);
		this.url = new URI(url).toURL();
	}

	private void downloadFile() {
		Path targetPath = Paths.get("download/" + this.fileName);
		File path = new File("download/" + this.fileName);
		try {
			if (path.exists())
				throw new IOException("File arledy exist.");
			Files.copy(this.url.openStream(), targetPath);
		} catch (Exception e) {
			System.err.println("Error download file number " + this.fileNumber + " " + e.getMessage());
		}
		
	}

	@Override
	public void run() {
		
		System.out.println(Thread.currentThread().getName() + " start download file number " + this.fileNumber);
		downloadFile();
		System.out.println(Thread.currentThread().getName() + " finish download file number " + this.fileNumber);
	}
	
}