/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Menu.java                                          :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/19 16:11:30 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/22 15:23:39 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.util.InputMismatchException;
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
		switch (option) {
			case 1:
				addUser();
				break;
			case 2:
				ViewUserBalance();
				break;
			case 3:
				PerformTransfer();
				break;
			case 4:
				viewTransactions();
				break;
			case 5:
				if (this.isDevMode) removeTransaction();
				else System.exit(0);
				break;
			case 6:
				if (this.isDevMode) checkValidity();
				break;
			case 7:
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Input.");
				break;
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
		if (this.transactionsService.getUsersList().getNumberOfUsers() < 1) {
			System.out.println("there is no users.");
			return ;
		}
		while (true) {
			System.out.println("Inter a user ID");
			System.out.print("-> ");
			try {
				Integer userId = scanner.nextInt();
				scanner.nextLine();
				if (userId < 1) {
					throw new NumberFormatException("Invalid ID");
				}
				User user = this.transactionsService.getUsersList().getUserById(userId - 1);
				System.out.println(user.getName() + " - " + user.getBalance());
				return ;
			}
			catch (InputMismatchException e) {
				System.err.println("Error: Input must be an integer.");
				scanner.nextLine();
			}
			catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
	}

	private void PerformTransfer() {
	
		if (this.transactionsService.getUsersList().getNumberOfUsers() < 2) {
			System.err.println("You can't make a transaction.");
			return ;
		}
		while (true) {
			System.out.println("Enter a sender ID, a recipient ID, and a transfer amount");
			System.out.print("-> ");
			try {
				Integer senderId = scanner.nextInt();
				Integer recipientId = scanner.nextInt();
				Integer amount = scanner.nextInt();
				this.transactionsService.performTransfer(senderId - 1, recipientId - 1, amount);
				System.out.println("The transfer is completed");
				scanner.nextLine();
				return ;
			}
			catch (InputMismatchException e) {
				System.out.println("Error: Invalid input.");
				scanner.nextLine();
			}
			catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
				scanner.nextLine();
			}
		}
	}

	private void viewTransactions() {
		
		if (this.transactionsService.getUsersList().getNumberOfUsers() < 1) {
			System.err.println("No transactions found.");
			return ;
		}
		while (true) {
			System.out.println("Enter a user ID");
			System.out.print("-> ");
			try {
				Integer userId = scanner.nextInt();
				User user = this.transactionsService.getUsersList().getUserById(userId - 1);
				Transaction transactionArray[] = user.getTransactionsList().toArray();
				if (transactionArray.length < 1) {
					System.out.println("No transaction found.");
					return ;
				}
				for (int i = 0; i < transactionArray.length; i++) {
					String signe = "+";
					if (transactionArray[i].getCategory() == Transaction.TransferCategory.DEBITS) {
						signe = "-";
					}
					System.out.println("To " + transactionArray[i].getRecipient().getName() + "(id = " + (transactionArray[i].getRecipient().getId() + 1) + ") " + signe + transactionArray[i].getAmount() + " with id = " + transactionArray[i].getIdentifier());
				}
				return ;
			}
			catch (InputMismatchException e) {
				System.err.println("Error: Invalid input.");
				scanner.nextLine();
			}
			catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
	}
	
	private void removeTransaction() {

		if (this.transactionsService.getUsersList().getNumberOfUsers() < 1) {
			System.out.println("there is no transaction to remove.");
			return ;
		}
		while (true) {
			System.out.println("Enter a user ID and a transfer ID");
			System.out.print("-> ");
			try {
				Integer userId = scanner.nextInt();
				String transferId = scanner.next();
				Transaction[] transactions = this.transactionsService.getUserTransactions(userId - 1);
				if (transactions.length < 1) {
					System.out.println("No transaction found.");
					return ;
				}
				for (int i = 0; i < transactions.length; i++) {
					if (transactions[i].getIdentifier().equals(transferId)) {
						Transaction transaction = transactions[i];
						this.transactionsService.removeTransaction(userId - 1, transferId);
						System.out.println("Transfer To " + transaction.getRecipient().getName() + " (id = " + (transaction.getRecipient().getId() + 1) + ") " + transaction.getAmount() + " removed");
						scanner.nextLine();
						return ;
					}
				}
				throw new IllegalArgumentException("Transaction not found.");
			}
			catch (InputMismatchException e) {
				System.err.println("Error: Invalid input.");
				scanner.nextLine();
			}
			catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
	}

	private void checkValidity() {
		try {
			Transaction[] transactions = this.transactionsService.getUnpairedTransactions();
			if (transactions.length < 1) {
				throw new NullPointerException("No unpair transaction.");
			}
			System.out.println("Check results:");
			for (int i = 0; i < transactions.length; i++) {
				System.out.println(transactions[i].getRecipient().getName() + " has unacknowledged transfer id = " + (transactions[i].getIdentifier() + 1) + " from " + transactions[i].getSender().getName() + "(id = " + (transactions[i].getSender().getId() + 1) + ") for " + transactions[i].getAmount());
			}
		}
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	
}