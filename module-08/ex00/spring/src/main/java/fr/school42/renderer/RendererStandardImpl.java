package fr.school42.renderer;

import fr.school42.preprocessor.PreProcessor;

public class RendererStandardImpl implements Renderer {

    private final PreProcessor preProcessor;

    public RendererStandardImpl(PreProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }

    @Override
    public void render(String message) {
        
        System.out.println(preProcessor.prossece(message));
    }

}
