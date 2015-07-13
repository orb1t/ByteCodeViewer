package org.obicere.bcviewer.bytecode.instruction;

/**
 * @author Obicere
 */
public class goto_ extends Instruction {

    private static final String MNEMONIC = "goto";
    private static final int    OPCODE   = 0xa7;

    private final int branchbyte1;
    private final int branchbyte2;

    public goto_(final int branchbyte1, final int branchbyte2) {
        super(MNEMONIC, OPCODE);
        this.branchbyte1 = branchbyte1;
        this.branchbyte2 = branchbyte2;
    }

    public int getBranchbyte1(){
        return branchbyte1;
    }

    public int getBranchbyte2(){
        return branchbyte2;
    }

    public int getBranchOffest(){
        return (branchbyte1 << 8) | branchbyte2;
    }
}
