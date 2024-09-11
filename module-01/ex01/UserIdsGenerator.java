/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   UserIdsGenerator.java                              :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/09/11 10:27:22 by yowazga           #+#    #+#             */
/*   Updated: 2024/09/11 11:36:52 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

public class UserIdsGenerator {

	private static UserIdsGenerator generatorInstance = null;
	private Integer lastId;

	private UserIdsGenerator() {
		this.lastId = -1;
	}

	public static UserIdsGenerator getInstance() {
		
		if (generatorInstance == null) {
			generatorInstance = new UserIdsGenerator();
		}
		return generatorInstance;
	}

	public Integer generateId() {
		return ++this.lastId;
	}
}