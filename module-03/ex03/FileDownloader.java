/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   FileDownloader.java                                :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/03/19 14:05:34 by yowazga           #+#    #+#             */
/*   Updated: 2025/12/16 16:02:06 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileDownloader {
	
	private ExecutorService executorService;
	private List<String> urls;

	private boolean isValidURL(String url) {
		try {
			new URI(url).toURL();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void createDownloadFolder() throws IOException {
		
		Path folderPath = Paths.get("Download");
		Files.createDirectories(folderPath);
	}

	public FileDownloader(int threadCount, List<String> urls) throws IOException {
		
		this.executorService = Executors.newFixedThreadPool(threadCount);
		this.urls = urls;
		createDownloadFolder();
	}

	public void startDownload() throws IOException, URISyntaxException {
		
		for (int i = 0; i < urls.size(); i++) {
			String url = urls.get(i);
			if (!isValidURL(url)) {
				System.err.println("Invalid url number " + (i + 1));
				continue ;
			}
			Runnable worker = new DownloadTask(i + 1, url);
			executorService.execute(worker);
		}
		executorService.shutdown();

		try {
			if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
				System.err.println("Tasks did not finish in time. Forcing shutdown...");
				executorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			System.err.println("Thread was interrupted while waiting for termination.");
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
	
}