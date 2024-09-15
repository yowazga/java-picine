/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   TransactionsList.java                              :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/12 11:14:36 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/15 14:50:08 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public interface TransactionsList {

	void addTransaction(Transaction transaction) throws NullPointerException;
	
	void removeTransactionById(String idenifier) throws TransactionsLinkedList.TransactionNotFoundException;
	
	Transaction[] toArray();
}