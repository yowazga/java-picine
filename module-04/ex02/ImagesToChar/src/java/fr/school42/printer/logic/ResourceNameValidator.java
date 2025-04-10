/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   ResourceNameValidator.java                         :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.1337.ma>          +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/04/05 19:09:01 by yowazga           #+#    #+#             */
/*   Updated: 2025/04/07 11:59:10 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.printer.logic;


import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class ResourceNameValidator implements IParameterValidator{
	
	@Override
	public void validate(String name, String value) throws ParameterException {
		if (value == null || value.trim().isEmpty()) {
			throw new ParameterException("Parameter " + name + " should not be empty");
		}
		if (!value.endsWith(".bmp")) {
			throw new ParameterException("Parameter " + name + " should be a BMP file");
		}
	}
}
