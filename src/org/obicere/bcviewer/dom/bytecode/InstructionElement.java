package org.obicere.bcviewer.dom.bytecode;

import org.obicere.bcviewer.bytecode.instruction.Instruction;
import org.obicere.bcviewer.dom.DocumentBuilder;
import org.obicere.bcviewer.dom.TextAttributesResourcePool;
import org.obicere.bcviewer.dom.TextElement;

/**
 */
public class InstructionElement extends TextElement {

    // the longest mnemonic length + 1
    // the plus 1 is to ensure there is at least 1 space before the params
    private static final int MINIMUM_RIGHT_PAD = 15;

    public InstructionElement(final Instruction instruction, final DocumentBuilder builder) {
        super(instruction.getIdentifier(), instruction.getMnemonic());
        setAttributes(builder.getAttributesPool().get(TextAttributesResourcePool.ATTRIBUTES_INSTRUCTION));
        setLeftPad(builder.getTabSize());
        setRightPad(builder.getTabbedPaddingSize(instruction.getMnemonic().length(), MINIMUM_RIGHT_PAD));
    }

}