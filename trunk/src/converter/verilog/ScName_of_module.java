package converter.verilog;

import parser.verilog.ASTNode;

/**
 *  name_of_module  <br>
 *     ::=  IDENTIFIER  
 */
class ScName_of_module extends ScVerilog {
    public ScName_of_module(ASTNode node) {
        super(node);
        assert(node.getId() == ASTNAME_OF_MODULE);
    }

    public String ScString() {
        String ret = "";
        return ret;
    }
}
