package converter.vhdl;

import parser.vhdl.ASTNode;


/**
 * <dl> concurrent_procedure_call_statement ::=
 *   <dd> [ label : ] [ <b>postponed</b> ] procedure_call ;
 */
class ScConcurrent_procedure_call_statement extends ScCommonIdentifier implements IStatement {
    ScProcedure_call procedure_call = null;
    public ScConcurrent_procedure_call_statement(ASTNode node) {
        super(node);
        assert(node.getId() == ASTCONCURRENT_PROCEDURE_CALL_STATEMENT);
        for(int i = 0; i < node.getChildrenNum(); i++) {
            ASTNode c = (ASTNode)node.getChild(i);
            switch(c.getId())
            {
            case ASTPROCEDURE_CALL:
                procedure_call = new ScProcedure_call(c);
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

    public String scString() {
        return procedure_call.scString();
    }

    @Override
    public String getDeclaration() {
        return "";
    }

    @Override
    public String getImplements() {
        return "";
    }

    @Override
    public String getInitCode()
    {
        return toString();
    }
}
