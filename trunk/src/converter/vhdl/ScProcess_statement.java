package converter.vhdl;

import converter.IScStatementBlock;
import parser.vhdl.ASTNode;


/**
 * <dl> process_statement ::=
 *   <dd> [ <i>process_</i>label : ]
 *   <ul> [ <b>postponed</b> ] <b>process</b> [ ( sensitivity_list ) ] [ <b>is</b> ]
 *   <ul> process_declarative_part
 *   </ul> <b>begin</b>
 *   <ul> process_statement_part
 *   </ul> <b>end</b> [ <b>postponed</b> ] <b>process</b> [ <i>process_</i>label ] ; </ul>
 */
class ScProcess_statement extends ScCommonIdentifier implements IScStatementBlock {
    ScSensitivity_list sensitivity_list = null;
    ScProcess_declarative_part declarative_part = null;
    ScProcess_statement_part statement_part = null;
    public ScProcess_statement(ASTNode node) {
        super(node);
        assert(node.getId() == ASTPROCESS_STATEMENT);
        for(int i = 0; i < node.getChildrenNum(); i++) {
            ASTNode c = (ASTNode)node.getChild(i);
            switch(c.getId())
            {
            case ASTIDENTIFIER:
                identifier = c.firstTokenImage();
                break;
            case ASTSENSITIVITY_LIST:
                sensitivity_list = new ScSensitivity_list(c);
                break;
            case ASTPROCESS_DECLARATIVE_PART:
                declarative_part = new ScProcess_declarative_part(c);
                break;
            case ASTPROCESS_STATEMENT_PART:
                statement_part = new ScProcess_statement_part(c);
                break;
            default:
                break;
            }
        }
        if(identifier.isEmpty()) {
            identifier = String.format("line%d", node.getFirstToken().beginLine);
        }
    }
    
    private String getName() {
        return "process_" + identifier;
    }
    
    private String getSpec(boolean individual) {
        String ret = intent() + "void ";
        if(individual)
            ret += className + "::";
        return ret + getName() + "(void)";
    }

    public String scString() {
        String ret = "\r\n" + getSpec(individual) + "\r\n";

        ret += startIntentBraceBlock();
        ret += addLF(declarative_part.toString());
        ret += statement_part.toString();
        ret += endIntentBraceBlock();

        return ret;
    }

    @Override
    public String getDeclaration() {
        String ret = "";
        ret += addLF(declarative_part.getDeclaration());
        ret += getSpec(false) + ";";
        return ret;
    }

    @Override
    public String getImplements() {
        String ret = "\r\n" + getSpec(individual) + "\r\n";
        ret += startIntentBraceBlock();
        ret += addLF(declarative_part.getImplements());
        ret += statement_part.toString();
        ret += endIntentBraceBlock();
        return ret;
    }

    @Override
    public String getInitCode()
    {
        String ret = "";
        ret += intent() + "SC_METHOD(" + getName() + ");\r\n";
        if(sensitivity_list != null) {
            ret += intent() + "sensitive";
            for(int i = 0; i < sensitivity_list.items.size(); i++) {
                ret += " << " + sensitivity_list.items.get(i);
            }
            ret += ";";
        }
        return ret;
    }
}
