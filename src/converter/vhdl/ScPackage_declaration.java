package converter.vhdl;

import parser.vhdl.ASTNode;


/**
 * <dl> package_declaration ::=
 *   <dd> <b>package</b> identifier <b>is</b>
 *   <ul> package_declarative_part
 *   </ul> <b>end</b> [ <b>package</b> ] [ <i>package_</i>simple_name ] ;
 */
class ScPackage_declaration extends ScCommonIdentifier {
    ScVhdl declarative_part = null;
    ScPackage_body body = null;
    public ScPackage_declaration(ASTNode node) {
        super(node);
        assert(node.getId() == ASTPACKAGE_DECLARATION);
        for(int i = 0; i < node.getChildrenNum(); i++) {
            ASTNode c = (ASTNode)node.getChild(i);
            ScVhdl tmp = null;
            switch(c.getId())
            {
            case ASTIDENTIFIER:
                tmp = new ScIdentifier(c);
                identifier = tmp.scString();;
                break;
            case ASTPACKAGE_DECLARATIVE_PART:
                declarative_part = new ScPackage_declarative_part(c);
                break;
            default:
                break;
            }
        }
        units.add(this);
    }
    
    public String getName() {
        return identifier;
    }
    
    public void setPackageBody(ScPackage_body b) {
        body = b;
    }

    public String scString() {
        String ret = "";
        if(body == null) {
            warning("no package body");
        }
        ret += "namespace " + identifier;
        ret += "{\r\n";
        startIntentBlock();
        ret += declarative_part.scString();
        ret += "\r\n";
        ret += body.scString();
        endIntentBlock();
        ret += "};\r\n";
        return ret;
    }
}
