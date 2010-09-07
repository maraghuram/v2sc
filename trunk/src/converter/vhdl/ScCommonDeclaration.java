package converter.vhdl;

import parser.vhdl.ASTNode;
import java.util.ArrayList;


/**
 * identifier_list : [mode] subtype_indication | subnature_indication [ signal_kind ] [ := expression ]
 */
class ScCommonDeclaration extends ScVhdl {
    ScIdentifier_list idList = null;
    ScMode mode = null;
    ScVhdl sub = null;
    ScSignal_kind signal_kind = null;
    ScExpression expression = null;
    public ScCommonDeclaration(ASTNode node) {
        super(node);
        for(int i = 0; i < node.getChildrenNum(); i++) {
            ASTNode c = (ASTNode)node.getChild(i);
            switch(c.getId())
            {
            case ASTIDENTIFIER_LIST:
                idList = new ScIdentifier_list(c);
                break;
            case ASTSUBTYPE_INDICATION:
                sub = new ScSubtype_indication(c);
                break;
            case ASTSUBNATURE_INDICATION:
                sub = new ScSubnature_indication(c);
                break;
            case ASTEXPRESSION:
                expression = new ScExpression(c);
                break;
            case ASTMODE:
                mode = new ScMode(c);
                break;
            case ASTSIGNAL_KIND:
                signal_kind = new ScSignal_kind(c);
                break;
            default:
                break;
            }
        }
    }
    
    protected boolean isTypeValid() {
        return true;
    }
    
    public String scString() {
        String ret = "";
        if(signal_kind != null) {
            warning("signal kind ignored:" + signal_kind.token);
        }
        String strType = sub.scString();
        String maxIndex = "";
        int index = strType.indexOf('<');
          if(!isTypeValid() && index > 0) {
            String strTmp = strType.substring(0, index);
            if(strTmp.equals(scType[SC_UINT])) {
                int index2 = strType.lastIndexOf('>');
                maxIndex = strType.substring(index+1, index2).trim();
                strType = "sc_uint_base";
            }
        }
        
        if(curNode.isDescendantOf(ASTPORT_LIST)) {
            if(mode != null) {
                if(mode.scString().equalsIgnoreCase(PORT_IN)) {
                    ret += "sc_in<";
                }else if(mode.scString().equalsIgnoreCase(PORT_OUT)) {
                    ret += "sc_out<";
                }else {
                    warning("token" + mode.scString() + " ignored");
                }
            }
            ret += strType;
            if(strType.endsWith(">")) {
                ret += " ";  // avoid double '>'
            }
            ret += ">";
        }else {
            ret += strType;
        }        
        
        ArrayList<ScIdentifier> items = idList.getItems();
        for(int i = 0; i < items.size(); i++) {
            ret += " " + items.get(i).scString();
            if(!maxIndex.isEmpty()) {
                ret += "(" + maxIndex + ")";
            }
            if(expression != null) {
                ret += " = " + expression.scString();
            }
            if(i < items.size() - 1) {
                ret += ",";
            }
        }
        return ret;
    }
}