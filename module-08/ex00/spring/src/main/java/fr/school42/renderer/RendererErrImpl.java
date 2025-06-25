/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   RendererErrImpl.java                               :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/25 16:48:23 by Younes            #+#    #+#             */
/*   Updated: 2025/06/25 16:49:31 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.renderer;

import fr.school42.preprocessor.PreProcessor;

public class RendererErrImpl implements Renderer {

    private final PreProcessor preProcessor;
    
    public RendererErrImpl(PreProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }
    
    @Override
    public void render(String message) {
        
        System.err.println(preProcessor.prossece(message));
    }

}
