/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   PreProcessorToLowerImpl.java                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/25 16:33:35 by Younes            #+#    #+#             */
/*   Updated: 2025/06/25 16:34:02 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.preprocessor;

public class PreProcessorToLowerImpl implements PreProcessor {

    @Override
    public String prossece(String message) {
        
        return message.toLowerCase();
    }

}
