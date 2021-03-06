package org.obicere.bytecode.viewer.modeler;

import org.obicere.bytecode.core.objects.annotation.Annotation;
import org.obicere.bytecode.core.objects.attribute.RuntimeInvisibleAnnotationsAttribute;
import org.obicere.bytecode.viewer.dom.DocumentBuilder;

/**
 */
public class RuntimeInvisibleAnnotationsAttributeModeler implements Modeler<RuntimeInvisibleAnnotationsAttribute> {
    @Override
    public void model(final RuntimeInvisibleAnnotationsAttribute element, final DocumentBuilder builder) {
        final Annotation[] annotations = element.getAnnotations();

        for (final Annotation annotation : annotations) {
            builder.model(annotation);
        }
    }
}
