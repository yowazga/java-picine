/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   SignatureAnalyze.java                              :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/10/14 09:39:14 by yowazga           #+#    #+#             */
/*   Updated: 2024/10/14 09:40:07 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.io.IOException;

public interface SignatureAnalyze {

	String analyze(String filePath) throws IOException;	
}