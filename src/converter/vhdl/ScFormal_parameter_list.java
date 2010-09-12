package converter.vhdl;

import parser.vhdl.ASTNode;
import java.util.ArrayList;


/**
 * <dl> formal_parameter_list ::=
 *   <dd> <i>parameter_</i>interface_list
 */
class ScFormal_parameter_list extends ScVhdl {
    ScInterface_list interface_list = null;
    public ScFormal_parameter_list(ASTNode node) {
        super(node);
        assert(node.getId() == ASTFORMAL_PARAMETER_LIST);
        ASTNode c = (ASTNode)node.getChild(0);
        interface_list = new ScInterface_list(c);
    }

    public String scString() {
        String ret = "";
        ArrayList<ScInterface_element> items = interface_list.items;
        for(int i = 0; i < items.size(); i++) {
            ret += items.get(i).scString();
            if(i < items.size() - 1) {
                ret += ", ";
            }
        }
        return ret;
    }
}
