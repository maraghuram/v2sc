package converter.vhdl;

import converter.IScStatementBlock;
import parser.vhdl.ASTNode;


/**
 * <dl> simultaneous_procedural_statement ::=
 *   <dd> [ <i>procedural_</i>label : ]
 *   <ul> <b>procedural</b> [ <b>is</b> ]
 *   <ul> procedural_declarative_part
 *   </ul> <b>begin</b>
 *   <ul> procedural_statement_part
 *   </ul> <b>end</b> <b>procedural</b> [ <i>procedural_</i>label ] ; </ul>
 */
class ScSimultaneous_procedural_statement extends ScCommonIdentifier implements IScStatementBlock {
    ScProcedural_declarative_part declarative_part = null;
    ScProcedural_statement_part statement_part = null;
    public ScSimultaneous_procedural_statement(ASTNode node) {
        super(node);
        assert(node.getId() == ASTSIMULTANEOUS_PROCEDURAL_STATEMENT);
        for(int i = 0; i < node.getChildrenNum(); i++) {
            ASTNode c = (ASTNode)node.getChild(i);
            switch(c.getId())
            {
            case ASTPROCEDURAL_DECLARATIVE_PART:
                declarative_part = new ScProcedural_declarative_part(c);
                break;
            case ASTPROCEDURAL_STATEMENT_PART:
                statement_part = new ScProcedural_statement_part(c);
                break;
            case ASTIDENTIFIER:
                identifier = c.firstTokenImage();
                break;
            default:
                break;
            }
        }
        if(identifier.isEmpty())
            identifier = String.format("line%d", node.getFirstToken().beginLine);
    }
    
    private String getName() {
        return "process_procedural_" + identifier;
    }
    
    private String getSpec() {
        return intent() + "void " + getName() + "(void)";
    }

    public String scString() {
        String ret = getSpec() + "\r\n";
        ret += intent() + "{";
        startIntentBlock();
        ret += intent() + declarative_part.toString() + "\r\n";
        ret += intent() + statement_part.toString() + "\r\n";
        endIntentBlock();
        ret += intent() + "}";
        return ret;
    }

    @Override
    public String getDeclaration()
    {
        return getSpec() + ";";
    }

    @Override
    public String getImplements()
    {
        return "";
    }

    @Override
    public String getInitCode()
    {
        // just call it
        return getName() + "();";
    }
}