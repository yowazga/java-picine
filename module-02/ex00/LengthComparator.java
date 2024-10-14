/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   LengthComparator.java                              :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/10/14 09:42:42 by yowazga           #+#    #+#             */
/*   Updated: 2024/10/14 09:59:58 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.util.Comparator;

public class LengthComparator implements Comparator<String> {

	@Override
	public int compare(String o2, String o1) {
		return (o1.length() - o2.length() == 0) ? o1.compareTo(o2) : o1.length() - o2.length();
	}
}