package org.obicere.bytecode.viewer.modeler;

import org.obicere.bytecode.core.objects.annotation.Annotation;
import org.obicere.bytecode.core.objects.attribute.RuntimeVisibleAnnotationsAttribute;
import org.obicere.bytecode.viewer.dom.DocumentBuilder;

/**
 */
public class RuntimeVisibleAnnotationsAttributeModeler implements Modeler<RuntimeVisibleAnnotationsAttribute> {
    @Override
    public void model(final RuntimeVisibleAnnotationsAttribute element, final DocumentBuilder builder) {
        final Annotation[] annotations = element.getAnnotations();

        for (final Annotation annotation : annotations) {
            builder.model(annotation);
        }
    }
}
