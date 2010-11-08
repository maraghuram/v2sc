package converter.vhdl;

import java.util.ArrayList;
import java.util.StringTokenizer;

import common.MyDebug;

import parser.vhdl.ASTNode;
import parser.vhdl.LibSymbolTable;
import parser.vhdl.Symbol;
import parser.vhdl.SymbolTable;


/**
 * <dl> generic_map_aspect ::=
 *   <dd> <b>generic</b> <b>map</b> ( <i>generic_</i>association_list )
 */
class ScGeneric_map_aspect extends ScVhdl {
    ScAssociation_list association_list = null;
    public ScGeneric_map_aspect(ASTNode node) {
        super(node);
        assert(node.getId() == ASTGENERIC_MAP_ASPECT);
        for(int i = 0; i < node.getChildrenNum(); i++) {
            ASTNode c = (ASTNode)node.getChild(i);
            switch(c.getId())
            {
            case ASTASSOCIATION_LIST:
                association_list = new ScAssociation_list(c);
                break;
            default:
                break;
            }
        }
    }
    
    /**
     * map to component
     * @param name: name of component
     */
    public String mapString(String name) {
        String ret = "";
        int i = 0;
        

        SymbolTable symTab = null;
        if(name.indexOf('.') > 0) {
            StringTokenizer tkn = new StringTokenizer(name, ".");
            while(tkn.hasMoreTokens()) {
                symTab = new LibSymbolTable(symTab, tkn.nextToken());
            }
        }else {
            symTab = (SymbolTable)parser.getTableOfSymbol(curNode, name);
            if(symTab == null) {
                MyDebug.printFileLine("library not found:" + name);
                return ret;
            }
        }

        
        Symbol[] syms = symTab.getKindSymbols(GENERIC);
        if(syms == null) {
            MyDebug.printFileLine("component not found:" + name);
            return ret;
        }
        ArrayList<ScAssociation_element> elements = association_list.elements;
        
        for(i = 0; i < elements.size(); i++) {
            if(elements.get(i).formal_part!= null) {
                String genName = elements.get(i).formal_part.scString();
                for(int j = i; j < syms.length; j++) {     // add omitted items
                    Symbol sym = syms[j];
                    if(sym.name.equalsIgnoreCase(genName)) {
                        break;
                    }
                    ret += sym.value;
                    if(j < syms.length - 1 && i < elements.size() - 1) {
                        ret += ", ";
                    }
                }
            }
            ret += elements.get(i).actual_part.scString();
            if(i < elements.size() - 1) {
                ret += ", ";
            }
        }

        return ret;
    }

    public String scString() {
        return "";
    }
}
