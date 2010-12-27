package converter.verilog;

import parser.verilog.ASTNode;

/**
 *  name_of_function  <br>
 *     ::=  IDENTIFIER  
 */
class ScName_of_function extends ScVerilog {
    public ScName_of_function(ASTNode node) {
        super(node);
        assert(node.getId() == ASTNAME_OF_FUNCTION);
    }

    public String scString() {
        String ret = "";
        return ret;
    }
}
