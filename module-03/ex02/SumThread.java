/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   SumThread.java                                     :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/03/03 11:46:16 by yowazga           #+#    #+#             */
/*   Updated: 2025/03/03 14:56:48 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class SumThread extends Thread {
	
	private static int id = 0;
	private int[] array;
	private int start, end;
	private int localSum = 0;
	
	public SumThread(int[] array, int start, int end) {
		this.setName("Thread " + ++id);
		this.array = array;
		this.start = start;
		this.end = end;
	}

	public int getSum() {
		return this.localSum;
	}
	
	@Override
	public void run() {
		for (int i = this.start; i < this.end; i++) {
			this.localSum += this.array[i];
		}
		System.out.println(getName() + " from " + this.start + " to " + this.end + " sum is " + this.localSum);
	}
}
