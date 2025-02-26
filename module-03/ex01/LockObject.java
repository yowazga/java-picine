/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   LockObject.java                                    :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/02/26 15:54:45 by yowazga           #+#    #+#             */
/*   Updated: 2025/02/26 16:05:39 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class LockObject {
	
	private Integer numberOfInswers;
	public boolean turn;

	public LockObject() {this.turn = true;}
	
	public Integer getNumberOfInswers() {return this.numberOfInswers;}
	
	public void setNumberOfInswers(Integer number) {
		this.numberOfInswers = number;
	}
}
