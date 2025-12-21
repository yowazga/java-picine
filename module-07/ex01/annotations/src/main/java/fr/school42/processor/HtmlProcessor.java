/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   HtmlProcessor.java                                 :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/22 12:17:50 by Younes            #+#    #+#             */
/*   Updated: 2025/12/20 10:56:59 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.processor;

import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import com.google.auto.service.AutoService;

import fr.school42.annotations.HtmlForm;
import fr.school42.annotations.HtmlInput;

@AutoService(Processor.class) //Registers this processor so Maven can find it and run it automatically
@SupportedAnnotationTypes({ //Tells the processor: “Only run if these annotations are used in the code.”
    "fr.school42.annotations.HtmlForm",
    "fr.school42.annotations.HtmlInput"
})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class HtmlProcessor extends AbstractProcessor{

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "HtmlProcessor is running");
        for (Element element : roundEnv.getElementsAnnotatedWith(HtmlForm.class)) {
            
            //Looks for every class (element) annotated with @HtmlForm
            //Skips it if it's not a class (e.g., someone puts it on a method by mistake)
            if (element.getKind() != ElementKind.CLASS) {
                continue;
            }

            HtmlForm form = element.getAnnotation(HtmlForm.class);
            StringBuilder html = new StringBuilder();

            html.append(String.format("<form action=\"%s\" method=\"%s\">\n", form.action(), form.method()));
            
            for (Element enclosed : element.getEnclosedElements()) {
                
                HtmlInput input = enclosed.getAnnotation(HtmlInput.class);
                if (input != null) {
                    html.append(String.format("  <input type=\"%s\" name=\"%s\" placeholder=\"%s\">\n",
                                            input.type(), input.name(), input.placeholder()));
                    
                }
            }

            html.append("  <input type=\"submit\" value=\"Send\">\n</form>");

            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                            "Generating HTML form: " + form.fileName());

            try {

                Filer filer = processingEnv.getFiler();
                FileObject file = filer.createResource(StandardLocation.CLASS_OUTPUT, "", form.fileName(), element);
                try (Writer writer = file.openWriter()) {
                    writer.write(html.toString());
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                        "Successfully wrote: " + file.toUri());
                }
                
            } catch (Exception e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Failed to write HTML: " + e.getMessage());
                  processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Failed to write HTML: " + e.getMessage());
            }
        }
        
        return true;
    }
}
