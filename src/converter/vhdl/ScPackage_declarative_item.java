package converter.vhdl;

import parser.vhdl.ASTNode;


/**
 * <dl> package_declarative_item ::=
 *   <dd> subprogram_declaration
 *   <br> | type_declaration
 *   <br> | subtype_declaration
 *   <br> | constant_declaration
 *   <br> | signal_declaration
 *   <br> | <i>shared_</i>variable_declaration
 *   <br> | file_declaration
 *   <br> | alias_declaration
 *   <br> | component_declaration
 *   <br> | attribute_declaration
 *   <br> | attribute_specification
 *   <br> | disconnection_specification
 *   <br> | use_clause
 *   <br> | group_template_declaration
 *   <br> | group_declaration
 *   <br> | nature_declaration
 *   <br> | subnature_declaration
 *   <br> | terminal_declaration
 */
class ScPackage_declarative_item extends ScVhdl {
    ScVhdl item = null;
    public ScPackage_declarative_item(ASTNode node) {
        super(node);
        //assert(node.getId() == ASTPACKAGE_DECLARATIVE_ITEM);
        switch(node.getId())
        {
        case ASTSUBPROGRAM_DECLARATION:
            item = new ScSubprogram_declaration(node);
            break;
        case ASTTYPE_DECLARATION:
            item = new ScType_declaration(node);
            break;
        case ASTSUBTYPE_DECLARATION:
            item = new ScSubtype_declaration(node);
            break;
        case ASTCONSTANT_DECLARATION:
            item = new ScConstant_declaration(node);
            break;
        case ASTSIGNAL_DECLARATION:
            item = new ScSignal_declaration(node);
            break;
        case ASTVARIABLE_DECLARATION:
            item = new ScVariable_declaration(node);
            break;
        case ASTFILE_DECLARATION:
            item = new ScFile_declaration(node);
            break;
        case ASTALIAS_DECLARATION:
            item = new ScAlias_declaration(node);
            break;
        case ASTCOMPONENT_DECLARATION:
            item = new ScComponent_declaration(node);
            break;
        case ASTATTRIBUTE_DECLARATION:
            item = new ScAttribute_declaration(node);
            break;
        case ASTATTRIBUTE_SPECIFICATION:
            item = new ScAttribute_specification(node);
            break;
        case ASTDISCONNECTION_SPECIFICATION:
            item = new ScDisconnection_specification(node);
            break;
        case ASTUSE_CLAUSE:
            item = new ScUse_clause(node);
            break;
        case ASTGROUP_TEMPLATE_DECLARATION:
            item = new ScGroup_template_declaration(node);
            break;
        case ASTGROUP_DECLARATION:
            item = new ScGroup_declaration(node);
            break;
        case ASTNATURE_DECLARATION:
            item = new ScNature_declaration(node);
            break;
        case ASTSUBNATURE_DECLARATION:
            item = new ScSubnature_declaration(node);
            break;
        case ASTTERMINAL_DECLARATION:
            item = new ScTerminal_declaration(node);
            break;
        default:
            break;
        }
    }

    public String scString() {
        if(!(item instanceof ScComponent_declaration))
            return item.scString();
        return "";
    }
}
