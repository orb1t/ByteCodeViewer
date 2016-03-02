package org.obicere.bytecode.viewer.modeler.instruction;

import org.obicere.bytecode.core.objects.Constant;
import org.obicere.bytecode.core.objects.ConstantPool;
import org.obicere.bytecode.core.objects.instruction.getstatic;
import org.obicere.bytecode.viewer.dom.DocumentBuilder;

/**
 */
public class getstaticModeler extends InstructionModeler<getstatic> {
    @Override
    protected void modelValue(final getstatic element, final DocumentBuilder builder) {
        final int index = element.getIndex();

        final ConstantPool constantPool = builder.getConstantPool();
        final Constant constant = constantPool.get(index);

        builder.tab();
        builder.model(constant);
    }
}