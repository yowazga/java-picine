/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Menu.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/19 16:11:30 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/19 20:52:55 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.util.Scanner;


public class Menu {

	private TransactionsService transactionsService;
	private boolean isDevMode;
	private Scanner scanner;


	public Menu(boolean isDevMode) {
		this.transactionsService = new TransactionsService();
		this.isDevMode = isDevMode;
		this.scanner = new Scanner(System.in);
	}

	public void displayMenu() {
		while (true) {
			System.out.println("1. Add a user");
			System.out.println("2. View user balances");
			System.out.println("3. Perform a transfer");
			System.out.println("4. View all transactions for a specific user");
			if (this.isDevMode) {
				System.out.println("5. DEV - remove a transfer by ID");
				System.out.println("6. DEV - check transfer validity");
				System.out.println("7. Finish execution");
			} else {
				System.out.println("5. Finish execution");
			}
	
			getOption();

			System.out.println("---------------------------------------------------------");
		}
	}

	private int getUserInput() {
		
		System.out.print("-> ");
		if (scanner.hasNextInt()) {
			return scanner.nextInt();
		}
		scanner.nextLine();
		return -1;
	}

	private void getOption() {
		int option = getUserInput();
		try {
			switch (option) {
				case 1:
					addUser();
					break;
				case 2:
					ViewUserBalance();
					break;
				// case 3:
				// 	PerformTransfer();
				// 	break;
				case 7:
					System.exit(0);
					break;
				default:
					System.out.println("Invalid Input.");
					break;
			}
		}
		catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		
		
	}

	private void addUser() {
		while (true) {
			try {
				System.out.println("  Enter a user name and a balance");
				System.out.print("-> ");
				if (scanner.hasNext()) {
					String userName = scanner.next();
					if (scanner.hasNext()) {
						if (scanner.hasNextInt()) {
							Integer balance = scanner.nextInt();
							User user = new User(userName, balance);
							this.transactionsService.addUser(user);
							System.out.println("  User with id = " + (user.getId() + 1) + " is added");
							scanner.nextLine();
							return;
						}
					}
					throw new IllegalArgumentException("Invalid input.");
				}
			}
			catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
			
		}
	}

	private void ViewUserBalance() {
		Integer userId;

		System.out.println("Inter a user ID");
		System.out.print("-> ");
		if (scanner.hasNextInt()) {
			userId = scanner.nextInt();
			scanner.nextLine();
			if (userId < 1) {
				throw new NumberFormatException("Invalid ID");
			}
			User user = this.transactionsService.getUsersList().getUserById(userId - 1);
			System.out.println(user.getName() + " - " + user.getBalance());
		}
	}



	
}