package org.obicere.bytecode.viewer.modeler.instruction;

import org.obicere.bytecode.core.objects.code.instruction.DefaultInvokeStatic;
import org.obicere.bytecode.core.objects.constant.AbstractConstant;
import org.obicere.bytecode.core.objects.constant.ConstantPool;
import org.obicere.bytecode.viewer.dom.DocumentBuilder;

/**
 * @author Obicere
 */
public class invokestaticModeler extends InstructionModeler<DefaultInvokeStatic> {
    @Override
    protected void modelValue(final DefaultInvokeStatic element, final DocumentBuilder builder) {
        final int index = element.getIndex();

        final ConstantPool constantPool = builder.getConstantPool();
        final AbstractConstant constant = constantPool.get(index);

        builder.tab();
        builder.model(constant);
    }
}
