/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/11/09 12:35:45 by yowazga           #+#    #+#             */
/*   Updated: 2024/11/10 16:24:13 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class Program {

	static void ThreadMessage(String message) {
		
		String threadName = Thread.currentThread().getName();
		System.out.format("%s: %s%n", threadName, message);
	}

	private static class MessageLoop implements Runnable {
		
		@Override
		public void run() {
			String importantInfo[] = {
				"Mares eat oats",
				"Does eat oats",
				"Little lambs eat ivy",
				"A kid will eat evy too"
			};

			try {
				for (int i = 0; i < importantInfo.length; i++) {
					
					Thread.sleep(4000);
					
					ThreadMessage(importantInfo[i]);
				}
				
			} catch (InterruptedException e) {
				ThreadMessage("I wasn't done!");
			}
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException{

		long patience = 1000 * 60 * 60;

		if (args.length > 0) {
			
			try {
				patience = Long.parseLong(args[0]) * 1000;
			} catch (NumberFormatException e) {
				System.err.println("argument must be an integer.");
				System.exit(1);
			}
		}

		ThreadMessage("Starting MessageLoop thread");
		
		long startTime = System.currentTimeMillis();
		Thread t = new Thread(new MessageLoop());
		t.start();

		ThreadMessage("Waiting for MessageLooop thread to finish");
		
		while (t.isAlive()) {
			
			ThreadMessage("Still waiting...");
			
			t.join(1000);

			if (((System.currentTimeMillis() - startTime) > patience) && t.isAlive()) {
				
				ThreadMessage("Tired of waiting!");
				t.interrupt();
				t.join();
			}
		}
		ThreadMessage("Finally!");
	}
}
