/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Transaction.java                                   :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/10 07:33:03 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/10 19:36:20 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.util.UUID;

public class Transaction {
	
	public enum TransferCategory {
			DEBITS("OUTCOME", "-"),
			CREDITS("INCOME", "+");

			private final String TEXT;
			private final String SIGN;

			TransferCategory(String text, String sign) {
				this.TEXT = text;
				this.SIGN = sign;
			}

			public String getText() {
				return TEXT;
			}

			public String getSign() {
				return SIGN;
			}
		}
	private String identifier;
	private User recipient;
	private User sender;
	private TransferCategory transferCategory;
	private Integer transferAmount;

	public Transaction(User sender, User recipient, Integer transferAmount) {
		this.identifier = UUID.randomUUID().toString();
		this.recipient = recipient;
		this.sender = sender;
		setCategory(transferAmount);
		this.transferAmount = transferAmount;
	}

	public void setCategory(Integer amount) {
		if (amount < 0) {
			this.transferCategory = TransferCategory.DEBITS;
		} else {
			this.transferCategory = TransferCategory.CREDITS;
		}
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public User getRecipient() {
		return this.recipient;
	}

	public User getSender() {
		return this.sender;
	}

	public TransferCategory getTransferCategory() {
		return this.transferCategory;
	}

	public double getTransferAmount() {
		return this.transferAmount;
	}

	public String toString() {
		return "Transaction [ID: " + getIdentifier() + ", Sender: " + this.sender.getName() + 
		", Recipient: " + this.recipient.getName() + ", Category: " + getTransferCategory() + 
		", Amount: " + getTransferAmount() + "]";
	}
}