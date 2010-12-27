package converter.verilog;

import parser.verilog.ASTNode;

/**
 *  name_of_block  <br>
 *     ::=  IDENTIFIER  
 */
class ScName_of_block extends ScVerilog {
    public ScName_of_block(ASTNode node) {
        super(node);
        assert(node.getId() == ASTNAME_OF_BLOCK);
    }

    public String scString() {
        String ret = "";
        return ret;
    }
}
