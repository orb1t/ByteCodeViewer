package org.obicere.bcviewer.reader.instruction;

import org.obicere.bcviewer.bytecode.instruction.dreturn;
import org.obicere.bcviewer.util.IndexedDataInputStream;
import org.obicere.bcviewer.util.Reader;

import java.io.IOException;

/**
 * @author Obicere
 */
public class dreturnReader implements Reader<dreturn> {

    private final dreturn instance = new dreturn();

    @Override
    public dreturn read(final IndexedDataInputStream input) throws IOException {
        return instance;
    }
}